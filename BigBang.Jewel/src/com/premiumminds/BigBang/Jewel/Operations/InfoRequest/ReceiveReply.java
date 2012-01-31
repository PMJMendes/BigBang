package com.premiumminds.BigBang.Jewel.Operations.InfoRequest;

import java.util.ArrayList;
import java.util.UUID;

import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ReceiveReply
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrNotes;
	public String mstrEmailID;
	public DocOps mobjDocOps;
	private boolean mbFromEmail;
	private String mstrSubject;
	private String mstrBody;
	private String mstrNewEmailID;

	public ReceiveReply(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_InfoReq_ReceiveReply;
	}

	public String ShortDesc()
	{
		return "Recepção da Resposta";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A resposta ao pedido foi a seguinte:");

		if ( mstrNotes != null )
			lstrResult.append(pstrLineBreak).append(mstrNotes);

		if ( mbFromEmail )
		{
			lstrResult.append(pstrLineBreak).append(mstrSubject);
			lstrResult.append(pstrLineBreak).append(mstrBody);
		}

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Item lobjItem;

		if ( mobjDocOps != null )
			mobjDocOps.RunSubOp(pdb, GetProcess().GetParent().GetDataKey());

		GetProcess().Stop(pdb);

		if ( mstrEmailID != null )
		{
			mbFromEmail = true;
			try
			{
				lobjItem = MailConnector.DoGetItem(mstrEmailID);
				mstrSubject = lobjItem.getSubject();
				mstrBody = lobjItem.getBody().toString();
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

		lstrResult = new StringBuilder("A resposta será retirada e o processo será reaberto.");

		if ( mobjDocOps != null )
			mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A resposta foi retirada e o processo foi reaberto.");

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Restart(pdb);

		if ( mobjDocOps != null )
			mobjDocOps.UndoSubOp(pdb, GetProcess().GetParent().GetDataKey());

		if ( mbFromEmail )
		{
			try
			{
				MailConnector.DoUnprocessItem(mstrNewEmailID);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
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

		if ( mobjDocOps != null )
		{
			larrAux = mobjDocOps.GetSubSet();
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
