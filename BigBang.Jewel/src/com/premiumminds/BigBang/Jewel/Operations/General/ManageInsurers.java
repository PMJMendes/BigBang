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
import com.premiumminds.BigBang.Jewel.Data.AccountingData;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocDataHeavy;
import com.premiumminds.BigBang.Jewel.Objects.AccountingEntry;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageInsurers
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public class CompanyData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public String mstrAcronym;
		public String mstrISPNumber;
		public String mstrMedCode;
		public String mstrFiscalNumber;
		public String mstrBankID;
		public String mstrAcctCode;
		public String mstrAddress1;
		public String mstrAddress2;
		public String mstrAddress3;
		public UUID midZipCode;
		public Boolean mbTaxRetention;
		public ContactOps mobjContactOps;
		public DocOps mobjDocOps;
		public CompanyData mobjPrevValues;
	}

	public CompanyData[] marrCreate;
	public CompanyData[] marrModify;
	public CompanyData[] marrDelete;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;
	public AccountingData[] marrAccounting;

	public ManageInsurers(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_ManageCompanies;
	}

	public String ShortDesc()
	{
		return "Gestão de Seguradoras";
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
				lstrResult.append("Foi criada 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
				if ( marrCreate[0].mobjContactOps != null )
					marrCreate[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjDocOps != null )
					marrCreate[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criadas ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Seguradora ");
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
				lstrResult.append("Foi modificada 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificadas ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Seguradora ");
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
				lstrResult.append("Foi apagada 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
				if ( marrDelete[0].mobjContactOps != null )
					marrDelete[0].mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjDocOps != null )
					marrDelete[0].mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Seguradora ");
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
			lstrResult.append("Operações sobre contactos de seguradoras:");
			lstrResult.append(pstrLineBreak);
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
		}

		if ( mobjDocOps  != null )
		{
			lstrResult.append("Operações sobre documentos de seguradoras:");
			lstrResult.append(pstrLineBreak);
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
		}

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
		{
			lstrResult.append(pstrLineBreak).append("Movimentos contabilísticos:").append(pstrLineBreak);
			for ( i = 0; i < marrAccounting.length; i++ )
			{
				marrAccounting[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
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
		Company lobjAux;
		Entity lrefCompanies;
		Contact[] larrContacts;
		Document[] larrDocs;
		int j;
		AccountingEntry lobjEntry;

		try
		{
			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(Company.I.NAME,         marrCreate[i].mstrName);
					lobjAux.setAt(Company.I.ACRONYM,      marrCreate[i].mstrAcronym);
					lobjAux.setAt(Company.I.ISPNUMBER,    marrCreate[i].mstrISPNumber);
					lobjAux.setAt(Company.I.MEDIATORCODE, marrCreate[i].mstrMedCode);
					lobjAux.setAt(Company.I.FISCALNUMBER, marrCreate[i].mstrFiscalNumber);
					lobjAux.setAt(Company.I.BANKINGID,    marrCreate[i].mstrBankID);
					lobjAux.setAt(Company.I.ADDRESS1,     marrCreate[i].mstrAddress1);
					lobjAux.setAt(Company.I.ADDRESS2,     marrCreate[i].mstrAddress2);
					lobjAux.setAt(Company.I.ADDRESS3,     marrCreate[i].mstrAddress3);
					lobjAux.setAt(Company.I.ZIPCODE,      marrCreate[i].midZipCode);
					lobjAux.setAt(Company.I.ACCTCODE,     marrCreate[i].mstrAcctCode);
					lobjAux.setAt(Company.I.TAXRETENTION, marrCreate[i].mbTaxRetention);
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
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					marrModify[i].mobjPrevValues = new CompanyData();
					marrModify[i].mobjPrevValues.mid = lobjAux.getKey();
					marrModify[i].mobjPrevValues.mstrName         = (String) lobjAux.getAt(Company.I.NAME);
					marrModify[i].mobjPrevValues.mstrAcronym      = (String) lobjAux.getAt(Company.I.ACRONYM);
					marrModify[i].mobjPrevValues.mstrISPNumber    = (String) lobjAux.getAt(Company.I.ISPNUMBER);
					marrModify[i].mobjPrevValues.mstrMedCode      = (String) lobjAux.getAt(Company.I.MEDIATORCODE);
					marrModify[i].mobjPrevValues.mstrFiscalNumber = (String) lobjAux.getAt(Company.I.FISCALNUMBER);
					marrModify[i].mobjPrevValues.mstrBankID       = (String) lobjAux.getAt(Company.I.BANKINGID);
					marrModify[i].mobjPrevValues.mstrAddress1     = (String) lobjAux.getAt(Company.I.ADDRESS1);
					marrModify[i].mobjPrevValues.mstrAddress2     = (String) lobjAux.getAt(Company.I.ADDRESS2);
					marrModify[i].mobjPrevValues.mstrAddress3     = (String) lobjAux.getAt(Company.I.ADDRESS3);
					marrModify[i].mobjPrevValues.midZipCode       = (UUID)   lobjAux.getAt(Company.I.ZIPCODE);
					marrModify[i].mobjPrevValues.mstrAcctCode     = (String) lobjAux.getAt(Company.I.ACCTCODE);
					marrModify[i].mobjPrevValues.mbTaxRetention   = (Boolean)lobjAux.getAt(Company.I.TAXRETENTION);
					marrModify[i].mobjPrevValues.mobjContactOps = null;
					marrModify[i].mobjPrevValues.mobjPrevValues = null;
					lobjAux.setAt(Company.I.NAME,         marrModify[i].mstrName);
					lobjAux.setAt(Company.I.ACRONYM,      marrModify[i].mstrAcronym);
					lobjAux.setAt(Company.I.ISPNUMBER,    marrModify[i].mstrISPNumber);
					lobjAux.setAt(Company.I.MEDIATORCODE, marrModify[i].mstrMedCode);
					lobjAux.setAt(Company.I.FISCALNUMBER, marrModify[i].mstrFiscalNumber);
					lobjAux.setAt(Company.I.BANKINGID,    marrModify[i].mstrBankID);
					lobjAux.setAt(Company.I.ADDRESS1,     marrModify[i].mstrAddress1);
					lobjAux.setAt(Company.I.ADDRESS2,     marrModify[i].mstrAddress2);
					lobjAux.setAt(Company.I.ADDRESS3,     marrModify[i].mstrAddress3);
					lobjAux.setAt(Company.I.ZIPCODE,      marrModify[i].midZipCode);
					lobjAux.setAt(Company.I.ACCTCODE,     marrModify[i].mstrAcctCode);
					lobjAux.setAt(Company.I.TAXRETENTION, marrModify[i].mbTaxRetention);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				lrefCompanies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Company));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].mid);
					marrDelete[i].mstrName         = (String) lobjAux.getAt(Company.I.NAME);
					marrDelete[i].mstrAcronym      = (String) lobjAux.getAt(Company.I.ACRONYM);
					marrDelete[i].mstrISPNumber    = (String) lobjAux.getAt(Company.I.ISPNUMBER);
					marrDelete[i].mstrMedCode      = (String) lobjAux.getAt(Company.I.MEDIATORCODE);
					marrDelete[i].mstrFiscalNumber = (String) lobjAux.getAt(Company.I.FISCALNUMBER);
					marrDelete[i].mstrBankID       = (String) lobjAux.getAt(Company.I.BANKINGID);
					marrDelete[i].mstrAddress1     = (String) lobjAux.getAt(Company.I.ADDRESS1);
					marrDelete[i].mstrAddress2     = (String) lobjAux.getAt(Company.I.ADDRESS2);
					marrDelete[i].mstrAddress3     = (String) lobjAux.getAt(Company.I.ADDRESS3);
					marrDelete[i].midZipCode       = (UUID)   lobjAux.getAt(Company.I.ZIPCODE);
					marrDelete[i].mstrAcctCode     = (String) lobjAux.getAt(Company.I.ACCTCODE);
					marrDelete[i].mbTaxRetention   = (Boolean)lobjAux.getAt(Company.I.TAXRETENTION);
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
						marrDelete[i].mobjDocOps.marrDelete2 = new DocDataHeavy[larrDocs.length];
						for ( j = 0; j < larrDocs.length; j++ )
						{
							marrDelete[i].mobjDocOps.marrDelete2[j] = new DocDataHeavy();
							marrDelete[i].mobjDocOps.marrDelete2[j].mid = larrDocs[j].getKey();
						}
						marrDelete[i].mobjDocOps.RunSubOp(pdb, null);
					}
					marrDelete[i].mobjPrevValues = null;
					lrefCompanies.Delete(pdb, marrDelete[i].mid);
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, null);

			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, null);

			if ( marrAccounting != null )
			{
				for ( i = 0; i < marrAccounting.length; i++ )
				{
					lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					marrAccounting[i].ToObject(lobjEntry);
					lobjEntry.SaveToDb(pdb);
				}
			}
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
				lstrResult.append("A seguradora criada será apagada.");
			else
				lstrResult.append("As seguradoras criadas serão apagadas.");
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
					lstrResult.append("Seguradora ");
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
				lstrResult.append("A seguradora apagada será reposta.");
			else
				lstrResult.append("As seguradoras apagadas serão repostas.");
			lstrResult.append(pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps  != null )
			mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
			lstrResult.append(pstrLineBreak).append("Serão gerados movimentos contabilísticos de compensação.").append(pstrLineBreak);

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
				lstrResult.append("Foi apagada 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
				if ( marrCreate[0].mobjContactOps != null )
					marrCreate[0].mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
				if ( marrCreate[0].mobjDocOps != null )
					marrCreate[0].mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Seguradora ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foram repostos os valores de 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos os valores de ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Seguradora ");
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
				lstrResult.append("Foi reposta 1 seguradora:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
				if ( marrDelete[0].mobjContactOps != null )
					marrDelete[0].mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
				if ( marrDelete[0].mobjDocOps != null )
					marrDelete[0].mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostas ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" seguradoras:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Seguradora ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
					if ( marrDelete[i].mobjContactOps != null )
						marrDelete[i].mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
					if ( marrDelete[i].mobjDocOps != null )
						marrDelete[i].mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( mobjContactOps != null )
		{
			lstrResult.append("Operações sobre contactos de seguradoras:");
			lstrResult.append(pstrLineBreak);
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);
		}

		if ( mobjDocOps  != null )
		{
			lstrResult.append("Operações sobre documentos de seguradoras:");
			lstrResult.append(pstrLineBreak);
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
		}

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
		{
			lstrResult.append(pstrLineBreak).append("Movimentos contabilísticos compensados:").append(pstrLineBreak);
			for ( i = 0; i < marrAccounting.length; i++ )
			{
				marrAccounting[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		int i;
		Company lobjAux;
		Entity lrefCompanies;
		AccountingEntry lobjEntry;

		try
		{
			if ( marrCreate != null )
			{
				lrefCompanies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Company));

				for ( i = 0; i < marrCreate.length; i++ )
				{
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.UndoSubOp(pdb, null);
					if ( marrCreate[i].mobjDocOps != null )
						marrCreate[i].mobjDocOps.UndoSubOp(pdb, null);
					lrefCompanies.Delete(pdb, marrCreate[i].mid);
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAux.setAt(Company.I.NAME,         marrModify[i].mobjPrevValues.mstrName);
					lobjAux.setAt(Company.I.ACRONYM,      marrModify[i].mobjPrevValues.mstrAcronym);
					lobjAux.setAt(Company.I.ISPNUMBER,    marrModify[i].mobjPrevValues.mstrISPNumber);
					lobjAux.setAt(Company.I.MEDIATORCODE, marrModify[i].mobjPrevValues.mstrMedCode);
					lobjAux.setAt(Company.I.FISCALNUMBER, marrModify[i].mobjPrevValues.mstrFiscalNumber);
					lobjAux.setAt(Company.I.BANKINGID,    marrModify[i].mobjPrevValues.mstrBankID);
					lobjAux.setAt(Company.I.ADDRESS1,     marrModify[i].mobjPrevValues.mstrAddress1);
					lobjAux.setAt(Company.I.ADDRESS2,     marrModify[i].mobjPrevValues.mstrAddress2);
					lobjAux.setAt(Company.I.ADDRESS3,     marrModify[i].mobjPrevValues.mstrAddress3);
					lobjAux.setAt(Company.I.ZIPCODE,      marrModify[i].mobjPrevValues.midZipCode);
					lobjAux.setAt(Company.I.ACCTCODE,     marrModify[i].mobjPrevValues.mstrAcctCode);
					lobjAux.setAt(Company.I.TAXRETENTION, marrModify[i].mobjPrevValues.mbTaxRetention);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(Company.I.NAME,         marrDelete[i].mstrName);
					lobjAux.setAt(Company.I.ACRONYM,      marrDelete[i].mstrAcronym);
					lobjAux.setAt(Company.I.ISPNUMBER,    marrDelete[i].mstrISPNumber);
					lobjAux.setAt(Company.I.MEDIATORCODE, marrDelete[i].mstrMedCode);
					lobjAux.setAt(Company.I.FISCALNUMBER, marrDelete[i].mstrFiscalNumber);
					lobjAux.setAt(Company.I.BANKINGID,    marrDelete[i].mstrBankID);
					lobjAux.setAt(Company.I.ADDRESS1,     marrDelete[i].mstrAddress1);
					lobjAux.setAt(Company.I.ADDRESS2,     marrDelete[i].mstrAddress2);
					lobjAux.setAt(Company.I.ADDRESS3,     marrDelete[i].mstrAddress3);
					lobjAux.setAt(Company.I.ZIPCODE,      marrDelete[i].midZipCode);
					lobjAux.setAt(Company.I.ACCTCODE,     marrDelete[i].mstrAcctCode);
					lobjAux.setAt(Company.I.TAXRETENTION, marrDelete[i].mbTaxRetention);
					lobjAux.SaveToDb(pdb);
					marrDelete[i].mid = lobjAux.getKey();
					if ( marrDelete[i].mobjContactOps != null )
						marrDelete[i].mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
					if ( marrDelete[i].mobjDocOps != null )
						marrDelete[i].mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, null);

			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, null);

			if ( marrAccounting != null )
			{
				try
				{
					for ( i = 0; i < marrAccounting.length; i++ )
					{
						if ( marrAccounting[i].mstrSign.equals("D") )
							marrAccounting[i].mstrSign = "C";
						else
							marrAccounting[i].mstrSign = "D";
						marrAccounting[i].mstrDesc = "Reversão de " + marrAccounting[i].mstrDesc;
						lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						marrAccounting[i].ToObject(lobjEntry);
						lobjEntry.SaveToDb(pdb);
					}
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}
			}
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
			larrResult[i].midType = Constants.ObjID_Company;
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

	private void Describe(StringBuilder pstrString, CompanyData pobjData, String pstrLineBreak)
	{
		ObjectBase lobjZipCode;

		pstrString.append("Nome: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
		pstrString.append("Código: ");
		pstrString.append(pobjData.mstrAcronym);
		pstrString.append(pstrLineBreak);
		pstrString.append("Número no ISP: ");
		pstrString.append(pobjData.mstrISPNumber);
		pstrString.append(pstrLineBreak);
		pstrString.append("Nosso código de mediador: ");
		pstrString.append(pobjData.mstrMedCode);
		pstrString.append(pstrLineBreak);
		pstrString.append("NIFC: ");
		pstrString.append(pobjData.mstrFiscalNumber);
		pstrString.append(pstrLineBreak);
		pstrString.append("NIB: ");
		pstrString.append(pobjData.mstrBankID);
		pstrString.append(pstrLineBreak);

		if ( pobjData.mstrAcctCode != null )
		{
			pstrString.append("Contabilidade: 72113");
			pstrString.append(pobjData.mstrAcctCode);
			pstrString.append("1 / 21111");
			pstrString.append(pobjData.mstrAcctCode.substring(0, 1));
			pstrString.append("0");
			pstrString.append(pobjData.mstrAcctCode.substring(1));
			pstrString.append(pstrLineBreak);
		}

		pstrString.append("Morada:");
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress1);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress2);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress3);
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
