package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceShortCircuit;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerImageOnCreate;

public class CreateReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public transient DSBridgeData mobjImage;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_CreateReceipt;
	}

	public String ShortDesc()
	{
		return "Criação de Recibo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criado o seguinte recibo:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjAux;
		IScript lobjScript;
		IProcess lobjProc; 
		TriggerImageOnCreate lopTIOC;
		ExternForceShortCircuit lopEFSC;
		Client lobjClient;

		try
		{
			if ( mobjData.midManager == null )
				mobjData.midManager = GetProcess().GetManagerID();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = (UUID)GetProcess().GetData().getAt(11);

			lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Receipt);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjImage != null )
		{
			lopTIOC = new TriggerImageOnCreate(lobjProc.getKey());
			lopTIOC.mobjImage = mobjImage;
			TriggerOp(lopTIOC, pdb);
		}

		lobjClient = (Client)GetProcess().GetParent().GetData();
		if ( Constants.ProfID_Simple.equals((UUID)lobjClient.getAt(9)) )
		{
			lopEFSC = new ExternForceShortCircuit(lobjProc.getKey());
			TriggerOp(lopEFSC, pdb);
		}
	}
}
