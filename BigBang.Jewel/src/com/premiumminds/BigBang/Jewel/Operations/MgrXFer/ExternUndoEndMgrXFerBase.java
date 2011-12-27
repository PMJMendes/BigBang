package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public abstract class ExternUndoEndMgrXFerBase
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midXFerProcess;
	public UUID midReopener;
	public UUID midOldManager;
	public UUID midNewManager;

	public ExternUndoEndMgrXFerBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Reabertura de Processo de Transferência de Gestor";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		MgrXFer lobjXFer;

		lobjXFer = null;
		try
		{
			lobjXFer = (MgrXFer)PNProcess.GetInstance(Engine.getCurrentNameSpace(), midXFerProcess).GetData();
		}
		catch(Throwable e)
		{
		}

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O procedimento de transferência de gestor foi reaberto.").append(pstrLineBreak);

		lstrBuffer.append("Pedido original feito por: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), lobjXFer.GetRequestingUser()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do utilizador.)");
		}
		lstrBuffer.append(pstrLineBreak);

		lstrBuffer.append("Reabertura por: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midReopener).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do utilizador.)");
		}
		lstrBuffer.append(pstrLineBreak);

		lstrBuffer.append("Gestor actual: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midOldManager).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor.)");
		}
		lstrBuffer.append(pstrLineBreak);

		lstrBuffer.append("Gestor pedido: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor.)");
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
