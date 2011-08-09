package bigBang.module.generalSystemModule.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.EngineImplementor;
import bigBang.library.server.FileServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyService;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.ZipCodeBridge;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsuranceCompanies;

public class InsuranceAgencyServiceImpl
	extends EngineImplementor
	implements InsuranceAgencyService
{

	private static final long serialVersionUID = 1L;

	public InsuranceAgency[] getInsuranceAgencies()
		throws SessionExpiredException, BigBangException
	{
		UUID lidCompanies;
		UUID lidZipCodes;
        MasterDB ldb;
        ResultSet lrsCompanies;
		ArrayList<InsuranceAgency> larrAux;
		ObjectBase lobjAux, lobjZipCode;
		InsuranceAgency lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<InsuranceAgency>();

		try
		{
			lidCompanies = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Company);
			lidZipCodes = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsCompanies = Entity.GetInstance(lidCompanies).SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsCompanies.next())
	        {
	        	lobjAux = Engine.GetWorkInstance(lidCompanies, lrsCompanies);
	        	lobjZipCode = Engine.GetWorkInstance(lidZipCodes, (UUID)lobjAux.getAt(8));
	        	lobjTmp = new InsuranceAgency();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.name = (String)lobjAux.getAt(0);
	        	lobjTmp.acronym = (String)lobjAux.getAt(1);
	        	lobjTmp.ISPNumber = (String)lobjAux.getAt(2);
	        	lobjTmp.ownMediatorCodes = new String[1];
	        	lobjTmp.ownMediatorCodes[0] = (String)lobjAux.getAt(3);
	        	lobjTmp.taxNumber = (String)lobjAux.getAt(4);
	        	lobjTmp.NIB = (String)lobjAux.getAt(5);
	        	lobjTmp.address = new Address();
	        	lobjTmp.address.street1 = (String)lobjAux.getAt(6);
	        	lobjTmp.address.street2 = (String)lobjAux.getAt(7);
	        	lobjTmp.address.zipCode = new ZipCode();
	        	lobjTmp.address.zipCode.code = (String)lobjZipCode.getAt(0);
	        	lobjTmp.address.zipCode.city = (String)lobjZipCode.getAt(1);
	        	lobjTmp.address.zipCode.county = (String)lobjZipCode.getAt(2);
	        	lobjTmp.address.zipCode.district = (String)lobjZipCode.getAt(3);
	        	lobjTmp.address.zipCode.country = (String)lobjZipCode.getAt(4);
	        	larrAux.add(lobjTmp);
	        }
        }
        catch (Throwable e)
        {
			try { lrsCompanies.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsCompanies.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new InsuranceAgency[larrAux.size()]);
	}

	public InsuranceAgency createInsuranceAgency(InsuranceAgency agency)
		throws SessionExpiredException, BigBangException
	{
		ManageInsuranceCompanies lopMIC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMIC = new ManageInsuranceCompanies(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMIC.marrCreate = new ManageInsuranceCompanies.CompanyData[1];
			lopMIC.marrCreate[0] = lopMIC.new CompanyData();
			lopMIC.marrCreate[0].mid = null;
			lopMIC.marrCreate[0].mstrName = agency.name;
			lopMIC.marrCreate[0].mstrAcronym = agency.acronym;
			lopMIC.marrCreate[0].mstrISPNumber = agency.ISPNumber;
			if ( (agency.ownMediatorCodes != null) && (agency.ownMediatorCodes.length > 0) )
				lopMIC.marrCreate[0].mstrMedCode = agency.ownMediatorCodes[0];
			else
				lopMIC.marrCreate[0].mstrMedCode = null;
			lopMIC.marrCreate[0].mstrFiscalNumber = agency.taxNumber;
			lopMIC.marrCreate[0].mstrBankID = agency.NIB;
			if ( agency.address != null )
			{
				lopMIC.marrCreate[0].mstrAddress1 = agency.address.street1;
				lopMIC.marrCreate[0].mstrAddress2 = agency.address.street2;
				if ( agency.address.zipCode != null )
					lopMIC.marrCreate[0].midZipCode = ZipCodeBridge.GetZipCode(agency.address.zipCode.code,
							agency.address.zipCode.city, agency.address.zipCode.county, agency.address.zipCode.district,
							agency.address.zipCode.country);
				else
					lopMIC.marrCreate[0].midZipCode = null;
			}
			else
			{
				lopMIC.marrCreate[0].mstrAddress1 = null;
				lopMIC.marrCreate[0].mstrAddress2 = null;
				lopMIC.marrCreate[0].midZipCode = null;
			}
			if ( (agency.contacts != null) && (agency.contacts.length > 0) )
			{
				lopMIC.marrCreate[0].mobjContactOps = new ContactOps();
				lopMIC.marrCreate[0].mobjContactOps.marrCreate = BuildContactTree(lopMIC.marrCreate[0].mobjContactOps,
						agency.contacts, Constants.ObjID_Company);
			}
			else
				lopMIC.marrCreate[0].mobjContactOps = null;
			if ( (agency.documents != null) && (agency.documents.length > 0) )
			{
				lopMIC.marrCreate[0].mobjDocOps = new DocOps();
				lopMIC.marrCreate[0].mobjDocOps.marrCreate = BuildDocTree(lopMIC.marrCreate[0].mobjDocOps,
						agency.documents, Constants.ObjID_Company);
			}
			else
				lopMIC.marrCreate[0].mobjDocOps = null;
			lopMIC.marrModify = null;
			lopMIC.marrDelete = null;
			lopMIC.mobjContactOps = null;
			lopMIC.mobjDocOps = null;

			lopMIC.Execute(null);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		agency.id = lopMIC.marrCreate[0].mid.toString();
		if ( (agency.contacts != null) && (agency.contacts.length > 0) )
			WalkContactTree(lopMIC.marrCreate[0].mobjContactOps.marrCreate, agency.contacts);
		if ( (agency.documents != null) && (agency.documents.length > 0) )
			WalkDocTree(lopMIC.marrCreate[0].mobjDocOps.marrCreate, agency.documents);

		return agency;
	}

	public InsuranceAgency saveInsuranceAgency(InsuranceAgency agency)
		throws SessionExpiredException, BigBangException
	{
		ManageInsuranceCompanies lopMIC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMIC = new ManageInsuranceCompanies(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMIC.marrModify = new ManageInsuranceCompanies.CompanyData[1];
			lopMIC.marrModify[0] = lopMIC.new CompanyData();
			lopMIC.marrModify[0].mid = UUID.fromString(agency.id);
			lopMIC.marrModify[0].mstrName = agency.name;
			lopMIC.marrModify[0].mstrAcronym = agency.acronym;
			lopMIC.marrModify[0].mstrISPNumber = agency.ISPNumber;
			if ( (agency.ownMediatorCodes != null) && (agency.ownMediatorCodes.length > 0) )
				lopMIC.marrModify[0].mstrMedCode = agency.ownMediatorCodes[0];
			else
				lopMIC.marrModify[0].mstrMedCode = null;
			lopMIC.marrModify[0].mstrFiscalNumber = agency.taxNumber;
			lopMIC.marrModify[0].mstrBankID = agency.NIB;
			if ( agency.address != null )
			{
				lopMIC.marrModify[0].mstrAddress1 = agency.address.street1;
				lopMIC.marrModify[0].mstrAddress2 = agency.address.street2;
				if ( agency.address.zipCode != null )
					lopMIC.marrModify[0].midZipCode = ZipCodeBridge.GetZipCode(agency.address.zipCode.code,
							agency.address.zipCode.city, agency.address.zipCode.county, agency.address.zipCode.district,
							agency.address.zipCode.country);
				else
					lopMIC.marrModify[0].midZipCode = null;
			}
			else
			{
				lopMIC.marrModify[0].mstrAddress1 = null;
				lopMIC.marrModify[0].mstrAddress2 = null;
				lopMIC.marrModify[0].midZipCode = null;
			}
			lopMIC.marrCreate = null;
			lopMIC.marrDelete = null;
			lopMIC.mobjContactOps = null;
			lopMIC.mobjDocOps = null;

			lopMIC.Execute(null);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return agency;
	}

	public void deleteInsuranceAgency(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageInsuranceCompanies lopMIC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMIC = new ManageInsuranceCompanies(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMIC.marrDelete = new ManageInsuranceCompanies.CompanyData[1];
			lopMIC.marrDelete[0] = lopMIC.new CompanyData();
			lopMIC.marrDelete[0].mid = UUID.fromString(id);
			lopMIC.marrDelete[0].mstrName = null;
			lopMIC.marrDelete[0].mstrAcronym = null;
			lopMIC.marrDelete[0].mstrISPNumber = null;
			lopMIC.marrDelete[0].mstrMedCode = null;
			lopMIC.marrDelete[0].mstrFiscalNumber = null;
			lopMIC.marrDelete[0].mstrBankID = null;
			lopMIC.marrDelete[0].mstrAddress1 = null;
			lopMIC.marrDelete[0].mstrAddress2 = null;
			lopMIC.marrDelete[0].midZipCode = null;
			lopMIC.marrCreate = null;
			lopMIC.marrModify = null;
			lopMIC.mobjContactOps = null;
			lopMIC.mobjDocOps = null;

			lopMIC.Execute(null);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private ContactOps.ContactData[] BuildContactTree(ContactOps prefAux, Contact[] parrContacts, UUID pidParentType)
		throws BigBangJewelException
	{
		ContactOps.ContactData[] larrResult;
		int i, j;

		if ( (parrContacts == null) || (parrContacts.length == 0) )
			return null;

		larrResult = new ContactOps.ContactData[parrContacts.length];
		for ( i = 0; i < parrContacts.length; i++ )
		{
			larrResult[i] = prefAux.new ContactData();
			larrResult[i].mid = null;
			larrResult[i].mstrName = parrContacts[i].name;
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = null;
			if ( parrContacts[i].address != null )
			{
				larrResult[i].mstrAddress1 = parrContacts[i].address.street1;
				larrResult[i].mstrAddress2 = parrContacts[i].address.street2;
				larrResult[i].midZipCode = ZipCodeBridge.GetZipCode(parrContacts[i].address.zipCode.code,
						parrContacts[i].address.zipCode.city, parrContacts[i].address.zipCode.county,
						parrContacts[i].address.zipCode.district, parrContacts[i].address.zipCode.country);
			}
			else
			{
				larrResult[i].mstrAddress1 = null;
				larrResult[i].mstrAddress2 = null;
				larrResult[i].midZipCode = null;
			}
			larrResult[i].midContactType = (parrContacts[i].typeId == null ? null : UUID.fromString(parrContacts[i].typeId));
			if ( parrContacts[i].info != null )
			{
				larrResult[i].marrInfo = new ContactOps.ContactData.ContactInfoData[parrContacts[i].info.length];
				for ( j = 0; j < parrContacts[i].info.length; j++ )
				{
					larrResult[i].marrInfo[j] = larrResult[i].new ContactInfoData();
					larrResult[i].marrInfo[j].midType = UUID.fromString(parrContacts[i].info[j].typeId);
					larrResult[i].marrInfo[j].mstrValue = parrContacts[i].info[j].value;
				}
			}
			else
				larrResult[i].marrInfo = null;
			larrResult[i].marrSubContacts = BuildContactTree(prefAux, parrContacts[i].subContacts, Constants.ObjID_Contact);
		}

		return larrResult;
	}

	private DocOps.DocumentData[] BuildDocTree(DocOps prefAux, Document[] parrDocuments, UUID pidParentType)
		throws BigBangJewelException
	{
		DocOps.DocumentData[] larrResult;
		int i, j;

		if ( (parrDocuments == null) || (parrDocuments.length == 0) )
			return null;

		larrResult = new DocOps.DocumentData[parrDocuments.length];
		for ( i = 0; i < parrDocuments.length; i++ )
		{
			larrResult[i] = prefAux.new DocumentData();
			larrResult[i].mid = null;
			larrResult[i].mstrName = parrDocuments[i].name;
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = null;
			larrResult[i].midDocType = (parrDocuments[i].docTypeId == null ? null : UUID.fromString(parrDocuments[i].docTypeId));
			larrResult[i].mstrText = parrDocuments[i].text;
			if ( parrDocuments[i].fileStorageId != null )
				larrResult[i].mobjFile = FileServiceImpl.GetFileXferStorage().
						get(UUID.fromString(parrDocuments[i].fileStorageId)).GetVarData();
			else
				larrResult[i].mobjFile = null;
			if ( parrDocuments[i].parameters != null )
			{
				larrResult[i].marrInfo = new DocOps.DocumentData.DocInfoData[parrDocuments[i].parameters.length];
				for ( j = 0; j < parrDocuments[i].parameters.length; j++ )
				{
					larrResult[i].marrInfo[j] = larrResult[i].new DocInfoData();
					larrResult[i].marrInfo[j].mstrType = parrDocuments[i].parameters[j].name;
					larrResult[i].marrInfo[j].mstrValue = parrDocuments[i].parameters[j].value;
				}
			}
			else
				larrResult[i].marrInfo = null;
		}

		return larrResult;
	}

	private void WalkContactTree(ContactOps.ContactData[] parrResults, Contact[] parrContacts)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
		{
			parrContacts[i].id = parrResults[i].mid.toString();
			if ( (parrContacts[i].subContacts != null) && (parrResults[i].marrSubContacts != null) )
				WalkContactTree(parrResults[i].marrSubContacts, parrContacts[i].subContacts);
		}
	}

	private void WalkDocTree(DocOps.DocumentData[] parrResults, Document[] parrDocuments)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
			parrDocuments[i].id = parrResults[i].mid.toString();
	}
}
