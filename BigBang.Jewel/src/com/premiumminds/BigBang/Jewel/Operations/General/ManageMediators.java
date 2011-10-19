package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageMediators
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public class MediatorData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public String mstrISPNumber;
		public String mstrFiscalNumber;
		public String mstrBankID;
		public UUID midProfile;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
		public ContactOps mobjContactOps;
		public DocOps mobjDocOps;
		public MediatorData mobjPrevValues;
	}

	public MediatorData[] marrCreate;
	public MediatorData[] marrModify;
	public MediatorData[] marrDelete;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public ManageMediators(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageMediators;
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
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
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
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
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
				Describe(lstrResult, marrModify[0], pstrLineBreak);
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
					Describe(lstrResult, marrModify[i], pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi apagado 1 mediador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
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
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
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
		Mediator lobjAux;
		Entity lrefMediators;
		Contact[] larrContacts;
		Document[] larrDocs;
		int j;

		try
		{
			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrName);
					lobjAux.setAt(1, marrCreate[i].mstrISPNumber);
					lobjAux.setAt(2, marrCreate[i].mstrFiscalNumber);
					lobjAux.setAt(3, marrCreate[i].mstrBankID);
					lobjAux.setAt(4, marrCreate[i].midProfile);
					lobjAux.setAt(5, marrCreate[i].mstrAddress1);
					lobjAux.setAt(6, marrCreate[i].mstrAddress2);
					lobjAux.setAt(7, marrCreate[i].midZipCode);
					lobjAux.SaveToDb(pdb);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.RunSubOp(pdb, lobjAux.getKey());
					marrCreate[i].mid = lobjAux.getKey();
					marrCreate[i].mobjPrevValues = null;
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					marrModify[i].mobjPrevValues = new MediatorData();
					marrModify[i].mobjPrevValues.mid = lobjAux.getKey();
					marrModify[i].mobjPrevValues.mstrName = (String)lobjAux.getAt(0);
					marrModify[i].mobjPrevValues.mstrISPNumber = (String)lobjAux.getAt(1);
					marrModify[i].mobjPrevValues.mstrFiscalNumber = (String)lobjAux.getAt(2);
					marrModify[i].mobjPrevValues.mstrBankID = (String)lobjAux.getAt(3);
					marrModify[i].mobjPrevValues.midProfile = (UUID)lobjAux.getAt(4);
					marrModify[i].mobjPrevValues.mstrAddress1 = (String)lobjAux.getAt(5);
					marrModify[i].mobjPrevValues.mstrAddress2 = (String)lobjAux.getAt(6);
					marrModify[i].mobjPrevValues.midZipCode = (UUID)lobjAux.getAt(7);
					marrModify[i].mobjContactOps = null;
					marrModify[i].mobjPrevValues.mobjPrevValues = null;
					lobjAux.setAt(0, marrModify[i].mstrName);
					lobjAux.setAt(1, marrModify[i].mstrISPNumber);
					lobjAux.setAt(2, marrModify[i].mstrFiscalNumber);
					lobjAux.setAt(3, marrModify[i].mstrBankID);
					lobjAux.setAt(4, marrModify[i].midProfile);
					lobjAux.setAt(5, marrModify[i].mstrAddress1);
					lobjAux.setAt(6, marrModify[i].mstrAddress2);
					lobjAux.setAt(7, marrModify[i].midZipCode);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				lrefMediators = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Mediator));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].mid);
					marrDelete[i].mstrName = (String)lobjAux.getAt(0);
					marrDelete[i].mstrISPNumber = (String)lobjAux.getAt(1);
					marrDelete[i].mstrFiscalNumber = (String)lobjAux.getAt(2);
					marrDelete[i].mstrBankID = (String)lobjAux.getAt(3);
					marrDelete[i].midProfile = (UUID)lobjAux.getAt(4);
					marrDelete[i].mstrAddress1 = (String)lobjAux.getAt(5);
					marrDelete[i].mstrAddress2 = (String)lobjAux.getAt(6);
					marrDelete[i].midZipCode = (UUID)lobjAux.getAt(7);
					larrContacts = lobjAux.GetCurrentContacts();
					if ( (larrContacts == null) || (larrContacts.length == 0) )
						marrDelete[i].mobjContactOps = null;
					else
					{
						marrDelete[i].mobjContactOps = new ContactOps();
						marrDelete[i].mobjContactOps.marrDelete = new ContactData[larrContacts.length];
						for ( j = 0; j < larrContacts.length; j++ )
						{
							marrDelete[i].mobjContactOps.marrDelete[j] = new ContactData();
							marrDelete[i].mobjContactOps.marrDelete[j].mid = larrContacts[j].getKey();
						}
						marrDelete[i].mobjContactOps.RunSubOp(pdb, null);
					}
					larrDocs = lobjAux.GetCurrentDocs();
					if ( (larrDocs == null) || (larrDocs.length == 0) )
						marrDelete[i].mobjDocOps = null;
					else
					{
						marrDelete[i].mobjDocOps = new DocOps();
						marrDelete[i].mobjDocOps.marrDelete = new DocumentData[larrDocs.length];
						for ( j = 0; j < larrDocs.length; j++ )
						{
							marrDelete[i].mobjDocOps.marrDelete[j] = new DocumentData();
							marrDelete[i].mobjDocOps.marrDelete[j].mid = larrDocs[j].getKey();
						}
						marrDelete[i].mobjDocOps.RunSubOp(pdb, null);
					}
					marrDelete[i].mobjPrevValues = null;
					lrefMediators.Delete(pdb, marrDelete[i].mid);
				}
			}

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
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Mediador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i].mobjPrevValues, pstrLineBreak);
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
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
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
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
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
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
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
					Describe(lstrResult, marrModify[i].mobjPrevValues, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi reposto 1 mediador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
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
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
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
		Mediator lobjAux;
		Entity lrefMediators;

		try
		{
			if ( marrCreate != null )
			{
				lrefMediators = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Mediator));

				for ( i = 0; i < marrCreate.length; i++ )
				{
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.UndoSubOp(pdb, null);
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.UndoSubOp(pdb, null);
					lrefMediators.Delete(pdb, marrCreate[i].mid);
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAux.setAt(0, marrModify[i].mobjPrevValues.mstrName);
					lobjAux.setAt(1, marrModify[i].mobjPrevValues.mstrISPNumber);
					lobjAux.setAt(2, marrModify[i].mobjPrevValues.mstrFiscalNumber);
					lobjAux.setAt(3, marrModify[i].mobjPrevValues.mstrBankID);
					lobjAux.setAt(4, marrModify[i].mobjPrevValues.midProfile);
					lobjAux.setAt(5, marrModify[i].mobjPrevValues.mstrAddress1);
					lobjAux.setAt(6, marrModify[i].mobjPrevValues.mstrAddress2);
					lobjAux.setAt(7, marrModify[i].mobjPrevValues.midZipCode);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrName);
					lobjAux.setAt(1, marrCreate[i].mstrISPNumber);
					lobjAux.setAt(2, marrCreate[i].mstrFiscalNumber);
					lobjAux.setAt(3, marrCreate[i].mstrBankID);
					lobjAux.setAt(4, marrCreate[i].midProfile);
					lobjAux.setAt(5, marrCreate[i].mstrAddress1);
					lobjAux.setAt(6, marrCreate[i].mstrAddress2);
					lobjAux.setAt(7, marrCreate[i].midZipCode);
					lobjAux.SaveToDb(pdb);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());
				}
			}

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
				larrResult[i].marrDeleted[j] = marrCreate[j].mid;

			for ( j = 0; j < llngModifies; j ++ )
				larrResult[i].marrChanged[j] = marrModify[j].mid;

			for ( j = 0; j < llngDeletes; j ++ )
				larrResult[i].marrCreated[j] = marrDelete[j].mid;

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

	private void Describe(StringBuilder pstrString, MediatorData pobjData, String pstrLineBreak)
	{
		ObjectBase lobjProfile;
		ObjectBase lobjZipCode;

		pstrString.append("Nome: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
		pstrString.append("Número no ISP: ");
		pstrString.append(pobjData.mstrISPNumber);
		pstrString.append(pstrLineBreak);
		pstrString.append("NIFC: ");
		pstrString.append(pobjData.mstrFiscalNumber);
		pstrString.append(pstrLineBreak);
		pstrString.append("NIB: ");
		pstrString.append(pobjData.mstrBankID);
		pstrString.append(pstrLineBreak);
		pstrString.append("Perfil de Comissionamento: ");

		try
		{
			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CommProfile), pobjData.midProfile);
			pstrString.append((String)lobjProfile.getAt(0));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o perfil de comissionamento.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Morada:");
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress1);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress2);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");

		try
		{
			lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode), pobjData.midZipCode);
			pstrString.append((String)lobjZipCode.getAt(0));
			pstrString.append(" ");
			pstrString.append((String)lobjZipCode.getAt(1));
			pstrString.append(pstrLineBreak);
			pstrString.append("- ");
        	pstrString.append((String)lobjZipCode.getAt(4));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o código postal.)");
		}
		pstrString.append(pstrLineBreak);
	}
}
