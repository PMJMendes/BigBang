package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public abstract class ExternMassUndoMgrXFerBase
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midXFerProcess;
	public UUID midOldManager;
	public UUID midNewManager;

	public ExternMassUndoMgrXFerBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Desfazer Transferência de Gestor em Massa";
	}

	public String LongDesc(String pstrLineBreak)
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

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midXFerProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		if ( midNewManager.equals(midOldManager) )
			return;
		GetProcess().SetManagerID(midOldManager, pdb);
	}
}
