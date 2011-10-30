package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public abstract class ExternEndMgrXFerBase
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midXFerProcess;
	public boolean mbAccepted;
	public UUID midOldManager;
	public UUID midNewManager;

	public ExternEndMgrXFerBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Fim de Processo de Transferência de Gestor";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		IUser lobjUser;

		lstrBuffer = new StringBuilder();

		if ( mbAccepted )
		{
			lstrBuffer.append("A transferência foi aceite pelo novo gestor.");

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
		}
		else
		{
			lstrBuffer.append("A transferência de gestor foi cancelada.");

			lstrBuffer.append(pstrLineBreak).append("Gestor actual: ");
			try
			{
				lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), midOldManager);
				lstrBuffer.append(lobjUser.getDisplayName()).append(".");
			}
			catch (Throwable e)
			{
				lstrBuffer.append("(Erro a obter o nome do gestor actual.)");
			}

			lstrBuffer.append(pstrLineBreak).append("Novo gestor pedido: ");
			try
			{
				lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), midNewManager);
				lstrBuffer.append(lobjUser.getDisplayName()).append(".");
			}
			catch (Throwable e)
			{
				lstrBuffer.append("(Erro a obter o nome do novo gestor pedido.)");
			}
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
	}
}
