package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;

public class SendQuoteRequest
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public SendQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_SendQuoteRequest;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		AgendaItem lobjNewAgendaItem;

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjAgendaItem: larrItems.values() )
			{
				lobjAgendaItem.ClearData(pdb);
				lobjAgendaItem.getDefinition().Delete(pdb, lobjAgendaItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, 7);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

    	try
    	{
			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(0, "Resposta a Pedido de Cotação");
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_Negotiation);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, ldtLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
			lobjNewAgendaItem.SaveToDb(pdb);
			lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_ReceiveQuote,
					Constants.OPID_Negotiation_RepeatQuoteRequest, Constants.OPID_Negotiation_CancelNegotiation}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
