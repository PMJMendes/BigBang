package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;

public class SetInternational
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midClient;

	public SetInternational(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_SetInternational;
	}

	public String ShortDesc()
	{
		return "Indicação de Internacional";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Este cliente foi marcado como cliente internacional e não necessita de validação do NIF.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjClient;

		lobjClient = (Client)GetProcess().GetData();
		midClient = lobjClient.getKey();

		try
		{
			lobjClient.setAt(Client.I.INTERNATIONAL, true);
			lobjClient.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A indicação de cliente internacional será retirada. A validação do NIF será novamente obrigatória.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação de cliente internacional foi retirada. A validação do NIF é novamente obrigatória.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjClient;

		lobjClient = (Client)GetProcess().GetData();

		try
		{
			lobjClient.setAt(Client.I.INTERNATIONAL, false);
			lobjClient.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Client;
		lobjSet.marrChanged = new UUID[] {midClient};
		lobjSet.marrCreated = null;
		lobjSet.marrDeleted = null;

		return new UndoSet[] {lobjSet};
	}
}
