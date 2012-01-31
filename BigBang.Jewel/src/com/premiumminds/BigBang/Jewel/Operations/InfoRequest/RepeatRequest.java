package com.premiumminds.BigBang.Jewel.Operations.InfoRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.InfoRequest;
import com.premiumminds.BigBang.Jewel.Objects.RequestAddress;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class RepeatRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public int mlngDays;
	public String mstrSubject;
	public String mstrRequestBody;

	public RepeatRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_InfoReq_RepeatRequest;
	}

	public String ShortDesc()
	{
		return "Repetição do Pedido";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O pedido foi re-enviado com o seguinte texto:").append(pstrLineBreak);
		lstrBuffer.append(mstrSubject).append(pstrLineBreak);
		lstrBuffer.append(mstrRequestBody).append(pstrLineBreak).append(pstrLineBreak);
		lstrBuffer.append("Novo prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		InfoRequest lobjRequest;
		RequestAddress[] larrAddresses;
		ArrayList<String> larrTos;
		ArrayList<String> larrCCs;
		ArrayList<String> larrBCCs;
		ArrayList<String> larrReplyTos;
		ArrayList<UUID> larrUsers;
		int i;
		Hashtable<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjNewItem;

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, mlngDays);

		try
		{
			lobjRequest = (InfoRequest)GetProcess().GetData();
			larrAddresses = lobjRequest.GetAddresses(pdb);
			larrReplyTos = new ArrayList<String>();
			larrTos = new ArrayList<String>();
			larrCCs = new ArrayList<String>();
			larrBCCs = new ArrayList<String>();
			larrUsers = new ArrayList<UUID>();
			for ( i = 0; i < larrAddresses.length; i++ )
			{
				if ( Constants.UsageID_To.equals(larrAddresses[i].getAt(2)) )
					larrTos.add(larrAddresses[i].getLabel());
				if ( Constants.UsageID_CC.equals(larrAddresses[i].getAt(2)) )
					larrCCs.add(larrAddresses[i].getLabel());
				if ( Constants.UsageID_BCC.equals(larrAddresses[i].getAt(2)) )
					larrCCs.add(larrAddresses[i].getLabel());
				if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
				{
					larrReplyTos.add(larrAddresses[i].getLabel());
					larrUsers.add((UUID)larrAddresses[i].getAt(3));
				}
			}

			lobjRequest.setAt(3, mlngDays);
			lobjRequest.SaveToDb(pdb);
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

			for ( i = 0; i < larrUsers.size(); i++ )
			{
				lobjNewItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewItem.setAt(0, "Pedido de Informação ou Documento");
				lobjNewItem.setAt(1, larrUsers.get(i));
				lobjNewItem.setAt(2, Constants.ProcID_InfoRequest);
				lobjNewItem.setAt(3, ldtAux);
				lobjNewItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjNewItem.setAt(5, Constants.UrgID_Pending);
				lobjNewItem.SaveToDb(pdb);
				lobjNewItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_InfoReq_ReceiveReply,
						Constants.OPID_InfoReq_RepeatRequest, Constants.OPID_InfoReq_CancelRequest}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			MailConnector.DoSendMail(larrReplyTos.toArray(new String[larrReplyTos.size()]),
					larrTos.toArray(new String[larrTos.size()]), larrCCs.toArray(new String[larrCCs.size()]),
					larrBCCs.toArray(new String[larrBCCs.size()]), mstrSubject, mstrRequestBody);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
