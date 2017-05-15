package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingEntitiesData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyInsurerRequestData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyItemData;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageData
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public SubCasualtyData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public ManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_ManageData;
	}

	public String ShortDesc()
	{
		return "Alteração de Dados";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Novos dados do sub-sinistro:");
			lstrResult.append(pstrLineBreak);
			mobjData.Describe(lstrResult, pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

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
		SubCasualty lobjAux;
		SubCasualtyItem lobjItem;
		SubCasualtyInsurerRequest request;
		SubCasualtyFraming framing;
		SubCasualtyFramingEntity framingEntity;
		UUID lidOwner;
		int i;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				lobjAux = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues = new SubCasualtyData();
				mobjData.mobjPrevValues.FromObject(lobjAux);

				mobjData.midManager = GetProcess().GetManagerID();
				mobjData.midReviewer = mobjData.mobjPrevValues.midReviewer;
				mobjData.mdtReviewDate = mobjData.mobjPrevValues.mdtReviewDate;
				mobjData.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);

				if ( mobjData.marrItems != null )
				{
					for ( i = 0; i < mobjData.marrItems.length; i++ )
					{
						if ( mobjData.marrItems[i].mbDeleted )
						{
							if ( mobjData.marrItems[i].mid == null )
								continue;
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrItems[i].mid);
							mobjData.marrItems[i].FromObject(lobjItem);
							lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
						}
						else if ( mobjData.marrItems[i].mbNew )
						{
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrItems[i].midSubCasualty = mobjData.mid;
							mobjData.marrItems[i].ToObject(lobjItem);
							lobjItem.SaveToDb(pdb);
							mobjData.marrItems[i].mid = lobjItem.getKey();
						}
						else
						{
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrItems[i].mid);
							mobjData.marrItems[i].mobjPrevValues = new SubCasualtyItemData();
							mobjData.marrItems[i].mobjPrevValues.FromObject(lobjItem);
							mobjData.marrItems[i].ToObject(lobjItem);
							lobjItem.SaveToDb(pdb);
						}
					}
				}
				
				// Insurer Requests' management
				if ( mobjData.requests != null ) {
					for ( i = 0; i < mobjData.requests.length; i++ ) {
						if ( mobjData.requests[i].isDeleted ) {
							if ( mobjData.requests[i].id == null ) {
								continue;
							}
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.requests[i].id);
							mobjData.requests[i].FromObject(request);
							request.getDefinition().Delete(pdb, request.getKey());
						} else if ( mobjData.requests[i].isNew ) {
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.requests[i].subCasualtyId = mobjData.mid;
							mobjData.requests[i].ToObject(request);
							request.SaveToDb(pdb);
							mobjData.requests[i].id = request.getKey();
						}
						else {
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.requests[i].id);
							mobjData.requests[i].prevValues = new SubCasualtyInsurerRequestData();
							mobjData.requests[i].prevValues.FromObject(request);
							mobjData.requests[i].ToObject(request);
							request.SaveToDb(pdb);
						}
					}
				}
			}
			
			if (mobjData.framing != null) {
				if ( mobjData.framing.isDeleted ) {
					framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.id);
					mobjData.framing.FromObject(framing);
					framing.getDefinition().Delete(pdb, framing.getKey());
				} else if ( mobjData.framing.isNew ) {
					framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.framing.subCasualtyId = mobjData.mid;
					mobjData.framing.ToObject(framing);
					framing.SaveToDb(pdb);
					mobjData.framing.id = framing.getKey();
				} else {
					framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.id);
					mobjData.framing.subCasualtyId = mobjData.mid;
					mobjData.framing.prevValues = new SubCasualtyFramingData();
					mobjData.framing.prevValues.FromObject(framing);
					mobjData.framing.ToObject(framing);
					framing.SaveToDb(pdb);
				}
				
				// Framing Entities' management
				if ( mobjData.framing.framingEntities != null ) {
					for ( i = 0; i < mobjData.framing.framingEntities.length; i++ ) {
						if ( mobjData.framing.framingEntities[i].isDeleted ) {
							if ( mobjData.framing.framingEntities[i].id == null ) {
								continue;
							}
							framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.framingEntities[i].id);
							mobjData.framing.framingEntities[i].FromObject(framingEntity);
							framingEntity.getDefinition().Delete(pdb, framingEntity.getKey());
						} else if ( mobjData.framing.framingEntities[i].isNew ) {
							framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.framing.framingEntities[i].framingId = mobjData.framing.id;
							mobjData.framing.framingEntities[i].ToObject(framingEntity);
							framingEntity.SaveToDb(pdb);
							mobjData.framing.framingEntities[i].id = framingEntity.getKey();
						}
						else {
							framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.framingEntities[i].id);
							mobjData.framing.framingEntities[i].prevValues = new SubCasualtyFramingEntitiesData();
							mobjData.framing.framingEntities[i].prevValues.FromObject(framingEntity);
							mobjData.framing.framingEntities[i].ToObject(framingEntity);
							framingEntity.SaveToDb(pdb);
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lidOwner);
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lidOwner);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores serão repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores foram repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		SubCasualty lobjAux;
		SubCasualtyItem lobjItem;
		SubCasualtyInsurerRequest request;
		SubCasualtyFraming framing;
		SubCasualtyFramingEntity framingEntity;
		UUID lidOwner;
		int i;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				lobjAux = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);

				if ( mobjData.marrItems != null )
				{
					for ( i = 0; i < mobjData.marrItems.length; i++ )
					{
						if ( mobjData.marrItems[i].mbDeleted )
						{
							if ( mobjData.marrItems[i].mid == null )
								continue;
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrItems[i].ToObject(lobjItem);
							lobjItem.SaveToDb(pdb);
						}
						else if ( mobjData.marrItems[i].mbNew )
						{
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrItems[i].mid);
							lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
						}
						else
						{
							lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrItems[i].mid);
							mobjData.marrItems[i].mobjPrevValues.ToObject(lobjItem);
							lobjItem.SaveToDb(pdb);
						}
					}
				}
				
				if (mobjData.requests != null) {
					for (i = 0; i < mobjData.requests.length; i++) {
						if (mobjData.requests[i].isDeleted) {
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.requests[i].ToObject(request);
							request.SaveToDb(pdb);
						} else if (mobjData.requests[i].isNew) {
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.requests[i].id);
							request.getDefinition().Delete(pdb, request.getKey());
						} else {
							request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.requests[i].id);
							mobjData.requests[i].prevValues.ToObject(request);
							request.SaveToDb(pdb);
						}
					}
				}
				
				if (mobjData.framing != null) {
					if (mobjData.framing.isDeleted) {
						framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						mobjData.framing.ToObject(framing);
						framing.SaveToDb(pdb);
					} else if (mobjData.framing.isNew) {
						framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.id);
						framing.getDefinition().Delete(pdb, framing.getKey());
					} else {
						framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.id);
						mobjData.framing.prevValues.ToObject(framing);
						framing.SaveToDb(pdb);
					}
					
					// Framing Entities' management
					if ( mobjData.framing.framingEntities != null ) {
						for ( i = 0; i < mobjData.framing.framingEntities.length; i++ ) {
							if ( mobjData.framing.framingEntities[i].isDeleted ) {
								framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
								mobjData.framing.framingEntities[i].ToObject(framingEntity);
								framingEntity.SaveToDb(pdb);
							} else if ( mobjData.requests[i].isNew ) {
								framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.framingEntities[i].id);
								framingEntity.getDefinition().Delete(pdb, framingEntity.getKey());
							}
							else {
								framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), mobjData.framing.framingEntities[i].id);
								mobjData.framing.framingEntities[i].prevValues.ToObject(framingEntity);
								framingEntity.SaveToDb(pdb);
							}
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, lidOwner);
			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, lidOwner);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
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
		if ( mobjData != null )
			llngSize++;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( mobjData != null )
		{
			larrResult[0] = new UndoSet();
			larrResult[0].midType = Constants.ObjID_SubCasualty;
			larrResult[0].marrDeleted = new UUID[0];
			larrResult[0].marrChanged = new UUID[1];
			larrResult[0].marrChanged[0] = mobjData.mid;
			larrResult[0].marrCreated = new UUID[0];
			i++;
		}

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
