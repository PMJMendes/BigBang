package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public abstract class CreateExternRequestBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrSubject;
	public IncomingMessageData mobjMessage;
	public int mlngDays;
	public UUID midRequestObject;
	private boolean mbFromEmail;
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
					mstrFrom = ((EmailMessage)lobjItem).getFrom().getAddress();
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

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ExternAbortProcess lopEAP;

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		lopEAP = new ExternAbortProcess(midExternProcess);
		if ( mbFromEmail )
			lopEAP.mstrReviveEmailID = mstrNewEmailID;

		TriggerOp(lopEAP, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjDocs;

		lobjDocs = GetDocSet();

		if ( lobjDocs != null )
			return new UndoSet[] {lobjDocs};
		else
			return new UndoSet[0];
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
}
