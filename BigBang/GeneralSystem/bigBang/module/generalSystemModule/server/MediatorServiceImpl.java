package bigBang.module.generalSystemModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Mediator;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.MediatorService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MediatorData;
import com.premiumminds.BigBang.Jewel.Data.MediatorDealData;
import com.premiumminds.BigBang.Jewel.Data.MediatorExceptionData;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.MediatorDeal;
import com.premiumminds.BigBang.Jewel.Objects.MediatorException;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class MediatorServiceImpl
	extends EngineImplementor
	implements MediatorService
{
	private static final long serialVersionUID = 1L;

	public Mediator[] getMediators()
		throws SessionExpiredException, BigBangException
	{
		UUID lidMediators;
		UUID lidProfiles;
		UUID lidZipCodes;
        MasterDB ldb;
        ResultSet lrsMediators;
		ArrayList<Mediator> larrAux;
		com.premiumminds.BigBang.Jewel.Objects.Mediator lobjAux;
		ObjectBase lobjProfile, lobjZipCode;
		Mediator lobjTmp;
		MediatorDeal[] larrDeals;
		MediatorException[] larrExceptions;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<Mediator>();

		try
		{
			lidMediators = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator);
			lidProfiles = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CommProfile);
			lidZipCodes = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsMediators = Entity.GetInstance(lidMediators).SelectAllSort(ldb, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsMediators.next())
	        {
	        	lobjAux = com.premiumminds.BigBang.Jewel.Objects.Mediator.GetInstance(Engine.getCurrentNameSpace(), lrsMediators);
	        	lobjProfile = Engine.GetWorkInstance(lidProfiles, (UUID)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.PROFILE));
	        	lobjZipCode = (lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.ZIPCODE) == null ? null :
	        			Engine.GetWorkInstance(lidZipCodes, (UUID)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.ZIPCODE)));
	        	lobjTmp = new Mediator();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.name = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.NAME);
	        	lobjTmp.ISPNumber = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.ISPNUMBER);
	        	lobjTmp.taxNumber = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.FISCALNUMBER);
	        	lobjTmp.NIB = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.BANKINGID);
	        	lobjTmp.comissionProfile.id = ((UUID)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.PROFILE)).toString();
	        	lobjTmp.comissionProfile.value = lobjProfile.getLabel();
	        	lobjTmp.basePercent = (lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.PERCENT) == null ? null :
	        			((BigDecimal)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.PERCENT)).doubleValue());
	        	lobjTmp.hasRetention = (Boolean)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.HASRETENTION);
	        	lobjTmp.address = new Address();
	        	lobjTmp.address.street1 = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.ADDRESS1);
	        	lobjTmp.address.street2 = (String)lobjAux.getAt(com.premiumminds.BigBang.Jewel.Objects.Mediator.I.ADDRESS2);
	        	if ( lobjZipCode == null )
	        		lobjTmp.address.zipCode = null;
	        	else
	        	{
		        	lobjTmp.address.zipCode = new ZipCode();
		        	lobjTmp.address.zipCode.code = (String)lobjZipCode.getAt(0);
		        	lobjTmp.address.zipCode.city = (String)lobjZipCode.getAt(1);
		        	lobjTmp.address.zipCode.county = (String)lobjZipCode.getAt(2);
		        	lobjTmp.address.zipCode.district = (String)lobjZipCode.getAt(3);
		        	lobjTmp.address.zipCode.country = (String)lobjZipCode.getAt(4);
	        	}

	        	larrDeals = lobjAux.GetCurrentDeals();
	        	if ( larrDeals != null )
	        		for ( i = 0; i < larrDeals.length; i++ )
	        			lobjTmp.dealPercents.put(((UUID)larrDeals[i].getAt(MediatorDeal.I.SUBLINE)).toString(),
	        					((BigDecimal)larrDeals[i].getAt(MediatorDeal.I.PERCENT)).doubleValue());

	        	larrExceptions = lobjAux.GetCurrentExceptions();
	        	if ( larrExceptions != null )
	        	{
	        		lobjTmp.exceptions = new Mediator.MediatorException[larrExceptions.length];
	        		for ( i = 0; i < larrExceptions.length; i++ )
	        		{
	        			lobjTmp.exceptions[i] = new Mediator.MediatorException();
	        			lobjTmp.exceptions[i].clientId = (larrExceptions[i].getAt(MediatorException.I.CLIENT) == null ? null :
	        					((UUID)larrExceptions[i].getAt(MediatorException.I.CLIENT)).toString());
	        			lobjTmp.exceptions[i].policyId = (larrExceptions[i].getAt(MediatorException.I.POLICY) == null ? null :
        					((UUID)larrExceptions[i].getAt(MediatorException.I.POLICY)).toString());
	        			lobjTmp.exceptions[i].percent = ((BigDecimal)larrDeals[i].getAt(MediatorDeal.I.PERCENT)).doubleValue();
	        		}
	        	}

	        	larrAux.add(lobjTmp);
	        }
        }
        catch (Throwable e)
        {
			try { lrsMediators.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsMediators.close();
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

		return larrAux.toArray(new Mediator[larrAux.size()]);
	}

	public Mediator createMediator(Mediator mediator)
		throws SessionExpiredException, BigBangException
	{
		ManageMediators lopMM;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMM.marrCreate = new ManageMediators.MediatorOpData[1];
			lopMM.marrCreate[0] = new ManageMediators.MediatorOpData();
			lopMM.marrCreate[0].mobjMainValues = new MediatorData();
			lopMM.marrCreate[0].mobjMainValues.mid = null;
			lopMM.marrCreate[0].mobjMainValues.mstrName = mediator.name;
			lopMM.marrCreate[0].mobjMainValues.mstrISPNumber = mediator.ISPNumber;
			lopMM.marrCreate[0].mobjMainValues.mstrFiscalNumber = mediator.taxNumber;
			lopMM.marrCreate[0].mobjMainValues.mstrBankID = mediator.NIB;
			if ( mediator.comissionProfile != null )
				lopMM.marrCreate[0].mobjMainValues.midProfile = UUID.fromString(mediator.comissionProfile.id);
			else
				lopMM.marrCreate[0].mobjMainValues.midProfile = null;
			lopMM.marrCreate[0].mobjMainValues.mdblPercent = (mediator.basePercent == null ? null :
					new BigDecimal(mediator.basePercent));
			lopMM.marrCreate[0].mobjMainValues.mbHasRetention = mediator.hasRetention;
			if ( mediator.address != null )
			{
				lopMM.marrCreate[0].mobjMainValues.mstrAddress1 = mediator.address.street1;
				lopMM.marrCreate[0].mobjMainValues.mstrAddress2 = mediator.address.street2;
				if ( mediator.address.zipCode != null )
					lopMM.marrCreate[0].mobjMainValues.midZipCode = ZipCodeBridge.GetZipCode(mediator.address.zipCode.code,
							mediator.address.zipCode.city, mediator.address.zipCode.county, mediator.address.zipCode.district,
							mediator.address.zipCode.country);
				else
					lopMM.marrCreate[0].mobjMainValues.midZipCode = null;
			}
			else
			{
				lopMM.marrCreate[0].mobjMainValues.mstrAddress1 = null;
				lopMM.marrCreate[0].mobjMainValues.mstrAddress2 = null;
				lopMM.marrCreate[0].mobjMainValues.midZipCode = null;
			}

			if ( (mediator.dealPercents != null ) && (mediator.dealPercents.size() > 0) )
			{
				lopMM.marrCreate[0].mobjMainValues.marrDeals = new MediatorDealData[mediator.dealPercents.size()];
				i = 0;
				for ( String str: mediator.dealPercents.keySet() )
				{
					lopMM.marrCreate[0].mobjMainValues.marrDeals[i] = new MediatorDealData();
					lopMM.marrCreate[0].mobjMainValues.marrDeals[i].midSubLine = UUID.fromString(str);
					lopMM.marrCreate[0].mobjMainValues.marrDeals[i].mdblPercent = new BigDecimal(mediator.dealPercents.get(str));
					i++;
				}
			}
			else
				lopMM.marrCreate[0].mobjMainValues.marrDeals = null;

			if ( (mediator.exceptions != null ) && (mediator.exceptions.length > 0) )
			{
				lopMM.marrCreate[0].mobjMainValues.marrExceptions = new MediatorExceptionData[mediator.exceptions.length];
				for ( i = 0; i < mediator.exceptions.length; i++ )
				{
					lopMM.marrCreate[0].mobjMainValues.marrExceptions[i] = new MediatorExceptionData();
					lopMM.marrCreate[0].mobjMainValues.marrExceptions[i].midClient = ( mediator.exceptions[i].clientId == null ? null :
							UUID.fromString(mediator.exceptions[i].clientId) );
					lopMM.marrCreate[0].mobjMainValues.marrExceptions[i].midPolicy = ( mediator.exceptions[i].policyId == null ? null :
						UUID.fromString(mediator.exceptions[i].policyId) );
					lopMM.marrCreate[0].mobjMainValues.marrExceptions[i].mdblPercent = new BigDecimal(mediator.exceptions[i].percent);
				}
			}
			else
				lopMM.marrCreate[0].mobjMainValues.marrExceptions = null;

			if ( (mediator.contacts != null) && (mediator.contacts.length > 0) )
			{
				lopMM.marrCreate[0].mobjContactOps = new ContactOps();
				lopMM.marrCreate[0].mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(mediator.contacts);
			}
			else
				lopMM.marrCreate[0].mobjContactOps = null;

			if ( (mediator.documents != null) && (mediator.documents.length > 0) )
			{
				lopMM.marrCreate[0].mobjDocOps = new DocOps();
				lopMM.marrCreate[0].mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(mediator.documents);
			}
			else
				lopMM.marrCreate[0].mobjDocOps = null;

			lopMM.marrModify = null;
			lopMM.marrDelete = null;
			lopMM.mobjContactOps = null;
			lopMM.mobjDocOps = null;

			lopMM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		mediator.id = lopMM.marrCreate[0].mobjMainValues.mid.toString();
		if ( (mediator.contacts != null) && (mediator.contacts.length > 0) )
			ContactsServiceImpl.WalkContactTree(lopMM.marrCreate[0].mobjContactOps.marrCreate, mediator.contacts);
		if ( (mediator.documents != null) && (mediator.documents.length > 0) )
			DocumentServiceImpl.WalkDocTree(lopMM.marrCreate[0].mobjDocOps.marrCreate, mediator.documents);

		return mediator;
	}

	public Mediator saveMediator(Mediator mediator)
		throws SessionExpiredException, BigBangException
	{
		ManageMediators lopMM;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMM.marrModify = new ManageMediators.MediatorOpData[1];
			lopMM.marrModify[0] = new ManageMediators.MediatorOpData();
			lopMM.marrModify[0].mobjMainValues = new MediatorData();
			lopMM.marrModify[0].mobjMainValues.mid = UUID.fromString(mediator.id);
			lopMM.marrModify[0].mobjMainValues.mstrName = mediator.name;
			lopMM.marrModify[0].mobjMainValues.mstrISPNumber = mediator.ISPNumber;
			lopMM.marrModify[0].mobjMainValues.mstrFiscalNumber = mediator.taxNumber;
			lopMM.marrModify[0].mobjMainValues.mstrBankID = mediator.NIB;
			if ( mediator.comissionProfile != null )
				lopMM.marrModify[0].mobjMainValues.midProfile = UUID.fromString(mediator.comissionProfile.id);
			else
				lopMM.marrModify[0].mobjMainValues.midProfile = null;
			lopMM.marrModify[0].mobjMainValues.mdblPercent = (mediator.basePercent == null ? null :
				new BigDecimal(mediator.basePercent));
			lopMM.marrModify[0].mobjMainValues.mbHasRetention = mediator.hasRetention;
			if ( mediator.address != null )
			{
				lopMM.marrModify[0].mobjMainValues.mstrAddress1 = mediator.address.street1;
				lopMM.marrModify[0].mobjMainValues.mstrAddress2 = mediator.address.street2;
				if ( mediator.address.zipCode != null )
					lopMM.marrModify[0].mobjMainValues.midZipCode = ZipCodeBridge.GetZipCode(mediator.address.zipCode.code,
							mediator.address.zipCode.city, mediator.address.zipCode.county, mediator.address.zipCode.district,
							mediator.address.zipCode.country);
				else
					lopMM.marrModify[0].mobjMainValues.midZipCode = null;
			}
			else
			{
				lopMM.marrModify[0].mobjMainValues.mstrAddress1 = null;
				lopMM.marrModify[0].mobjMainValues.mstrAddress2 = null;
				lopMM.marrModify[0].mobjMainValues.midZipCode = null;
			}

			if ( (mediator.dealPercents != null ) && (mediator.dealPercents.size() > 0) )
			{
				lopMM.marrModify[0].mobjMainValues.marrDeals = new MediatorDealData[mediator.dealPercents.size()];
				i = 0;
				for ( String str: mediator.dealPercents.keySet() )
				{
					lopMM.marrModify[0].mobjMainValues.marrDeals[i] = new MediatorDealData();
					lopMM.marrModify[0].mobjMainValues.marrDeals[i].midSubLine = UUID.fromString(str);
					lopMM.marrModify[0].mobjMainValues.marrDeals[i].mdblPercent = new BigDecimal(mediator.dealPercents.get(str));
					i++;
				}
			}
			else
				lopMM.marrModify[0].mobjMainValues.marrDeals = null;

			if ( (mediator.exceptions != null ) && (mediator.exceptions.length > 0) )
			{
				lopMM.marrModify[0].mobjMainValues.marrExceptions = new MediatorExceptionData[mediator.exceptions.length];
				for ( i = 0; i < mediator.exceptions.length; i++ )
				{
					lopMM.marrModify[0].mobjMainValues.marrExceptions[i] = new MediatorExceptionData();
					lopMM.marrModify[0].mobjMainValues.marrExceptions[i].midClient = ( mediator.exceptions[i].clientId == null ? null :
							UUID.fromString(mediator.exceptions[i].clientId) );
					lopMM.marrModify[0].mobjMainValues.marrExceptions[i].midPolicy = ( mediator.exceptions[i].policyId == null ? null :
						UUID.fromString(mediator.exceptions[i].policyId) );
					lopMM.marrModify[0].mobjMainValues.marrExceptions[i].mdblPercent = new BigDecimal(mediator.exceptions[i].percent);
				}
			}
			else
				lopMM.marrModify[0].mobjMainValues.marrExceptions = null;

			lopMM.marrCreate = null;
			lopMM.marrDelete = null;
			lopMM.mobjContactOps = null;
			lopMM.mobjDocOps = null;

			lopMM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return mediator;
	}

	public void deleteMediator(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageMediators lopMM;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMM.marrDelete = new ManageMediators.MediatorOpData[1];
			lopMM.marrDelete[0] = new ManageMediators.MediatorOpData();
			lopMM.marrDelete[0].mobjMainValues = new MediatorData();
			lopMM.marrDelete[0].mobjMainValues.mid = UUID.fromString(id);
			lopMM.marrDelete[0].mobjMainValues.marrDeals = null;
			lopMM.marrCreate = null;
			lopMM.marrModify = null;
			lopMM.mobjContactOps = null;
			lopMM.mobjDocOps = null;

			lopMM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
