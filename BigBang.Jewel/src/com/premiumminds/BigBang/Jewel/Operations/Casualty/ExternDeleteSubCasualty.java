package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocDataHeavy;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingEntitiesData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingHeadingsData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyInsurerRequestData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyItemData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingHeadings;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternResumeSubCasualty;

public class ExternDeleteSubCasualty
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midSubCasualty;
	public String mstrReason;
	private SubCasualtyData mobjData;
	private ContactOps mobjContactOps;
	private DocOps mobjDocOps;

	public ExternDeleteSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExternDeleteSubCasualty;
	}

	public String ShortDesc()
	{
		return "Eliminação de Sub-Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi eliminado o seguinte sub-sinistro:");
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
		IEntity lrefSubCasualties;
		IEntity lrefSubCasualtyItems;
		IEntity requestEntity;
		IEntity framingEntity;
		IEntity framingEntitiesEntity;
		IEntity framingHeadingEntity;
		SubCasualty lobjAux;
		Contact[] larrContacts;
		Document[] larrDocs;
		SubCasualtyItem[] larrItems;
		SubCasualtyInsurerRequest[] requests;
		SubCasualtyFraming framing;
		SubCasualtyFramingEntity[] framingEntities;
		SubCasualtyFramingHeadings framingHeadings;
		PNProcess lobjProcess;
		int i;

		try
		{
			lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualty));
			lrefSubCasualtyItems = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualtyItem));
			requestEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualtyInsurerRequest));
			framingEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualtyFraming));
			framingEntitiesEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualtyFramingEntities));
			framingHeadingEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubCasualtyFramingHeadings));

			lobjAux = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), midSubCasualty);
			mobjData = new SubCasualtyData();
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
				mobjDocOps.marrDelete2 = new DocDataHeavy[larrDocs.length];
				for ( i = 0; i < larrDocs.length; i++ )
				{
					mobjDocOps.marrDelete2[i] = new DocDataHeavy();
					mobjDocOps.marrDelete2[i].mid = larrDocs[i].getKey();
				}
				mobjDocOps.RunSubOp(pdb, null);
			}

			larrItems = lobjAux.GetCurrentItems();
			if ( larrItems == null )
				mobjData.marrItems = null;
			else
			{
				mobjData.marrItems = new SubCasualtyItemData[larrItems.length];
				for ( i = 0; i < larrItems.length; i++ )
				{
					mobjData.marrItems[i] = new SubCasualtyItemData();
					mobjData.marrItems[i].FromObject(larrItems[i]);
					mobjData.marrItems[i].mbDeleted = true;
					lrefSubCasualtyItems.Delete(pdb, larrItems[i].getKey());
				}
			}
			
			requests = lobjAux.GetCurrentInsurerRequests();
			if (requests == null) {
				mobjData.requests = null;
			} else {
				mobjData.requests = new SubCasualtyInsurerRequestData[requests.length];
				for (i = 0; i<requests.length; i++) {
					mobjData.requests[i] = new SubCasualtyInsurerRequestData();
					mobjData.requests[i].FromObject(requests[i]);
					mobjData.requests[i].isDeleted = true;
					requestEntity.Delete(pdb, requests[i].getKey());
				}
			}
			
			framing = lobjAux.GetFraming();
			if (framing == null) {
				mobjData.framing = null;
			} else {
				mobjData.framing = new SubCasualtyFramingData();
				mobjData.framing.FromObject(framing);
				mobjData.framing.isDeleted = true;
				
				// Aditional entities' deletion
				framingEntities = framing.GetCurrentFramingEntities();
				if (framingEntities == null) {
					mobjData.framing.framingEntities = null;
				} else {
					mobjData.framing.framingEntities = new SubCasualtyFramingEntitiesData[framingEntities.length];
					for (i=0; i<framingEntities.length; i++) {
						mobjData.framing.framingEntities[i] = new SubCasualtyFramingEntitiesData();
						mobjData.framing.framingEntities[i].FromObject(framingEntities[i]);
						mobjData.framing.framingEntities[i].isDeleted = true;
						framingEntitiesEntity.Delete(pdb, framingEntities[i].getKey());
					}
				}
				
				// Headings' deletion
				framingHeadings = framing.GetFramingHeadings();
				if (framingHeadings == null) {
					mobjData.framing.framingHeadings = null;
				} else {
					mobjData.framing.framingHeadings = new SubCasualtyFramingHeadingsData();
					mobjData.framing.framingHeadings.FromObject(framingHeadings);
					mobjData.framing.framingHeadings.isDeleted = true;
					framingHeadingEntity.Delete(pdb, framingHeadings.getKey());
				}
				
				framingEntity.Delete(pdb, framing.getKey());
			}

			lrefSubCasualties.Delete(pdb, mobjData.mid);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O sub-sinistro apagado será reposto. O histórico de operações será recuperado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi reposto o seguinte sub-sinistro:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		SubCasualty lobjAux;
		SubCasualtyItem lobjItem;
		SubCasualtyInsurerRequest request;
		SubCasualtyFraming framing;
		SubCasualtyFramingEntity framingEntity;
		SubCasualtyFramingHeadings framingHeadings;
		PNProcess lobjProcess;
		ExternResumeSubCasualty lopERC;
		int i;

		try
		{
			lobjAux = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjData.mid = lobjAux.getKey();

			if ( mobjData.marrItems != null )
			{
				for ( i = 0; i < mobjData.marrItems.length; i++ )
				{
					if ( mobjData.marrItems[i].mbDeleted )
					{
						lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						mobjData.marrItems[i].midSubCasualty = mobjData.mid;
						mobjData.marrItems[i].ToObject(lobjItem);
						lobjItem.SaveToDb(pdb);
						mobjData.marrItems[i].mid = lobjItem.getKey();
					}
				}
			}
			
			if (mobjData.requests != null) {
				for (i = 0; i<mobjData.requests.length; i++) {
					if (mobjData.requests[i].isDeleted) {
						request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						mobjData.requests[i].subCasualtyId = mobjData.mid;
						mobjData.requests[i].ToObject(request);
						request.SaveToDb(pdb);
						mobjData.requests[i].id = request.getKey();
					}
				}
			}
			
			if (mobjData.framing != null) {
				if (mobjData.framing.isDeleted) {
					framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.framing.subCasualtyId = mobjData.mid;
					mobjData.framing.ToObject(framing);
					framing.SaveToDb(pdb);
					mobjData.framing.id = framing.getKey();
					
					if (mobjData.framing.framingEntities != null) {
						for (i = 0; i<mobjData.framing.framingEntities.length; i++) {
							if (mobjData.framing.framingEntities[i].isDeleted) {
								framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
								mobjData.framing.framingEntities[i].framingId = mobjData.framing.id;
								mobjData.framing.framingEntities[i].ToObject(framingEntity);
								framingEntity.SaveToDb(pdb);
								mobjData.framing.framingEntities[i].id = framingEntity.getKey();
							}
						}
					}
					
					if (mobjData.framing.framingHeadings != null) {
						if (mobjData.framing.framingHeadings.isDeleted) {
							framingHeadings = SubCasualtyFramingHeadings.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.framing.framingHeadings.framingId = mobjData.framing.id;
							mobjData.framing.framingHeadings.ToObject(framingHeadings);
							framingHeadings.SaveToDb(pdb);
							mobjData.framing.framingHeadings.id = framingHeadings.getKey();
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

		lopERC = new ExternResumeSubCasualty(lobjProcess.getKey());
		TriggerOp(lopERC, pdb);
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
		larrResult[0].midType = Constants.ObjID_SubCasualty;
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
