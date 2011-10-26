package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ClientData;
import com.premiumminds.BigBang.Jewel.Objects.Client;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class MergeIntoAnother
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midClientDestination;
	private ClientData mobjDestination;

	public MergeIntoAnother(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MergeIntoAnother;
	}

	public String ShortDesc()
	{
		return "Fusão com outro cliente.";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("Este cliente foi considerado repetido com o cliente descrito abaixo e foi fundido nesse.")
				.append(pstrLineBreak);

		mobjDestination.Describe(lstrResult, pstrLineBreak);

		lstrResult.append("A reposição deste cliente foi/é possível a partir do processo do cliente destino.")
				.append(pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjDestination.midProcess;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		ExternMergeOtherHere lopEMOH;
		Client lobjAux;

		try
		{
			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), midClientDestination);
		}
		catch (BigBangJewelException e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDestination = new ClientData();
		mobjDestination.FromObject(lobjAux);
		mobjDestination.mobjPrevValues = null;

		lopEMOH = new ExternMergeOtherHere(lobjAux.GetProcessID());
		lopEMOH.midClientSource = GetProcess().GetData().getKey();
		TriggerOp(lopEMOH);
	}
}
