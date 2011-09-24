package com.premiumminds.BigBang.Jewel.Operations.Client;

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
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;

public class CreateMgrXFer
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midNewManager;
	public UUID midCreatedSubproc;

	public CreateMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_CreateMgrXFer;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Transferência de Gestor";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;
		
		lstrBuffer = new StringBuilder();
		lstrBuffer.append("Foi iniciado o procedimento para transferir este cliente para um novo gestor.")
				.append(pstrLineBreak).append("Novo gestor: ");

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
		MgrXFer lobjXFer;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		try
		{
			if ( midNewManager == null )
				throw new JewelPetriException("Erro: Novo gestor não indicado.");
			if ( midNewManager.equals(GetProcess().GetManagerID()) )
				throw new JewelPetriException("Erro: O gestor indicado já é o gestor deste cliente.");

			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjXFer.setAt(1, GetProcess().GetManagerID());
			lobjXFer.setAt(2, midNewManager);
			lobjXFer.setAt(3, "Transferência de Gestor de Cliente");
			lobjXFer.setAt(4, Engine.getCurrentUser());
			lobjXFer.SaveToDb(pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), GetProcess().getKey(), pdb);

			ldtAux = new Timestamp(new java.util.Date().getTime());
        	ldtAux2 = Calendar.getInstance();
        	ldtAux2.setTimeInMillis(ldtAux.getTime());
        	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Transferência de Gestor de Cliente");
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_MgrXFer);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Valid);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_CancelXFer}, pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Transferência de Gestor de Cliente");
			lobjItem.setAt(1, midNewManager);
			lobjItem.setAt(2, Constants.ProcID_MgrXFer);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()},
					new UUID[] {Constants.OPID_AcceptXFer, Constants.OPID_CancelXFer}, pdb);

			midCreatedSubproc = lobjScript.getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
