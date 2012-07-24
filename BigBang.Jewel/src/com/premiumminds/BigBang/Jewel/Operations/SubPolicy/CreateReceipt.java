package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceReverse;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceShortCircuit;

public class CreateReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_CreateReceipt;
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
		SubPolicy lobjSubP;
		Client lobjClient;
		ExternForceShortCircuit lopEFSC;
		int i;

		try
		{
			if ( mobjData.midManager == null )
				mobjData.midManager = GetProcess().GetManagerID();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = (UUID)GetProcess().GetData().getAt(11);

			lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			for ( i = 3; i < 8; i++ )
				if ( lobjAux.getAt(i) != null )
					lobjAux.setAt(i, ((BigDecimal)lobjAux.getAt(i)).abs());
			if ( lobjAux.getAt(17) != null )
				lobjAux.setAt(17, ((BigDecimal)lobjAux.getAt(17)).abs());
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

			lobjSubP = (SubPolicy)GetProcess().GetData();
			lobjClient = Client.GetInstance(lobjSubP.getNameSpace(), (UUID)lobjSubP.getAt(2));
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( Constants.ProfID_Simple.equals((UUID)lobjClient.getAt(9)) )
		{
			lopEFSC = new ExternForceShortCircuit(lobjProc.getKey());
			TriggerOp(lopEFSC, pdb);
		}

		if ( lobjAux.isReverseCircuit() )
			TriggerOp(new ExternForceReverse(lobjProc.getKey()), pdb);
	}
}
