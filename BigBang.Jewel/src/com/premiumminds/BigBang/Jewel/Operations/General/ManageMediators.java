package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.MediatorData;
import com.premiumminds.BigBang.Jewel.Data.MediatorDealData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.MediatorDeal;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageMediators
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public static class MediatorOpData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public MediatorData mobjMainValues;
		public MediatorDealData[] marrDataCreate;
		public MediatorDealData[] marrDataModify;
		public MediatorDealData[] marrDataDelete;
		public ContactOps mobjContactOps;
		public DocOps mobjDocOps;
	}

	public MediatorOpData[] marrCreate;
	public MediatorOpData[] marrModify;
	public MediatorOpData[] marrDelete;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public ManageMediators(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_ManageMediators;
	}

	public String ShortDesc()
	{
		return "Gestão de Mediadores"; 
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi criado 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrCreate[0].mobjMainValues.Describe(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjContactOps != null )
					marrCreate[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjDocOps != null )
					marrCreate[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrCreate[i].mobjMainValues.Describe(lstrResult, pstrLineBreak);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foi modificado 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrModify[0].mobjMainValues.Describe(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrModify[i].mobjMainValues.Describe(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi apagado 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrDelete[0].mobjMainValues.Describe(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjContactOps != null )
					marrDelete[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjDocOps != null )
					marrDelete[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrDelete[i].mobjMainValues.Describe(lstrResult, pstrLineBreak);
					if ( marrDelete[i].mobjContactOps != null )
						marrDelete[i].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
					if ( marrDelete[i].mobjDocOps != null )
						marrDelete[i].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( mobjContactOps != null )
		{
			lstrResult.append("Operações sobre contactos de mediadores:");
			lstrResult.append(pstrLineBreak);
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
		}

		if ( mobjDocOps  != null )
		{
			lstrResult.append("Operações sobre documentos de mediadores:");
			lstrResult.append(pstrLineBreak);
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		int i;

		try
		{
			if ( marrCreate != null )
				for ( i = 0; i < marrCreate.length; i++ )
					CreateMediator(pdb, marrCreate[i]);

			if ( marrModify != null )
				for ( i = 0; i < marrModify.length; i++ )
					ModifyMediator(pdb, marrModify[i]);

			if ( marrDelete != null )
				for ( i = 0; i < marrDelete.length; i++ )
					DeleteMediator(pdb, marrDelete[i]);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, null);

			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, null);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				lstrResult.append("O mediador criado será apagado.");
			else
				lstrResult.append("Os mediadores criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				marrModify[0].mobjMainValues.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrModify[i].mobjMainValues.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
				lstrResult.append("O mediador apagado será reposto.");
			else
				lstrResult.append("Os mediadores apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps  != null )
			mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi apagado 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrCreate[0].mobjMainValues.Describe(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjContactOps != null )
					marrCreate[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjDocOps != null )
					marrCreate[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrCreate[i].mobjMainValues.Describe(lstrResult, pstrLineBreak);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foram repostos os valores de 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrModify[0].mobjMainValues.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos os valores de ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrModify[i].mobjMainValues.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi reposto 1 mediador:");
				lstrResult.append(pstrLineBreak);
				marrDelete[0].mobjMainValues.Describe(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjContactOps != null )
					marrDelete[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjDocOps != null )
					marrDelete[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" mediadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					marrDelete[i].mobjMainValues.Describe(lstrResult, pstrLineBreak);
					if ( marrDelete[i].mobjContactOps != null )
						marrDelete[i].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
					if ( marrDelete[i].mobjDocOps != null )
						marrDelete[i].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( mobjContactOps != null )
		{
			lstrResult.append("Operações sobre contactos de mediadores:");
			lstrResult.append(pstrLineBreak);
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
		}

		if ( mobjDocOps  != null )
		{
			lstrResult.append("Operações sobre documentos de mediadores:");
			lstrResult.append(pstrLineBreak);
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		int i;

		try
		{
			if ( marrCreate != null )
				for ( i = 0; i < marrCreate.length; i++ )
					UndoCreateMediator(pdb, marrCreate[i]);

			if ( marrModify != null )
				for ( i = 0; i < marrModify.length; i++ )
					UndoModifyMediator(pdb, marrModify[i]);

			if ( marrDelete != null )
				for ( i = 0; i < marrDelete.length; i++ )
					UndoDeleteMediator(pdb, marrDelete[i]);

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, null);

			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, null);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		int llngCreates, llngModifies, llngDeletes;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i, j;

		llngCreates = ( marrCreate == null ? 0 : marrCreate.length );
		llngModifies = ( marrModify == null ? 0 : marrModify.length );
		llngDeletes = ( marrDelete == null ? 0 : marrDelete.length );
		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 0;
		if ( llngCreates + llngModifies + llngDeletes > 0 )
			llngSize++;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;
		if ( llngSize == 0)
			return new UndoSet[0];

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( llngCreates + llngModifies + llngDeletes > 0 )
		{
			larrResult[i] = new UndoSet();
			larrResult[i].midType = Constants.ObjID_Mediator;
			larrResult[i].marrDeleted = new UUID[llngCreates];
			larrResult[i].marrChanged = new UUID[llngModifies];
			larrResult[i].marrCreated = new UUID[llngDeletes];

			for ( j = 0; j < llngCreates; j ++ )
				larrResult[i].marrDeleted[j] = marrCreate[j].mobjMainValues.mid;

			for ( j = 0; j < llngModifies; j ++ )
				larrResult[i].marrChanged[j] = marrModify[j].mobjMainValues.mid;

			for ( j = 0; j < llngDeletes; j ++ )
				larrResult[i].marrCreated[j] = marrDelete[j].mobjMainValues.mid;

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

	private void CreateMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		Mediator lobjAux;
		int i;

		try
		{
			lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			pobjData.mobjMainValues.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			pobjData.marrDataModify = null;
			pobjData.marrDataDelete = null;
			if ( (pobjData.mobjMainValues.marrDeals == null) || (pobjData.mobjMainValues.marrDeals.length == 0) )
				pobjData.marrDataCreate = null;
			else
			{
				pobjData.marrDataCreate = new MediatorDealData[pobjData.mobjMainValues.marrDeals.length];

				for ( i = 0; i < pobjData.marrDataCreate.length; i++ )
				{
					pobjData.marrDataCreate[i] = pobjData.mobjMainValues.marrDeals[i];
					pobjData.marrDataCreate[i].midMediator = lobjAux.getKey();
				}
			}
			RunDataOps(pdb, pobjData);

			if ( pobjData.mobjContactOps != null )
				pobjData.mobjContactOps.RunSubOp(pdb, lobjAux.getKey());

			if ( pobjData.mobjDocOps != null )
				pobjData.mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			pobjData.mobjMainValues.mid = lobjAux.getKey();
			pobjData.mobjMainValues.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void ModifyMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		Mediator lobjAux;
		MediatorDeal[] larrCurrent;
		HashMap<UUID, MediatorDealData> larrDeals;
		ArrayList<MediatorDealData> larrCreate;
		ArrayList<MediatorDealData> larrModify;
		ArrayList<MediatorDealData> larrDelete;
		MediatorDealData lobjData;
		int i;

		try
		{
			lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), pobjData.mobjMainValues.mid);
			pobjData.mobjMainValues.mobjPrevValues = new MediatorData();
			pobjData.mobjMainValues.mobjPrevValues.FromObject(lobjAux);
			pobjData.mobjMainValues.mobjPrevValues.mobjPrevValues = null;
			pobjData.mobjContactOps = null;
			pobjData.mobjDocOps = null;
			pobjData.mobjMainValues.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			larrCurrent = lobjAux.GetCurrentDeals();
			if ( pobjData.mobjMainValues.marrDeals == null )
			{
				pobjData.marrDataCreate = null;
				pobjData.marrDataModify = null;
				if ( (larrCurrent == null) || (larrCurrent.length == 0) )
					pobjData.marrDataDelete = null;
				else
				{
					pobjData.marrDataDelete = new MediatorDealData[larrCurrent.length];
					for ( i = 0; i < larrCurrent.length; i++ )
					{
						pobjData.marrDataDelete[i] = new MediatorDealData();
						pobjData.marrDataDelete[i].mid = larrCurrent[i].getKey();
					}
				}
			}
			else
			{
				larrDeals = new HashMap<UUID, MediatorDealData>();
				larrCreate = new ArrayList<MediatorDealData>();
				larrModify = new ArrayList<MediatorDealData>();
				larrDelete = new ArrayList<MediatorDealData>();

				for ( i = 0; i < larrCurrent.length; i++ )
				{
					lobjData = new MediatorDealData();
					lobjData.FromObject(larrCurrent[i]);
					larrDeals.put(lobjData.midSubLine, lobjData);
				}

				for ( i = 0; i < pobjData.mobjMainValues.marrDeals.length; i++ )
				{
					lobjData = larrDeals.get(pobjData.mobjMainValues.marrDeals[i].midSubLine);

					if ( lobjData == null )
					{
						pobjData.mobjMainValues.marrDeals[i].midMediator = lobjAux.getKey();
						larrCreate.add(pobjData.mobjMainValues.marrDeals[i]);
					}
					else
					{
						if ( !lobjData.mdblPercent.equals(pobjData.mobjMainValues.marrDeals[i].mdblPercent) )
							larrModify.add(pobjData.mobjMainValues.marrDeals[i]);
						larrDeals.put(pobjData.mobjMainValues.marrDeals[i].midSubLine, null);
					}
				}

				for ( UUID lid: larrDeals.keySet() )
				{
					lobjData = larrDeals.get(lid);
					if ( lobjData != null )
						larrDelete.add(lobjData);
				}

				if ( larrCreate.size() == 0 )
					pobjData.marrDataCreate = null;
				else
					pobjData.marrDataCreate = larrCreate.toArray(new MediatorDealData[larrCreate.size()]);
				if ( larrModify.size() == 0 )
					pobjData.marrDataModify = null;
				else
					pobjData.marrDataModify = larrModify.toArray(new MediatorDealData[larrModify.size()]);
				if ( larrDelete.size() == 0 )
					pobjData.marrDataDelete = null;
				else
					pobjData.marrDataDelete = larrDelete.toArray(new MediatorDealData[larrDelete.size()]);
			}
			RunDataOps(pdb, pobjData);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void DeleteMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		Mediator lobjAux;
		MediatorDeal[] larrDeals;
		Contact[] larrContacts;
		Document[] larrDocs;
		int i;

		try
		{
			lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), pobjData.mobjMainValues.mid);
			pobjData.mobjMainValues.FromObject(lobjAux);

			pobjData.marrDataCreate = null;
			pobjData.marrDataModify = null;
			larrDeals = lobjAux.GetCurrentDeals();
			if ( (larrDeals == null) || (larrDeals.length == 0) )
				pobjData.marrDataDelete = null;
			else
			{
				pobjData.marrDataDelete = new MediatorDealData[larrDeals.length];
				for ( i = 0; i < larrDeals.length; i++ )
				{
					pobjData.marrDataDelete[i] = new MediatorDealData();
					pobjData.marrDataDelete[i].mid = larrDeals[i].getKey();
				}
			}
			RunDataOps(pdb, pobjData);

			larrContacts = lobjAux.GetCurrentContacts();
			if ( (larrContacts == null) || (larrContacts.length == 0) )
				pobjData.mobjContactOps = null;
			else
			{
				pobjData.mobjContactOps = new ContactOps();
				pobjData.mobjContactOps.marrDelete = new ContactData[larrContacts.length];
				for ( i = 0; i < larrContacts.length; i++ )
				{
					pobjData.mobjContactOps.marrDelete[i] = new ContactData();
					pobjData.mobjContactOps.marrDelete[i].mid = larrContacts[i].getKey();
				}
				pobjData.mobjContactOps.RunSubOp(pdb, null);
			}

			larrDocs = lobjAux.GetCurrentDocs();
			if ( (larrDocs == null) || (larrDocs.length == 0) )
				pobjData.mobjDocOps = null;
			else
			{
				pobjData.mobjDocOps = new DocOps();
				pobjData.mobjDocOps.marrDelete = new DocumentData[larrDocs.length];
				for ( i = 0; i < larrDocs.length; i++ )
				{
					pobjData.mobjDocOps.marrDelete[i] = new DocumentData();
					pobjData.mobjDocOps.marrDelete[i].mid = larrDocs[i].getKey();
				}
				pobjData.mobjDocOps.RunSubOp(pdb, null);
			}

			pobjData.mobjMainValues.mobjPrevValues = null;
			lobjAux.getDefinition().Delete(pdb, pobjData.mobjMainValues.mid);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoCreateMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		try
		{
			if ( pobjData.mobjContactOps != null )
				pobjData.mobjContactOps.UndoSubOp(pdb, null);

			if ( pobjData.mobjDocOps != null )
				pobjData.mobjDocOps.UndoSubOp(pdb, null);

			UndoDataOps(pdb, pobjData);

			Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Mediator)).Delete(pdb, pobjData.mobjMainValues.mid);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoModifyMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		Mediator lobjAux;

		try
		{
			lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), pobjData.mobjMainValues.mid);
			pobjData.mobjMainValues.mobjPrevValues.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			UndoDataOps(pdb, pobjData);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoDeleteMediator(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		Mediator lobjAux;
		int i;

		try
		{
			lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			pobjData.mobjMainValues.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( pobjData.marrDataDelete != null )
				for ( i = 0; i < pobjData.marrDataDelete.length; i++ )
					pobjData.marrDataDelete[i].midMediator = lobjAux.getKey();
			UndoDataOps(pdb, pobjData);

			if ( pobjData.mobjContactOps != null )
				pobjData.mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());

			if ( pobjData.mobjDocOps != null )
				pobjData.mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void RunDataOps(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		int i;

		if ( pobjData.marrDataCreate != null )
			for ( i = 0; i < pobjData.marrDataCreate.length; i++ )
				CreateDeal(pdb, pobjData.marrDataCreate[i]);

		if ( pobjData.marrDataModify != null )
			for ( i = 0; i < pobjData.marrDataModify.length; i++ )
				ModifyDeal(pdb, pobjData.marrDataModify[i]);

		if ( pobjData.marrDataDelete != null )
			for ( i = 0; i < pobjData.marrDataDelete.length; i++ )
				DeleteDeal(pdb, pobjData.marrDataDelete[i]);
	}

	private void UndoDataOps(SQLServer pdb, MediatorOpData pobjData)
		throws BigBangJewelException
	{
		int i;

		if ( pobjData.marrDataCreate != null )
			for ( i = 0; i < pobjData.marrDataCreate.length; i++ )
				UndoCreateDeal(pdb, pobjData.marrDataCreate[i]);

		if ( pobjData.marrDataModify != null )
			for ( i = 0; i < pobjData.marrDataModify.length; i++ )
				UndoModifyDeal(pdb, pobjData.marrDataModify[i]);

		if ( pobjData.marrDataDelete != null )
			for ( i = 0; i < pobjData.marrDataDelete.length; i++ )
				UndoDeleteDeal(pdb, pobjData.marrDataDelete[i]);
	}

	private void CreateDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		MediatorDeal lobjAux;

		try
		{
			lobjAux = MediatorDeal.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			pobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			pobjData.mid = lobjAux.getKey();
			pobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void ModifyDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		MediatorDeal lobjAux;

		try
		{
			lobjAux = MediatorDeal.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);
			pobjData.mobjPrevValues = new MediatorDealData();
			pobjData.mobjPrevValues.FromObject(lobjAux);
			pobjData.mobjPrevValues.mobjPrevValues = null;
			pobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void DeleteDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		MediatorDeal lobjAux;

		try
		{
			lobjAux = MediatorDeal.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);
			pobjData.FromObject(lobjAux);
			pobjData.mobjPrevValues = null;
			lobjAux.getDefinition().Delete(pdb, pobjData.mid);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoCreateDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		try
		{
			Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_MediatorDeal)).Delete(pdb, pobjData.mid);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoModifyDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		MediatorDeal lobjAux;

		try
		{
			lobjAux = MediatorDeal.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);
			pobjData.mobjPrevValues.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoDeleteDeal(SQLServer pdb, MediatorDealData pobjData)
		throws BigBangJewelException
	{
		MediatorDeal lobjAux;

		try
		{
			lobjAux = MediatorDeal.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			pobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
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

		if ( marrCreate != null )
		{
			for ( i = 0; i < marrCreate.length; i++ )
			{
				if ( marrCreate[i].mobjContactOps != null )
				{
					larrAux = marrCreate[i].mobjContactOps.GetSubSet();
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
			}
		}

		if ( marrModify != null )
		{
			for ( i = 0; i < marrModify.length; i++ )
			{
				if ( marrModify[i].mobjContactOps != null )
				{
					larrAux = marrModify[i].mobjContactOps.GetSubSet();
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
			}
		}

		if ( marrDelete != null )
		{
			for ( i = 0; i < marrDelete.length; i++ )
			{
				if ( marrDelete[i].mobjContactOps != null )
				{
					larrAux = marrDelete[i].mobjContactOps.GetSubSet();
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
			}
		}

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

		if ( marrCreate != null )
		{
			for ( i = 0; i < marrCreate.length; i++ )
			{
				if ( marrCreate[i].mobjDocOps != null )
				{
					larrAux = marrCreate[i].mobjDocOps.GetSubSet();
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
			}
		}

		if ( marrModify != null )
		{
			for ( i = 0; i < marrModify.length; i++ )
			{
				if ( marrModify[i].mobjDocOps != null )
				{
					larrAux = marrModify[i].mobjDocOps.GetSubSet();
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
			}
		}

		if ( marrDelete != null )
		{
			for ( i = 0; i < marrDelete.length; i++ )
			{
				if ( marrDelete[i].mobjDocOps != null )
				{
					larrAux = marrDelete[i].mobjDocOps.GetSubSet();
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
			}
		}

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
