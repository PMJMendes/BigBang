package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class SendGrant
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public OutgoingMessageData mobjMessage;
	public Timestamp mdtEffectDate;
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
		StringBuilder lstrBuffer;
		ContactInfo lobjInfo;
		User lobjUser;
		int i;

		lstrBuffer = new StringBuilder();
		lstrBuffer.append("A negociação foi adjudicada");

		if ( mdtEffectDate != null )
			lstrBuffer.append(" com data efeito a " + mdtEffectDate.toString().substring(0, 10));
		lstrBuffer.append(".");

		if ( mbEmailSent )
		{
			lstrBuffer.append(" Foi enviado o seguinte email para a seguradora com a confirmação:").append(pstrLineBreak);
			lstrBuffer.append(mobjMessage.mstrSubject).append(pstrLineBreak);
			lstrBuffer.append(mobjMessage.mstrBody).append(pstrLineBreak).append(pstrLineBreak);

			if ( (mobjMessage.marrContactInfos != null) && (mobjMessage.marrContactInfos.length > 0) )
			{
				lstrBuffer.append("A mensagem foi enviada para os seguintes destinatários:").append(pstrLineBreak);
				for ( i = 0; i < mobjMessage.marrContactInfos.length; i++ )
				{
					try
					{
						lobjInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), mobjMessage.marrContactInfos[i]);
						lstrBuffer.append(" - ").append(lobjInfo.getOwner().getLabel()).append(pstrLineBreak);
					}
					catch (Throwable e)
					{
						lstrBuffer.append(" - (Erro a obter o nome do contacto.)").append(pstrLineBreak);
					}
				}
			}

			if ( (mobjMessage.marrUsers != null) && (mobjMessage.marrUsers.length > 0) )
			{
				lstrBuffer.append("O processo foi partilhado com os seguintes utilizadores:").append(pstrLineBreak);
				for ( i = 0; i < mobjMessage.marrUsers.length; i++ )
				{
					try
					{
						lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), mobjMessage.marrUsers[i]);
						lstrBuffer.append(" - ").append(lobjUser.getDisplayName()).append(pstrLineBreak);
					}
					catch (Throwable e)
					{
						lstrBuffer.append(" - (Erro a obter o nome do utilizador.)").append(pstrLineBreak);
					}
				}
			}

			if ( (mobjMessage.marrCCs != null) && (mobjMessage.marrCCs.length > 0) )
			{
				lstrBuffer.append("A mensagem foi enviada para os seguintes CCs:").append(pstrLineBreak);
				for ( i = 0; i < mobjMessage.marrCCs.length; i++ )
				{
					lstrBuffer.append(" - ").append(mobjMessage.marrCCs[i]).append(pstrLineBreak);
				}
			}

			if ( (mobjMessage.marrBCCs != null) && (mobjMessage.marrBCCs.length > 0) )
			{
				lstrBuffer.append("A mensagem foi enviada para os seguintes BCCs:").append(pstrLineBreak);
				for ( i = 0; i < mobjMessage.marrBCCs.length; i++ )
				{
					lstrBuffer.append(" - ").append(mobjMessage.marrBCCs[i]).append(pstrLineBreak);
				}
			}
		}

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		TriggerDisallowPolicy lopTDP;
		Timestamp ldtAux;
		Calendar ldtAux2;
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

		try
		{
			MailConnector.DoSendMail(mobjMessage, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
