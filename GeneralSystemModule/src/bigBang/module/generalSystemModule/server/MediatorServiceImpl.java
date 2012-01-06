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
import bigBang.definitions.shared.CommissionProfile;
import bigBang.definitions.shared.Mediator;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.MediatorService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.ZipCodeBridge;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;

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
		ObjectBase lobjAux, lobjProfile, lobjZipCode;
		Mediator lobjTmp;

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
	        lrsMediators = Entity.GetInstance(lidMediators).SelectAll(ldb);
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
	        	lobjAux = Engine.GetWorkInstance(lidMediators, lrsMediators);
	        	lobjProfile = Engine.GetWorkInstance(lidProfiles, (UUID)lobjAux.getAt(4));
	        	lobjZipCode = Engine.GetWorkInstance(lidZipCodes, (UUID)lobjAux.getAt(7));
	        	lobjTmp = new Mediator();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.name = (String)lobjAux.getAt(0);
	        	lobjTmp.ISPNumber = (String)lobjAux.getAt(1);
	        	lobjTmp.taxNumber = (String)lobjAux.getAt(2);
	        	lobjTmp.NIB = (String)lobjAux.getAt(3);
	        	lobjTmp.comissionProfile = new CommissionProfile();
	        	lobjTmp.comissionProfile.id = ((UUID)lobjAux.getAt(4)).toString();
	        	lobjTmp.comissionProfile.value = (String)lobjProfile.getAt(0);
	        	lobjTmp.address = new Address();
	        	lobjTmp.address.street1 = (String)lobjAux.getAt(5);
	        	lobjTmp.address.street2 = (String)lobjAux.getAt(6);
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

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMM.marrCreate = new ManageMediators.MediatorData[1];
			lopMM.marrCreate[0] = lopMM.new MediatorData();
			lopMM.marrCreate[0].mid = null;
			lopMM.marrCreate[0].mstrName = mediator.name;
			lopMM.marrCreate[0].mstrISPNumber = mediator.ISPNumber;
			lopMM.marrCreate[0].mstrFiscalNumber = mediator.taxNumber;
			lopMM.marrCreate[0].mstrBankID = mediator.NIB;
			if ( mediator.comissionProfile != null )
				lopMM.marrCreate[0].midProfile = UUID.fromString(mediator.comissionProfile.id);
			else
				lopMM.marrCreate[0].midProfile = null;
			if ( mediator.address != null )
			{
				lopMM.marrCreate[0].mstrAddress1 = mediator.address.street1;
				lopMM.marrCreate[0].mstrAddress2 = mediator.address.street2;
				if ( mediator.address.zipCode != null )
					lopMM.marrCreate[0].midZipCode = ZipCodeBridge.GetZipCode(mediator.address.zipCode.code,
							mediator.address.zipCode.city, mediator.address.zipCode.county, mediator.address.zipCode.district,
							mediator.address.zipCode.country);
				else
					lopMM.marrCreate[0].midZipCode = null;
			}
			else
			{
				lopMM.marrCreate[0].mstrAddress1 = null;
				lopMM.marrCreate[0].mstrAddress2 = null;
				lopMM.marrCreate[0].midZipCode = null;
			}
			if ( (mediator.contacts != null) && (mediator.contacts.length > 0) )
			{
				lopMM.marrCreate[0].mobjContactOps = new ContactOps();
				lopMM.marrCreate[0].mobjContactOps.marrCreate =
						ContactsServiceImpl.BuildContactTree(lopMM.marrCreate[0].mobjContactOps, mediator.contacts,
								Constants.ObjID_Mediator);
			}
			else
				lopMM.marrCreate[0].mobjContactOps = null;
			if ( (mediator.documents != null) && (mediator.documents.length > 0) )
			{
				lopMM.marrCreate[0].mobjDocOps = new DocOps();
				lopMM.marrCreate[0].mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopMM.marrCreate[0].mobjDocOps,
						mediator.documents);
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

		mediator.id = lopMM.marrCreate[0].mid.toString();
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

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMM.marrModify = new ManageMediators.MediatorData[1];
			lopMM.marrModify[0] = lopMM.new MediatorData();
			lopMM.marrModify[0].mid = UUID.fromString(mediator.id);
			lopMM.marrModify[0].mstrName = mediator.name;
			lopMM.marrModify[0].mstrISPNumber = mediator.ISPNumber;
			lopMM.marrModify[0].mstrFiscalNumber = mediator.taxNumber;
			lopMM.marrModify[0].mstrBankID = mediator.NIB;
			if ( mediator.comissionProfile != null )
				lopMM.marrModify[0].midProfile = UUID.fromString(mediator.comissionProfile.id);
			else
				lopMM.marrModify[0].midProfile = null;
			if ( mediator.address != null )
			{
				lopMM.marrModify[0].mstrAddress1 = mediator.address.street1;
				lopMM.marrModify[0].mstrAddress2 = mediator.address.street2;
				if ( mediator.address.zipCode != null )
					lopMM.marrModify[0].midZipCode = ZipCodeBridge.GetZipCode(mediator.address.zipCode.code,
							mediator.address.zipCode.city, mediator.address.zipCode.county, mediator.address.zipCode.district,
							mediator.address.zipCode.country);
				else
					lopMM.marrModify[0].midZipCode = null;
			}
			else
			{
				lopMM.marrModify[0].mstrAddress1 = null;
				lopMM.marrModify[0].mstrAddress2 = null;
				lopMM.marrModify[0].midZipCode = null;
			}
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
			lopMM.marrDelete = new ManageMediators.MediatorData[1];
			lopMM.marrDelete[0] = lopMM.new MediatorData();
			lopMM.marrDelete[0].mid = UUID.fromString(id);
			lopMM.marrDelete[0].mstrName = null;
			lopMM.marrDelete[0].mstrISPNumber = null;
			lopMM.marrDelete[0].mstrFiscalNumber = null;
			lopMM.marrDelete[0].mstrBankID = null;
			lopMM.marrDelete[0].midProfile = null;
			lopMM.marrDelete[0].mstrAddress1 = null;
			lopMM.marrDelete[0].mstrAddress2 = null;
			lopMM.marrDelete[0].midZipCode = null;
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
