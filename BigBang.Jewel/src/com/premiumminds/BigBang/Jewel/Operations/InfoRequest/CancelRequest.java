package com.premiumminds.BigBang.Jewel.Operations.InfoRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.InfoRequest;
import com.premiumminds.BigBang.Jewel.Objects.RequestAddress;

public class CancelRequest
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midMotive;
	private String mstrMotive;

	public CancelRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_InfoReq_CancelRequest;
	}

	public String ShortDesc()
	{
		return "Cancelamento do Pedido";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O pedido foi cancelado, pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;

		try
		{
			mstrMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_RequestCancelMotives), midMotive).getLabel();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

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
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo do pedido será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O pedido foi reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		InfoRequest lobjRequest;
		Timestamp ldtAux;
		Calendar ldtAux2;
		RequestAddress[] larrAddresses;
		int i;
		UUID lidUser;
		AgendaItem lobjNewItem;

		try
		{
			lobjRequest = (InfoRequest)GetProcess().GetData();

			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, (Integer)lobjRequest.getAt(3));

			larrAddresses = lobjRequest.GetAddresses(pdb);
			for ( i = 0; i < larrAddresses.length; i++ )
			{
				if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
				{
					lidUser = (UUID)larrAddresses[i].getAt(3);
					if ( lidUser == null )
						continue;

					lobjNewItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjNewItem.setAt(0, "Pedido de Informação ou Documento");
					lobjNewItem.setAt(1, lidUser);
					lobjNewItem.setAt(2, Constants.ProcID_InfoRequest);
					lobjNewItem.setAt(3, ldtAux);
					lobjNewItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjNewItem.setAt(5, Constants.UrgID_Valid);
					lobjNewItem.SaveToDb(pdb);
					lobjNewItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_InfoReq_ReceiveReply,
							Constants.OPID_InfoReq_RepeatRequest, Constants.OPID_InfoReq_CancelRequest}, pdb);
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
