package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.Objects.Negotiation;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public abstract class CreateExternRequestBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrSubject;
	public IncomingMessageData mobjMessage;
	public int mlngDays;
	public UUID midRequestObject;
	private ContactOps mobjContactOps;
	private boolean mbFromEmail;
	private UUID midFrom;
	private String mstrFrom;
	private String mstrNewEmailID;
	private UUID midExternProcess;

	public CreateExternRequestBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Pedido Externo de Informação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Foi recebido o seguinte pedido:").append(pstrLineBreak);
		lstrBuffer.append(mobjMessage.mstrSubject).append(pstrLineBreak);
		lstrBuffer.append(mobjMessage.mstrBody).append(pstrLineBreak).append(pstrLineBreak);
		lstrBuffer.append("Prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.LongDesc(lstrBuffer, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrBuffer, pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Item lobjItem;
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		ExternRequest lobjRequest;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjAgendaItem;

		if ( mobjMessage.mstrEmailID != null )
		{
			mbFromEmail = true;
			try
			{
				lobjItem = MailConnector.DoGetItem(mobjMessage.mstrEmailID);
				mobjMessage.mstrSubject = lobjItem.getSubject();
				mobjMessage.mstrBody = lobjItem.getBody().toString();
				if ( lobjItem instanceof EmailMessage )
				{
					midFrom = getInfoFromEmail(((EmailMessage)lobjItem).getFrom(), pdb);
					mstrFrom = ((EmailMessage)lobjItem).getFrom().getAddress();
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

		try
		{
			lobjRequest = ExternRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjRequest.setAt(1, mobjMessage.mstrSubject);
			lobjRequest.setText(mobjMessage.mstrBody);
			lobjRequest.setAt(3, mstrFrom);
			lobjRequest.setAt(4, ldtLimit);
			lobjRequest.setAt(5, midFrom);
			lobjRequest.SaveToDb(pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_ExternRequest);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjRequest.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			lobjAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjAgendaItem.setAt(0, "Pedido Externo de Informação");
			lobjAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
			lobjAgendaItem.setAt(3, ldtNow);
			lobjAgendaItem.setAt(4, ldtLimit);
			lobjAgendaItem.setAt(5, Constants.UrgID_Pending);
			lobjAgendaItem.SaveToDb(pdb);
			lobjAgendaItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_ExternReq_SendInformation}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

		midRequestObject = lobjRequest.getKey();
		midExternProcess = lobjProc.getKey();

		if ( mbFromEmail )
		{
			try
			{
				mstrNewEmailID = MailConnector.DoProcessItem(mobjMessage.mstrEmailID).getId().getUniqueId();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado será eliminado.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado foi eliminado.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ExternAbortProcess lopEAP;

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		if ( mobjContactOps != null )
			mobjContactOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		lopEAP = new ExternAbortProcess(midExternProcess);
		if ( mbFromEmail )
			lopEAP.mstrReviveEmailID = mstrNewEmailID;

		TriggerOp(lopEAP, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i;

		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 0;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( lobjContacts != null )
		{
			larrResult[i] = lobjContacts;
			i++;
		}

		if ( lobjDocs != null )
		{
			larrResult[i] = lobjDocs;
			i++;
		}

		return larrResult;
	}

	private UndoSet GetDocSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjMessage.mobjDocOps != null )
		{
			larrAux = mobjMessage.mobjDocOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Document.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Document;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

	private UndoSet GetContactSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjContactOps != null )
		{
			larrAux = mobjContactOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Contact.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Contact;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

	private UUID getInfoFromEmail(EmailAddress pobjAddress, SQLServer pdb)
		throws BigBangJewelException, JewelPetriException
	{
		String lstrAddress;
		String lstrName;
		Contact[] larrContacts;
		ContactInfo[] larrInfo;
		int i, j;

		if ( pobjAddress == null )
			return null;

		lstrAddress = pobjAddress.getAddress();
		if ( (lstrAddress == null) || ("".equals(lstrAddress.trim())) )
			return null;
		lstrAddress = lstrAddress.trim();

		lstrName = pobjAddress.getName();
		if ( (lstrName == null) || ("".equals(lstrName.trim())) )
			return null;
		lstrName = lstrName.trim();

		larrContacts = getParentContacts();
		if ( larrContacts == null )
			return null;

		for ( i = 0; i < larrContacts.length; i++ )
		{
			larrInfo = larrContacts[i].getCurrentInfo(pdb);
			for ( j = 0; j < larrInfo.length; j++ )
			{
				if ( Constants.CInfoID_Email.equals(larrInfo[j].getAt(ContactInfo.I.TYPE)) &&
						(lstrAddress.equals(larrInfo[j].getAt(ContactInfo.I.VALUE))) )
					return larrInfo[j].getKey();
			}
		}

		mobjContactOps = new ContactOps();
		mobjContactOps.marrDelete = null;

		for ( i = 0; i < larrContacts.length; i++ )
		{
			if ( lstrName.equals(larrContacts[i].getLabel()) )
			{
				mobjContactOps.marrCreate = null;
				mobjContactOps.marrModify = new ContactData[] {new ContactData()};
				mobjContactOps.marrModify[0].FromObject(larrContacts[i]);
				larrInfo = larrContacts[i].getCurrentInfo(pdb);
				mobjContactOps.marrModify[0].marrInfo = new ContactInfoData[larrInfo.length + 1];
				for ( j = 0; j < larrInfo.length; j++ )
				{
					mobjContactOps.marrModify[0].marrInfo[j] = new ContactInfoData();
					mobjContactOps.marrModify[0].marrInfo[j].FromObject(larrInfo[j]);
				}
				mobjContactOps.marrModify[0].marrInfo[j] = new ContactInfoData();
				mobjContactOps.marrModify[0].marrInfo[j].midOwner = mobjContactOps.marrModify[0].mid;
				mobjContactOps.marrModify[0].marrInfo[j].midType = Constants.CInfoID_Email;
				mobjContactOps.marrModify[0].marrInfo[j].mstrValue = lstrAddress;

				mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());

				return mobjContactOps.marrModify[0].marrInfo[j].mid;
			}
		}

		mobjContactOps.marrModify = null;
		mobjContactOps.marrCreate = new ContactData[] {new ContactData()};
		mobjContactOps.marrCreate[0].mstrName = lstrName;
		mobjContactOps.marrCreate[0].midOwnerType = GetProcess().GetScript().GetDataType();
		mobjContactOps.marrCreate[0].midOwnerId = GetProcess().GetDataKey();
		mobjContactOps.marrCreate[0].mstrAddress1 = null;
		mobjContactOps.marrCreate[0].mstrAddress2 = null;
		mobjContactOps.marrCreate[0].midZipCode = null;
		mobjContactOps.marrCreate[0].midContactType = Constants.CtTypeID_General;
		mobjContactOps.marrCreate[0].marrSubContacts = null;
		mobjContactOps.marrCreate[0].marrInfo = new ContactInfoData[] { new ContactInfoData() };
		mobjContactOps.marrCreate[0].marrInfo[0].midOwner = null;
		mobjContactOps.marrCreate[0].marrInfo[0].midType = Constants.CInfoID_Email;
		mobjContactOps.marrCreate[0].marrInfo[0].mstrValue = lstrAddress;

		mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());

		return mobjContactOps.marrCreate[0].marrInfo[0].mid;
	}

	private Contact[] getParentContacts()
		throws BigBangJewelException
	{
		ObjectBase lobjAux;

		try
		{
			lobjAux = GetProcess().GetData();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjAux instanceof Client )
			return ((Client)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof QuoteRequest )
			return ((QuoteRequest)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Negotiation )
			return ((Negotiation)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Policy )
			return ((Policy)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof SubPolicy )
			return ((SubPolicy)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Receipt )
			return ((Receipt)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Casualty )
			return ((Casualty)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof SubCasualty )
			return ((SubCasualty)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Expense )
			return ((Expense)lobjAux).GetCurrentContacts();

		return null;
	}
}
