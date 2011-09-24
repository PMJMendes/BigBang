package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;

public class ExternUndoEndManagerTransfer
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midProcess;
	public UUID midReopener;

	public ExternUndoEndManagerTransfer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoEndMgrXFer;
	}

	public String ShortDesc()
	{
		return "O procedimento de transferência do gestor de cliente foi reaberto.";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;
		MgrXFer lobjXFer;

		lobjXFer = null;
		try
		{
			lobjXFer = (MgrXFer)PNProcess.GetInstance(Engine.getCurrentNameSpace(), midProcess).GetData();
		}
		catch(Throwable e)
		{
		}

		lstrBuffer = new StringBuilder();

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
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), lobjXFer.GetOldManagerID()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor.)");
		}
		lstrBuffer.append(pstrLineBreak);

		lstrBuffer.append("Gestor pedido: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), lobjXFer.GetNewManagerID()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor.)");
		}

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		// Null Op. Node count manipulation only.
	}
}
