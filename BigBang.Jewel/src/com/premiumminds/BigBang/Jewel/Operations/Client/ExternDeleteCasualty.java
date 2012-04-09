package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.CasualtyData;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternResumeCasualty;

public class ExternDeleteCasualty
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midCasualty;
	public String mstrReason;
	private CasualtyData mobjData;
	private ContactOps mobjContactOps;
	private DocOps mobjDocOps;

	public ExternDeleteCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_ExternDeleteCasualty;
	}

	public String ShortDesc()
	{
		return "Eliminação de Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi eliminado o seguinte sinistro:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		lstrResult.append("Razão: ");
		if ( mstrReason != null )
			lstrResult.append(mstrReason);
		else
			lstrResult.append("(não indicada)");
		lstrResult.append(pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Casualty lobjAux;
		Contact[] larrContacts;
		Document[] larrDocs;
		PNProcess lobjProcess;
		int i;

		try
		{
			lobjAux = Casualty.GetInstance(Engine.getCurrentNameSpace(), midCasualty);
			mobjData = new CasualtyData();
			mobjData.FromObject(lobjAux);
			mobjData.mobjPrevValues = null;

			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess);
			lobjProcess.Stop(pdb);
			lobjProcess.SetDataObjectID(null, pdb);

			larrContacts = lobjAux.GetCurrentContacts();
			if ( (larrContacts == null) || (larrContacts.length == 0) )
				mobjContactOps = null;
			else
			{
				mobjContactOps = new ContactOps();
				mobjContactOps.marrDelete = new ContactData[larrContacts.length];
				for ( i = 0; i < larrContacts.length; i++ )
				{
					mobjContactOps.marrDelete[i] = new ContactData();
					mobjContactOps.marrDelete[i].mid = larrContacts[i].getKey();
				}
				mobjContactOps.RunSubOp(pdb, null);
			}

			larrDocs = lobjAux.GetCurrentDocs();
			if ( (larrDocs == null) || (larrDocs.length == 0) )
				mobjDocOps = null;
			else
			{
				mobjDocOps = new DocOps();
				mobjDocOps.marrDelete = new DocumentData[larrDocs.length];
				for ( i = 0; i < larrDocs.length; i++ )
				{
					mobjDocOps.marrDelete[i] = new DocumentData();
					mobjDocOps.marrDelete[i].mid = larrDocs[i].getKey();
				}
				mobjDocOps.RunSubOp(pdb, null);
			}

			lobjAux.getDefinition().Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O sinistro apagado será reposto. O histórico de operações será recuperado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi reposto o seguinte sinistro:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Casualty lobjAux;
		PNProcess lobjProcess;
		ExternResumeCasualty lopERC;

		try
		{
			lobjAux = Casualty.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjData.mid = lobjAux.getKey();

			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess);
			lobjProcess.SetDataObjectID(lobjAux.getKey(), pdb);
			lobjProcess.Restart(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopERC = new ExternResumeCasualty(lobjProcess.getKey());
		TriggerOp(lopERC, pdb);
	}

	public UndoSet[] GetSets()
	{
		return null;
	}
}
