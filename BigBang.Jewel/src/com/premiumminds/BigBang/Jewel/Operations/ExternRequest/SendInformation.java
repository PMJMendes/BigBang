package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

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
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class SendInformation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public boolean mbIsFinal;
	public int mlngDays;
	public String mstrSubject;
	public String mstrBody;
	public UUID[] marrUsers;
	public UUID[] marrContactInfos;
	public String[] marrCCs;
	public String[] marrBCCs;

	public SendInformation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternReq_SendInformation;
	}

	public String ShortDesc()
	{
		return "Envio de Informação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("A informação enviada foi a seguinte :").append(pstrLineBreak);
		lstrBuffer.append(mstrSubject).append(pstrLineBreak);
		lstrBuffer.append(mstrBody).append(pstrLineBreak).append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		TriggerCloseProcess lopTCP;
		Timestamp ldtAux;
		Calendar ldtAux2;
		Timestamp ldtLimit;
		ExternRequest lobjRequest;
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

		lobjRequest = (ExternRequest)GetProcess().GetData();

		if ( mbIsFinal )
		{
			lopTCP = new TriggerCloseProcess(GetProcess().getKey());
			TriggerOp(lopTCP, pdb);
		}
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, mlngDays);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());

	    	try
	    	{
				lobjRequest.setAt(4, ldtLimit);
				lobjRequest.SaveToDb(pdb);

				lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewAgendaItem.setAt(0, "Pedido Externo de Informação");
				lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
				lobjNewAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
				lobjNewAgendaItem.setAt(3, ldtAux);
				lobjNewAgendaItem.setAt(4, ldtLimit);
				lobjNewAgendaItem.setAt(5, Constants.UrgID_Valid);
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()},
						new UUID[] {Constants.OPID_ExternReq_ReceiveAdditionalInfo, Constants.OPID_ExternReq_CloseProcess}, pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( marrContactInfos == null )
		{
			if ( lobjRequest.getAt(3) == null )
				larrTos = null;
			else
				larrTos = new String[] {(String)lobjRequest.getAt(3)};
		}
		else
		{
			larrTos = new String[marrContactInfos.length];
			for ( i = 0; i < marrContactInfos.length; i++ )
			{
				try
				{
					larrTos[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(), marrContactInfos[i]).getAt(2);
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
				larrReplyTos = new String[marrUsers.length];
				for ( i = 0; i < marrUsers.length; i++ )
				{
					lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {marrUsers[i]}, new int[0]);
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
				MailConnector.DoSendMail(larrReplyTos, larrTos, marrCCs, marrBCCs, mstrSubject, mstrBody);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
}
