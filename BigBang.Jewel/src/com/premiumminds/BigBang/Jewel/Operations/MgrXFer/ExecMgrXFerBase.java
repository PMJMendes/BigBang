package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;

public abstract class ExecMgrXFerBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midNewManager;
	public UUID midMassProcess;
	private UUID midOldManager;
	private UUID midObject;

	public ExecMgrXFerBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public abstract String getObjName();
	public abstract String getArticle();
	public abstract UUID getSafeObjType();
	public abstract UUID getSafeProcType();

	public String ShortDesc()
	{
		if ( midMassProcess == null )
			return "Transferência do gestor de " + getObjName();

		return "Transferência em massa do gestor de " + getObjName();
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;

		lstrBuffer = new StringBuilder();

		if ( midMassProcess == null )
			lstrBuffer.append("Foi alterado o gestor ").append(getDecoration("deste ")).append(getObjName()).append(".");
		else
			lstrBuffer.append(getDecoration("Este ")).append(getObjName()).
			append(" foi incluído num processo de transferência em massa para um novo gestor.");

		lstrBuffer.append(pstrLineBreak).append("Gestor anterior: ");
		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), midOldManager);
			lstrBuffer.append(lobjUser.getDisplayName()).append(".");
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
		}

		lstrBuffer.append(pstrLineBreak).append("Novo gestor: ");
		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), midNewManager);
			lstrBuffer.append(lobjUser.getDisplayName()).append(".");
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do novo gestor.)");
		}

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midMassProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		String lstrObject;
		String lstrUser;
		AgendaItem lobjItem;

		if ( midNewManager == null )
			throw new JewelPetriException("Erro: Novo gestor não indicado.");
		midOldManager = GetProcess().GetManagerID();

		if ( midNewManager.equals(midOldManager) )
			throw new JewelPetriException("Erro: O gestor indicado já é o gestor deste " + getObjName() + ".");

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

		midObject = GetProcess().GetData().getKey();
		GetProcess().SetManagerID(midNewManager, pdb);

		if ( midMassProcess == null )
		{
			try
			{
				lstrObject = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						getSafeObjType()), midObject).getLabel();
			}
			catch (Throwable e)
			{
				lstrObject = "(erro a obter a identificação " + getDecoration("do ") + getObjName() + ")";
			}
			try
			{
				lstrUser = User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName();
			}
			catch (Throwable e)
			{
				lstrUser = "(erro a obter o nome do utilizador)";
			}

			if ( !midOldManager.equals(Engine.getCurrentUser()) )
			{
	    		try
	    		{
					lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjItem.setAt(0, "Transferência de Gestor de " + Character.toUpperCase(getObjName().charAt(0)) +
							getObjName().substring(1));
					lobjItem.setAt(1, midOldManager);
					lobjItem.setAt(2, getSafeProcType());
					lobjItem.setAt(3, ldtAux);
					lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjItem.setAt(5, Constants.UrgID_Completed);
					lobjItem.setAt(6, "A gestão " + getDecoration("do ") + getObjName() + lstrObject + " foi transferida para " +
							lstrUser + ".");
					lobjItem.SaveToDb(pdb);
					lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
	        	}
	    		catch (Throwable e)
	    		{
	    			throw new JewelPetriException(e.getMessage(), e);
	    		}
			}

			if ( !midNewManager.equals(Engine.getCurrentUser()) )
			{
	    		try
	    		{
					lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjItem.setAt(0, "Transferência de Gestor de " + Character.toUpperCase(getObjName().charAt(0)) +
							getObjName().substring(1));
					lobjItem.setAt(1, midNewManager);
					lobjItem.setAt(2, getSafeProcType());
					lobjItem.setAt(3, ldtAux);
					lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjItem.setAt(5, Constants.UrgID_Completed);
					lobjItem.setAt(6, "A gestão " + getDecoration("do ") + getObjName() + lstrObject + " foi transferida para " +
							lstrUser + ".");
					lobjItem.SaveToDb(pdb);
					lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
	        	}
	    		catch (Throwable e)
	    		{
	    			throw new JewelPetriException(e.getMessage(), e);
	    		}
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		if ( midMassProcess == null )
			return "Será reposto o gestor anterior.";

		return "Será reposto o gestor anterior. Este processo será retirado da lista de processos da transferência em massa.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;
		
		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Foi reposto o gestor anterior: ");
		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), midOldManager);
			lstrBuffer.append(lobjUser.getDisplayName()).append(".");
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
		}

		if ( midMassProcess != null )
			lstrBuffer.append(" Este processo foi retirado da lista de processos da transferência em massa.");

		return lstrBuffer.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		MgrXFer lobjXFer;

		if ( midNewManager.equals(midOldManager) )
			return;
		GetProcess().SetManagerID(midOldManager, pdb);

		if ( midMassProcess != null )
		{
			lobjXFer = (MgrXFer)PNProcess.GetInstance(Engine.getCurrentNameSpace(), midMassProcess).GetData();
			try
			{
				lobjXFer.SetUndone(GetProcess().getKey(), pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;

		larrResult = new UndoSet[] {new UndoSet()};
		larrResult[0].midType = getSafeObjType();
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[1];
		larrResult[0].marrChanged[0] = midObject;
		larrResult[0].marrCreated = new UUID[0];
		return larrResult;
	}

	private String getDecoration(String pstrArticle)
	{
		if ( "o".equals(getArticle()) )
			return pstrArticle;

		if ( "Este ".equals(pstrArticle) )
			return "Esta ";

		if ( "este ".equals(pstrArticle) )
			return "esta ";

		if ( "deste ".equals(pstrArticle) )
			return "desta ";

		if ( "do ".equals(pstrArticle) )
			return "da ";

		return pstrArticle;
	}
}
