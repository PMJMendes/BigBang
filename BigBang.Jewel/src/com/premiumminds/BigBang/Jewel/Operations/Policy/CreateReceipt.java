package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternBlockDirectRetrocession;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternBlockEndProcessSend;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceReverse;
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
		IProcess lobjMe;
		Policy lobjPolicy;
		Receipt lobjAux;
		IScript lobjScript;
		IProcess lobjProc; 
		TriggerImageOnCreate lopTIOC;
		UUID lidProfile;
		int i;

		try
		{
			lobjMe = GetProcess();
			lobjPolicy = (Policy)lobjMe.GetData();

			if ( (lobjPolicy.getAt(9) != null) && (((Timestamp)lobjPolicy.getAt(9)).compareTo(mobjData.mdtMaturity) < 0) )
				throw new BigBangJewelException("Erro: Não pode criar recibos com data de vencimento superior à data de fim da apólice.");

			if ( mobjData.midManager == null )
				mobjData.midManager = lobjMe.GetManagerID();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = (UUID)lobjPolicy.getAt(11);

			lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			if ( lobjAux.isReverseCircuit() )
			{
				lobjAux.setAt(4, null);
				lobjAux.setAt(5, null);
				lobjAux.setAt(6, null);
			}
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
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), lobjMe.getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;

			if ( "(Directo)".equals(lobjAux.getMediator().getLabel()) )
				TriggerOp(new ExternBlockDirectRetrocession(lobjProc.getKey()), pdb);
			else if ( lobjAux.doCalcRetrocession() )
				lobjAux.SaveToDb(pdb);

			lidProfile = lobjAux.getProfile();
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

		if ( Constants.ProfID_Simple.equals(lidProfile) )
		{
			TriggerOp(new ExternForceShortCircuit(lobjProc.getKey()), pdb);
		}

		if ( Constants.ProfID_External.equals(lidProfile) )
		{
			TriggerOp(new ExternForceShortCircuit(lobjProc.getKey()), pdb);
			TriggerOp(new ExternBlockEndProcessSend(lobjProc.getKey()), pdb);
		}

		if ( lobjAux.isReverseCircuit() )
		{
			TriggerOp(new ExternForceReverse(lobjProc.getKey()), pdb);
			TriggerOp(new ExternBlockEndProcessSend(lobjProc.getKey()), pdb);
		}
	}
}
