package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class DeleteNegotiation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midNegotiation;
	public transient String mstrReason;
	private boolean mbFromPolicy;

	public DeleteNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_DeleteNegotiation;
	}

	public String ShortDesc()
	{
		return "Eliminação da Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A negociação foi eliminada. A sua reposição foi/é possível a partir da " +
				(mbFromPolicy ? "apólice a que diz respeito" : "consulta de mercado a que pertence") + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation lopPDN;
//		com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExternDeleteNegotiation lopQRDN;
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		Timestamp ldtAgendaDate;

		larrItems = new Hashtable<UUID, AgendaItem>();
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
			lrs = null;

			ldtAgendaDate = null;
			for ( AgendaItem lobjItem: larrItems.values() )
			{
				ldtAgendaDate = (Timestamp)lobjItem.getAt(4);
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			mbFromPolicy = true;
			lopPDN = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation(GetProcess().GetParent().getKey());
			lopPDN.midNegotiation = midNegotiation;
			lopPDN.mstrReason = mstrReason;
			lopPDN.mdtAgendaDate = ldtAgendaDate;
			TriggerOp(lopPDN, pdb);
		}
		else
		{
			mbFromPolicy = false;
		}
	}
}
