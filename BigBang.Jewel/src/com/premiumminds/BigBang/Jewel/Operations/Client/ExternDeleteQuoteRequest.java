package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestCoverageData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestObjectData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestSubLineData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestValueData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExternResumeQuoteRequest;

public class ExternDeleteQuoteRequest
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midRequest;
	public String mstrReason;
	private QuoteRequestData mobjData;
	private ContactOps mobjContactOps;
	private DocOps mobjDocOps;

	public ExternDeleteQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_ExternDeleteQuoteRequest;
	}

	public String ShortDesc()
	{
		return "Eliminação de Consulta de Mercado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi eliminada a seguinte consulta de mercado:");
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
		QuoteRequest lobjAux;
		IProcess lobjProcess;
		Contact[] larrContacts;
		Document[] larrDocs;
		QuoteRequestObject[] larrObjects;
		QuoteRequestSubLine[] larrSubLines;
		QuoteRequestCoverage[] larrCoverages;
		QuoteRequestValue[] larrValues;
		int i, j, k;

		try
		{
			lobjAux = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), midRequest);
			mobjData = new QuoteRequestData();
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

			larrSubLines = lobjAux.GetCurrentSubLines();
			larrObjects = lobjAux.GetCurrentObjects();

			if ( (larrSubLines == null) || (larrSubLines.length == 0) )
				mobjData.marrSubLines = null;
			else
			{
				mobjData.marrSubLines = new QuoteRequestSubLineData[larrSubLines.length];
				for ( i = 0; i < larrSubLines.length; i++ )
				{
					mobjData.marrSubLines[i] = new QuoteRequestSubLineData();
					mobjData.marrSubLines[i].FromObject(larrSubLines[i]);

					larrCoverages = larrSubLines[i].GetCurrentCoverages(pdb);
					larrValues = larrSubLines[i].GetCurrentValues(pdb);

					if ( (larrCoverages == null) || (larrCoverages.length == 0) )
						mobjData.marrSubLines[i].marrCoverages = null;
					else
					{
						mobjData.marrSubLines[i].marrCoverages = new QuoteRequestCoverageData[larrCoverages.length];
						for ( j = 0; j < larrCoverages.length; j++ )
						{
							mobjData.marrSubLines[i].marrCoverages[j] = new QuoteRequestCoverageData();
							mobjData.marrSubLines[i].marrCoverages[j].FromObject(larrCoverages[j]);
							larrCoverages[j].getDefinition().Delete(pdb, larrCoverages[j].getKey());
						}
					}

					if ( (larrValues == null) || (larrValues.length == 0) )
						mobjData.marrSubLines[i].marrValues = null;
					else
					{
						mobjData.marrSubLines[i].marrValues = new QuoteRequestValueData[larrValues.length];
						for ( j = 0; j < larrValues.length; j++ )
						{
							mobjData.marrSubLines[i].marrValues[j] = new QuoteRequestValueData();
							mobjData.marrSubLines[i].marrValues[j].FromObject(larrValues[j]);
							mobjData.marrSubLines[i].marrValues[j].mlngObject = -1;
							if ( mobjData.marrSubLines[i].marrValues[j].midObject != null )
							{
								for ( k = 0; k < larrObjects.length; k++ )
								{
									if ( larrObjects[k].getKey().equals(mobjData.marrSubLines[i].marrValues[j].midObject) )
									{
										mobjData.marrSubLines[i].marrValues[j].mlngObject = k;
										break;
									}
								}
							}
							larrValues[j].getDefinition().Delete(pdb, larrValues[j].getKey());
						}
					}

					larrSubLines[i].getDefinition().Delete(pdb, larrSubLines[i].getKey());
				}
			}

			if ( (larrObjects == null) || (larrObjects.length == 0) )
				mobjData.marrObjects = null;
			{
				mobjData.marrObjects = new QuoteRequestObjectData[larrObjects.length];
				for ( i = 0; i < larrObjects.length; i++ )
				{
					mobjData.marrObjects[i] = new QuoteRequestObjectData();
					mobjData.marrObjects[i].FromObject(larrObjects[i]);
					larrObjects[i].getDefinition().Delete(pdb, larrObjects[i].getKey());
				}
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
		return "A consulta apagada será reposta. O histórico de operações será recuperado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi reposta a seguinte consulta de mercado:");
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
		QuoteRequest lobjAux;
		IProcess lobjProcess;
		QuoteRequestObject lobjObject;
		QuoteRequestSubLine lobjSubLine;
		QuoteRequestCoverage lobjCoverage;
		QuoteRequestValue lobjValue;
		int i, j;
		ExternResumeQuoteRequest lopERQR;

		try
		{
			lobjAux = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjData.mid = lobjAux.getKey();

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrObjects[i].midOwner = mobjData.mid;
					mobjData.marrObjects[i].ToObject(lobjObject);
					lobjObject.SaveToDb(pdb);
					mobjData.marrObjects[i].mid = lobjObject.getKey();
				}
			}

			if ( mobjData.marrSubLines != null )
			{
				for ( i = 0; i < mobjData.marrSubLines.length; i++ )
				{
					lobjSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrSubLines[i].midQuoteRequest = mobjData.mid;
					mobjData.marrSubLines[i].ToObject(lobjSubLine);
					lobjSubLine.SaveToDb(pdb);
					mobjData.marrSubLines[i].mid = lobjSubLine.getKey();

					if ( mobjData.marrSubLines[i].marrCoverages != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
						{
							lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].marrCoverages[j].midQRSubLine = mobjData.mid;
							mobjData.marrSubLines[i].marrCoverages[j].ToObject(lobjCoverage);
							lobjCoverage.SaveToDb(pdb);
							mobjData.marrSubLines[i].marrCoverages[j].mid = lobjCoverage.getKey();
						}
					}

					if ( mobjData.marrSubLines[i].marrValues != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
						{
							lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].marrValues[j].midQRSubLine = mobjData.mid;
							if ( mobjData.marrSubLines[i].marrValues[j].mlngObject < 0 )
								mobjData.marrSubLines[i].marrValues[j].midObject = null;
							else
								mobjData.marrSubLines[i].marrValues[j].midObject =
										mobjData.marrObjects[mobjData.marrSubLines[i].marrValues[j].mlngObject].mid;
							mobjData.marrSubLines[i].marrValues[j].ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
							mobjData.marrSubLines[i].marrValues[j].mid = lobjValue.getKey();
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());

			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess);
			lobjProcess.SetDataObjectID(lobjAux.getKey(), pdb);
			lobjProcess.Restart(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopERQR = new ExternResumeQuoteRequest(lobjProcess.getKey());
		TriggerOp(lopERQR, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i;

		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 1;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];

		larrResult[0] = new UndoSet();
		larrResult[0].midType = Constants.ObjID_QuoteRequest;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[0];
		larrResult[0].marrCreated = new UUID[1];
		larrResult[0].marrCreated[0] = mobjData.mid;
		i = 1;

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
