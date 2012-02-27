package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

public class CancelNegotiation
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public boolean mbSendNotification;
	public OutgoingMessageData mobjMessage;
	private boolean mbHadQuote;
	private Timestamp mdtPrevLimit;

	public CancelNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_CancelNegotiation;
	}

	public String ShortDesc()
	{
		return "Cancelamento da Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();

		lstrBuilder.append("A negociação foi cancelada sem adjudicação.");

		if ( mbSendNotification )
			lstrBuilder.append(" A seguradora foi notificada do facto.");

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		UUID[] larrOpIDs;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		IEntity lrefDecos;
		String[] larrTos;
		String[] larrReplyTos;
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

		mdtPrevLimit = null;
		try
		{
			for ( AgendaItem lobjAgendaItem: larrItems.values() )
			{
				if ( (mdtPrevLimit == null) || (mdtPrevLimit.getTime() > ((Timestamp)lobjAgendaItem.getAt(4)).getTime()) )
					mdtPrevLimit = (Timestamp)lobjAgendaItem.getAt(4);
				if ( !mbHadQuote )
				{
					larrOpIDs = lobjAgendaItem.GetAgendaOpIDs(pdb);
					for ( i = 0; i < larrOpIDs.length; i++ )
					{
						if ( Constants.OPID_Negotiation_SendGrant.equals(larrOpIDs[i]) )
						{
							mbHadQuote = true;
							break;
						}
					}
				}
				lobjAgendaItem.ClearData(pdb);
				lobjAgendaItem.getDefinition().Delete(pdb, lobjAgendaItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Stop(pdb);

		if ( mbSendNotification )
		{
			if ( mobjMessage.marrContactInfos == null )
				larrTos = null;
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
				larrReplyTos = new String[mobjMessage.marrUsers.length];
				lrs = null;
				try
				{
					lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
					for ( i = 0; i < mobjMessage.marrUsers.length; i++ )
					{
						lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {mobjMessage.marrUsers[i]},
								new int[0]);
					    if (lrs.next())
					    	larrReplyTos[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
					    else
					    	larrReplyTos[i] = null;
					    lrs.close();
					}
					lrs = null;

					MailConnector.DoSendMail(larrReplyTos, larrTos, mobjMessage.marrCCs, mobjMessage.marrBCCs,
							mobjMessage.mstrSubject, mobjMessage.mstrBody);
				}
				catch (Throwable e)
				{
					if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
					throw new JewelPetriException(e.getMessage(), e);
				}
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será reaberto e a negociação será retomada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo foi reaberto e a negociação foi retomada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		AgendaItem lobjNewAgendaItem;

		GetProcess().Restart(pdb);

		ldtNow = new Timestamp(new java.util.Date().getTime());

    	try
    	{
			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, mdtPrevLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);

			if ( mbHadQuote )
			{
				lobjNewAgendaItem.setAt(0, "Cotação Recebida");
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_SendGrant,
						Constants.OPID_Negotiation_CancelNegotiation}, pdb);
			}
			else
			{
				lobjNewAgendaItem.setAt(0, "Pedido de Cotação (Enviado) Criado");
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_ReceiveQuote,
						Constants.OPID_Negotiation_RepeatQuoteRequest, Constants.OPID_Negotiation_CancelNegotiation}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
