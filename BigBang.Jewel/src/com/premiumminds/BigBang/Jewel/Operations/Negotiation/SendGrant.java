package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

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
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Negotiation;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class SendGrant
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public OutgoingMessageData mobjMessage;
	private boolean mbEmailSent;

	public SendGrant(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_SendGrant;
	}

	public String ShortDesc()
	{
		return "Envio de Adjudicação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		if ( mbEmailSent )
			return "A negociação foi adjudicada. Foi enviado o seguinte email para a seguradora com a confirmação:" +
					pstrLineBreak + mobjMessage.mstrSubject + pstrLineBreak + mobjMessage.mstrBody;

		return "A negociação foi adjudicada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		TriggerDisallowPolicy lopTDP;
		Timestamp ldtAux;
		Calendar ldtAux2;
		Timestamp ldtLimit;
		Negotiation lobjNegotiation;
		AgendaItem lobjNewAgendaItem;
		IEntity lrefDecos;
		String[] larrReplyTos;
		String[] larrTos;
		int i;

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

		lobjNegotiation = (Negotiation)GetProcess().GetData();

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			lopTDP = new TriggerDisallowPolicy(GetProcess().getKey());
			TriggerOp(lopTDP, pdb);
		}
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());

	    	try
	    	{
				lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewAgendaItem.setAt(0, "Negociação Adjudicada");
				lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
				lobjNewAgendaItem.setAt(2, Constants.ProcID_Negotiation);
				lobjNewAgendaItem.setAt(3, ldtAux);
				lobjNewAgendaItem.setAt(4, ldtLimit);
				lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()},
						new UUID[] {Constants.OPID_Negotiation_CreatePolicy, Constants.OPID_Negotiation_CloseProcess}, pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( mobjMessage.marrContactInfos == null )
		{
			if ( lobjNegotiation.getAt(4) == null )
				larrTos = null;
			else
				larrTos = new String[] {(String)lobjNegotiation.getAt(4)};
		}
		else
		{
			larrTos = new String[mobjMessage.marrContactInfos.length];
			for ( i = 0; i < mobjMessage.marrContactInfos.length; i++ )
			{
				try
				{
					larrTos[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
							mobjMessage.marrContactInfos[i]).getAt(2);
				}
				catch (BigBangJewelException e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				} 
			}
		}

		if ( larrTos != null )
		{
	        lrs = null;
			try
			{
				lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				larrReplyTos = new String[mobjMessage.marrUsers.length];
				for ( i = 0; i < mobjMessage.marrUsers.length; i++ )
				{
					lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {mobjMessage.marrUsers[i]}, new int[0]);
				    if (lrs.next())
				    	larrReplyTos[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
				    else
				    	larrReplyTos[i] = null;
				    lrs.close();
				}
	
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new JewelPetriException(e.getMessage(), e);
			}

			try
			{
				MailConnector.DoSendMail(larrReplyTos, larrTos, mobjMessage.marrCCs, mobjMessage.marrBCCs,
						mobjMessage.mstrSubject, mobjMessage.mstrBody);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
}
