package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;

public class ExternResumeNegotiation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtAgendaDate;

	public ExternResumeNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_ExternResumeNegotiation;
	}

	public String ShortDesc()
	{
		return "Reposição da Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta negociação foi reposta.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		ldtAux = new Timestamp(new java.util.Date().getTime());
		if ( mdtAgendaDate == null )
		{
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);
	    	mdtAgendaDate = new Timestamp(ldtAux2.getTimeInMillis());
		}

    	try
    	{
			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Envio do Pedido de Cotação");
			lobjItem.setAt(1, GetProcess().GetManagerID());
			lobjItem.setAt(2, Constants.ProcID_Negotiation);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, mdtAgendaDate);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_SendQuoteRequest}, pdb);
    	}
    	catch (Throwable e)
    	{
    		throw new JewelPetriException(e.getMessage(), e);
    	}
	}
}
