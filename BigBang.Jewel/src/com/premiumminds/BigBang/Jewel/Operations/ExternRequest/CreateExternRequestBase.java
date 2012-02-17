package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.Item;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

public abstract class CreateExternRequestBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public int mlngDays;
	public String mstrSubject;
	public String mstrBody;
	public String mstrEmailID;
	private boolean mbFromEmail;
	private String mstrFrom;
	private String mstrNewEmailID;
	public UUID midRequestObject;
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
		lstrBuffer.append(mstrSubject).append(pstrLineBreak);
		lstrBuffer.append(mstrBody).append(pstrLineBreak).append(pstrLineBreak);
		lstrBuffer.append("Prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

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

		if ( mstrEmailID != null )
		{
			mbFromEmail = true;
			try
			{
				lobjItem = MailConnector.DoGetItem(mstrEmailID);
				mstrSubject = lobjItem.getSubject();
				mstrBody = lobjItem.getBody().toString();
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
			lobjRequest.setAt(1, mstrSubject);
			lobjRequest.setText(mstrBody);
			lobjRequest.setAt(3, mstrFrom);
			lobjRequest.setAt(4, ldtLimit);
			lobjRequest.SaveToDb(pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_InfoRequest);
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

		midRequestObject = lobjRequest.getKey();
		midExternProcess = lobjProc.getKey();

		if ( mbFromEmail )
		{
			try
			{
				mstrNewEmailID = MailConnector.DoProcessItem(mstrEmailID).getId().getUniqueId();
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

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado foi eliminado.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ExternAbortProcess lopEAP;

		lopEAP = new ExternAbortProcess(midExternProcess);
		if ( mbFromEmail )
			lopEAP.mstrReviveEmailID = mstrNewEmailID;

		TriggerOp(lopEAP, pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
