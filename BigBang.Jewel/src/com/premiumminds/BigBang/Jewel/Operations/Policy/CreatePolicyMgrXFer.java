package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.TriggerAllowUndoClientMgrXFer;

public class CreatePolicyMgrXFer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midNewManager;
	public boolean mbMassTransfer;
	public UUID midTransferObject;
	public UUID midCreatedSubproc;
	public boolean mbDirectTransfer;
	private UUID midOldManager;
	private UUID midPolicy;

	public CreatePolicyMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_CreateClientMgrXFer;
	}

	public String ShortDesc()
	{
		if ( mbDirectTransfer )
			return "Alteração do Gestor de Apólice";

		return "Criação de Sub-Processo: Transferência de Gestor";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;

		lstrBuffer = new StringBuilder();

		if ( mbMassTransfer )
			lstrBuffer.append("Esta apólice foi incluída num procedimento de transferência para um novo gestor.");
		else if ( mbDirectTransfer )
			lstrBuffer.append("Foi alterado o gestor desta apólice.");
		else
			lstrBuffer.append("Foi iniciado o procedimento para transferir esta apólice para um novo gestor.");

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
		return midCreatedSubproc;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		MgrXFer lobjXFer;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;

		if ( midNewManager == null )
			throw new JewelPetriException("Erro: Novo gestor não indicado.");
		midOldManager = GetProcess().GetManagerID();

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	if ( !mbMassTransfer )
    	{
    		if ( midNewManager.equals(midOldManager) )
    			throw new JewelPetriException("Erro: O gestor indicado já é o gestor deste cliente.");

    		if ( midNewManager.equals(Engine.getCurrentUser()) )
    		{
    			mbDirectTransfer = true;
    			midPolicy = GetProcess().GetData().getKey();
    			GetProcess().SetManagerID(midNewManager, pdb);
    			TriggerOp(new TriggerAllowUndoClientMgrXFer(GetProcess().getKey()));
	    		try
	    		{
					lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjItem.setAt(0, "Transferência de Gestor de Apólice");
					lobjItem.setAt(1, midOldManager);
					lobjItem.setAt(2, Constants.ProcID_Policy);
					lobjItem.setAt(3, ldtAux);
					lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjItem.setAt(5, Constants.UrgID_Completed);
					lobjItem.SaveToDb(pdb);
					lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
	        	}
	    		catch (Throwable e)
	    		{
	    			throw new JewelPetriException(e.getMessage(), e);
	    		}
    			return;
    		}

    		mbDirectTransfer = false;
    		try
    		{
    			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
    			lobjXFer.setAt(1, GetProcess().GetManagerID());
    			lobjXFer.setAt(2, midNewManager);
    			lobjXFer.setAt(3, "Transferência de Gestor de Apólice");
    			lobjXFer.setAt(4, Engine.getCurrentUser());
    			lobjXFer.setAt(5, false);
				lobjXFer.setAt(6, Constants.ObjID_Policy);
    			lobjXFer.SaveToDb(pdb);

    			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
    			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), GetProcess().getKey(), pdb);

				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Apólice");
				lobjItem.setAt(1, midNewManager);
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()},
						new UUID[] {Constants.OPID_AcceptXFer, Constants.OPID_CancelXFer}, pdb);

				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Apólice");
				lobjItem.setAt(1, Engine.getCurrentUser());
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Valid);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_CancelXFer}, pdb);
        	}
    		catch (Throwable e)
    		{
    			throw new JewelPetriException(e.getMessage(), e);
    		}

        	midTransferObject = lobjXFer.getKey();
			midCreatedSubproc = lobjProc.getKey();
			
			return;
		}

		mbDirectTransfer = false;
	}

	public String UndoDesc(String pstrLineBreak)
	{
		if ( mbMassTransfer || !mbDirectTransfer )
			return "N/A";

		return "Será reposto o gestor anterior.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;
		
		if ( mbMassTransfer || !mbDirectTransfer )
			return "N/A";

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

		return lstrBuffer.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		if ( mbMassTransfer )
			throw new JewelPetriException("Inesperado: Não pode desfazer individualmente transferências de gestor de " +
					"processos transferidos em grupo.");

		if ( !mbDirectTransfer )
			throw new JewelPetriException("Inesperado: Não pode desfazer transferências de gestor para terceiros gestores.");

		if ( midNewManager.equals(midOldManager) )
			return;
		GetProcess().SetManagerID(midOldManager, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;

		if ( !mbDirectTransfer )
			return new UndoSet[0];

		larrResult = new UndoSet[1];
		larrResult[0].midType = Constants.ObjID_Policy;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[1];
		larrResult[0].marrChanged[0] = midPolicy;
		larrResult[0].marrCreated = new UUID[0];
		return larrResult;
	}

	public boolean LocalCanUndo()
	{
		return mbDirectTransfer && !mbMassTransfer;
	}
}
