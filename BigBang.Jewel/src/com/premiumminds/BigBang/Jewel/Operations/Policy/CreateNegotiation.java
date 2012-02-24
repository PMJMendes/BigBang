package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Negotiation;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.ExternAllowGrant;

public class CreateNegotiation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public NegotiationData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_CreateNegotiation;
	}

	public String ShortDesc()
	{
		return "Criação de Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criada a seguinte negociação:");
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
		Negotiation lobjAux;
		IScript lobjScript;
		IProcess lobjProc;
		Timestamp ldtAux;
		Calendar ldtAux2;
		AgendaItem lobjItem;

		try
		{
			if ( mobjData.midManager == null )
				mobjData.midManager = GetProcess().GetManagerID();
			if ( mobjData.midCompany == null )
				mobjData.midCompany = (UUID)((Policy)GetProcess().GetData()).getAt(2);

			lobjAux = Negotiation.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Negotiation);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);
			TriggerOp(new ExternAllowGrant(lobjProc.getKey()), pdb);

			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Envio do Pedido de Cotação");
			lobjItem.setAt(1, mobjData.midManager);
			lobjItem.setAt(2, Constants.ProcID_Negotiation);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_Negotiation_SendQuoteRequest}, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
