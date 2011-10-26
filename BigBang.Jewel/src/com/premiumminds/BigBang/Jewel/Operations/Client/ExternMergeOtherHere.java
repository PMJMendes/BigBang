package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ClientData;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.DocInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ExternMergeOtherHere
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midClientSource;
	private ClientData mobjSource;
	private ContactOps mobjContactOps;
	private DocOps mobjDocOps;
	private UUID[] marrSubProcIDs;
	private UUID midNewClient;
	private UUID midNewProcess;

	public ExternMergeOtherHere(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternMergeOtherHere;
	}

	public String ShortDesc()
	{
		return "Fusão de outro Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O cliente descrito abaixo foi considerado repetido com este cliente e foi fundido neste.")
				.append(pstrLineBreak);

		mobjSource.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjSource.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Entity lrefClients;
		Client lobjAux;
		Contact[] larrContacts;
		ContactInfo[] larrCInfo;
		Document[] larrDocs;
		DocInfo[] larrDInfo;
		PNProcess lobjProcess;
		IProcess[] larrSubProcs;
		int i, j;

		try
		{
			midNewClient = GetProcess().GetData().getKey();
			midNewProcess = GetProcess().getKey();

			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Client));

			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), midClientSource);
			mobjSource.FromObject(lobjAux);
			mobjSource.mobjPrevValues = null;

			lobjProcess = (PNProcess)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Jewel.Petri.Constants.ObjID_PNProcess), mobjSource.midProcess);
			larrSubProcs = lobjProcess.GetCurrentSubProcesses();

			if ( (larrSubProcs == null) || (larrSubProcs.length == 0) )
				marrSubProcIDs = null;
			else
			{
				marrSubProcIDs = new UUID[larrSubProcs.length];
				for ( i = 0; i < larrSubProcs.length; i++ )
				{
					marrSubProcIDs[i] = larrSubProcs[i].getKey();
					larrSubProcs[i].SetParentProcId(midNewProcess, pdb);
				}
			}

			lobjProcess.setAt(1, null);
			lobjProcess.setAt(4, false);
			lobjProcess.SaveToDb(pdb);

			larrContacts = lobjAux.GetCurrentContacts();
			if ( (larrContacts == null) || (larrContacts.length == 0) )
				mobjContactOps = null;
			else
			{
				mobjContactOps = new ContactOps();
				mobjContactOps.marrModify = new ContactData[larrContacts.length];
				for ( i = 0; i < larrContacts.length; i++ )
				{
					mobjContactOps.marrModify[i] = new ContactData();
					mobjContactOps.marrModify[i].FromObject(larrContacts[i]);
					mobjContactOps.marrModify[i].midOwnerId = midNewClient;
					larrCInfo = larrContacts[i].getCurrentInfo();
					if ( larrCInfo != null )
					{
						mobjContactOps.marrModify[i].marrInfo = new ContactInfoData[larrCInfo.length];
						for ( j = 0; j < larrCInfo.length; j++ )
						{
							mobjContactOps.marrModify[i].marrInfo[j] = new ContactInfoData();
							mobjContactOps.marrModify[i].marrInfo[j].FromObject(larrCInfo[j]);
						}
					}
				}
				mobjContactOps.marrCreate = null;
				mobjContactOps.marrDelete = null;
				mobjContactOps.RunSubOp(pdb, null);
			}

			larrDocs = lobjAux.GetCurrentDocs();
			if ( (larrDocs == null) || (larrDocs.length == 0) )
				mobjDocOps = null;
			else
			{
				mobjDocOps = new DocOps();
				mobjDocOps.marrModify = new DocumentData[larrDocs.length];
				for ( i = 0; i < larrDocs.length; i++ )
				{
					mobjDocOps.marrModify[i] = new DocumentData();
					mobjDocOps.marrModify[i].FromObject(larrContacts[i]);
					mobjDocOps.marrModify[i].midOwnerId = midNewClient;
					larrDInfo = larrDocs[i].getCurrentInfo();
					if ( larrDInfo != null )
					{
						mobjDocOps.marrModify[i].marrInfo = new DocInfoData[larrDInfo.length];
						for ( j = 0; j < larrDInfo.length; j++ )
						{
							mobjDocOps.marrModify[i].marrInfo[j] = new DocInfoData();
							mobjDocOps.marrModify[i].marrInfo[j].FromObject(larrDInfo[j]);
						}
					}
				}
				mobjDocOps.marrCreate = null;
				mobjDocOps.marrDelete = null;
				mobjDocOps.RunSubOp(pdb, null);
			}

			lrefClients.Delete(pdb, mobjSource.mid);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "Os dois clientes serão novamente separados. Os processos do cliente antigo serão repostos " +
				"e o seu histórico de operações será recuperado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi reposto o seguinte cliente:");
		lstrResult.append(pstrLineBreak);

		mobjSource.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		UUID lidProcesses;
		Client lobjAux;
		PNProcess lobjProcess;
		PNProcess lobjSubProcAux;
		int i;
		ExternResumeClient lopERC;

		try
		{
			lidProcesses = Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNProcess);

			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjSource.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjSource.mid = lobjAux.getKey();

			lobjProcess = (PNProcess)Engine.GetWorkInstance(lidProcesses, mobjSource.midProcess);
			lobjProcess.setAt(1, lobjAux.getKey());
			lobjProcess.setAt(4, true);
			lobjProcess.SaveToDb(pdb);

			if ( mobjContactOps != null )
			{
				for ( i = 0; i < mobjContactOps.marrModify.length; i++ )
					mobjContactOps.marrModify[i].mobjPrevValues.midOwnerId = lobjAux.getKey();
				mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
			}

			if ( mobjDocOps != null )
			{
				for ( i = 0; i < mobjDocOps.marrModify.length; i++ )
					mobjDocOps.marrModify[i].mobjPrevValues.midOwnerId = lobjAux.getKey();
				mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());
			}

			if ( marrSubProcIDs != null )
			{
				for ( i = 0; i < marrSubProcIDs.length; i++ )
				{
					lobjSubProcAux = (PNProcess)Engine.GetWorkInstance(lidProcesses, marrSubProcIDs[i]);
					lobjSubProcAux.SetParentProcId(lobjProcess.getKey(), pdb);
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopERC = new ExternResumeClient(lobjProcess.getKey());
		lopERC.midOtherClientProc = GetProcess().getKey();
		TriggerOp(lopERC);
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
		larrResult[0].midType = Constants.ObjID_Client;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrCreated = new UUID[1];
		larrResult[0].marrChanged = new UUID[1];
		larrResult[0].marrCreated[0] = mobjSource.mid;
		larrResult[0].marrChanged[0] = midNewClient; 
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
