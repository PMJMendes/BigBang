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
import bigBang.definitions.shared.OtherEntity;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.OtherEntityService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class OtherEntityServiceImpl
	extends EngineImplementor
	implements OtherEntityService
{
	private static final long serialVersionUID = 1L;

	public OtherEntity[] getOtherEntities()
		throws SessionExpiredException, BigBangException
	{
		UUID lidOthers;
		UUID lidTypes;
		UUID lidZipCodes;
	    MasterDB ldb;
	    ResultSet lrsOthers;
		ArrayList<OtherEntity> larrAux;
		ObjectBase lobjAux, lobjType, lobjZipCode;
		OtherEntity lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<OtherEntity>();

		try
		{
			lidOthers = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OtherEntity);
			lidTypes = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OtherEntityType);
			lidZipCodes = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

	    try
	    {
	        lrsOthers = Entity.GetInstance(lidOthers).SelectAllSort(ldb, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsOthers.next())
	        {
	        	lobjAux = Engine.GetWorkInstance(lidOthers, lrsOthers);
	        	lobjType = Engine.GetWorkInstance(lidTypes, (UUID)lobjAux.getAt(1));
	        	lobjZipCode = Engine.GetWorkInstance(lidZipCodes, (UUID)lobjAux.getAt(5));
	        	lobjTmp = new OtherEntity();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.name = (String)lobjAux.getAt(0);
	        	lobjTmp.type = ((UUID)lobjAux.getAt(1)).toString();
	        	lobjTmp.typeLabel = (String)lobjType.getAt(0);
	        	lobjTmp.notes = (String)lobjAux.getAt(2);
	        	lobjTmp.address = new Address();
	        	lobjTmp.address.street1 = (String)lobjAux.getAt(3);
	        	lobjTmp.address.street2 = (String)lobjAux.getAt(4);
	        	lobjTmp.address.zipCode = new ZipCode();
	        	lobjTmp.address.zipCode.code = (String)lobjZipCode.getAt(0);
	        	lobjTmp.address.zipCode.city = (String)lobjZipCode.getAt(1);
	        	lobjTmp.address.zipCode.county = (String)lobjZipCode.getAt(2);
	        	lobjTmp.address.zipCode.district = (String)lobjZipCode.getAt(3);
	        	lobjTmp.address.zipCode.country = (String)lobjZipCode.getAt(4);
	        	lobjTmp.taxNumber = (String)lobjAux.getAt(6);
	        	larrAux.add(lobjTmp);
	        }
	    }
	    catch (Throwable e)
	    {
			try { lrsOthers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
	    	throw new BigBangException(e.getMessage(), e);
	    }

	    try
	    {
	    	lrsOthers.close();
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

		return larrAux.toArray(new OtherEntity[larrAux.size()]);
	}

	public OtherEntity createOtherEntity(OtherEntity entity)
		throws SessionExpiredException, BigBangException
	{
		ManageOtherEntities lopMOE;
		ObjectBase lobjType;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMOE = new ManageOtherEntities(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMOE.marrCreate = new ManageOtherEntities.EntityData[1];
			lopMOE.marrCreate[0] = lopMOE.new EntityData();
			lopMOE.marrCreate[0].mid = null;
			lopMOE.marrCreate[0].mstrName = entity.name;
			lopMOE.marrCreate[0].midType = UUID.fromString(entity.type);
			lopMOE.marrCreate[0].mstrNotes = entity.notes;
			if ( entity.address != null )
			{
				lopMOE.marrCreate[0].mstrAddress1 = entity.address.street1;
				lopMOE.marrCreate[0].mstrAddress2 = entity.address.street2;
				if ( entity.address.zipCode != null )
					lopMOE.marrCreate[0].midZipCode = ZipCodeBridge.GetZipCode(entity.address.zipCode.code,
							entity.address.zipCode.city, entity.address.zipCode.county, entity.address.zipCode.district,
							entity.address.zipCode.country);
				else
					lopMOE.marrCreate[0].midZipCode = null;
			}
			else
			{
				lopMOE.marrCreate[0].mstrAddress1 = null;
				lopMOE.marrCreate[0].mstrAddress2 = null;
				lopMOE.marrCreate[0].midZipCode = null;
			}
			lopMOE.marrCreate[0].mstrFiscal = entity.taxNumber;
			if ( (entity.contacts != null) && (entity.contacts.length > 0) )
			{
				lopMOE.marrCreate[0].mobjContactOps = new ContactOps();
				lopMOE.marrCreate[0].mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(entity.contacts);
			}
			else
				lopMOE.marrCreate[0].mobjContactOps = null;
			if ( (entity.documents != null) && (entity.documents.length > 0) )
			{
				lopMOE.marrCreate[0].mobjDocOps = new DocOps();
				lopMOE.marrCreate[0].mobjDocOps.marrCreate2 = DocumentServiceImpl.buildTreeLight(entity.documents);
			}
			else
				lopMOE.marrCreate[0].mobjDocOps = null;
			lopMOE.marrModify = null;
			lopMOE.marrDelete = null;
			lopMOE.mobjContactOps = null;
			lopMOE.mobjDocOps = null;

			lopMOE.Execute();

        	lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OtherEntityType),
        			lopMOE.marrCreate[0].midType);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		entity.id = lopMOE.marrCreate[0].mid.toString();
		entity.typeLabel = lobjType.getLabel();
		if ( (entity.contacts != null) && (entity.contacts.length > 0) )
			ContactsServiceImpl.WalkContactTree(lopMOE.marrCreate[0].mobjContactOps.marrCreate, entity.contacts);
		if ( (entity.documents != null) && (entity.documents.length > 0) )
			DocumentServiceImpl.WalkDocTree(lopMOE.marrCreate[0].mobjDocOps.marrCreate2, entity.documents);

		return entity;
	}

	public OtherEntity saveOtherEntity(OtherEntity entity)
		throws SessionExpiredException, BigBangException
	{
		ManageOtherEntities lopMOE;
		ObjectBase lobjType;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMOE = new ManageOtherEntities(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMOE.marrModify = new ManageOtherEntities.EntityData[1];
			lopMOE.marrModify[0] = lopMOE.new EntityData();
			lopMOE.marrModify[0].mid = UUID.fromString(entity.id);
			lopMOE.marrModify[0].mstrName = entity.name;
			lopMOE.marrModify[0].midType = UUID.fromString(entity.type);
			lopMOE.marrModify[0].mstrNotes = entity.notes;
			if ( entity.address != null )
			{
				lopMOE.marrModify[0].mstrAddress1 = entity.address.street1;
				lopMOE.marrModify[0].mstrAddress2 = entity.address.street2;
				if ( entity.address.zipCode != null )
					lopMOE.marrModify[0].midZipCode = ZipCodeBridge.GetZipCode(entity.address.zipCode.code,
							entity.address.zipCode.city, entity.address.zipCode.county, entity.address.zipCode.district,
							entity.address.zipCode.country);
				else
					lopMOE.marrModify[0].midZipCode = null;
			}
			else
			{
				lopMOE.marrModify[0].mstrAddress1 = null;
				lopMOE.marrModify[0].mstrAddress2 = null;
				lopMOE.marrModify[0].midZipCode = null;
			}
			lopMOE.marrModify[0].mstrFiscal = entity.taxNumber;
			lopMOE.marrCreate = null;
			lopMOE.marrDelete = null;
			lopMOE.mobjContactOps = null;
			lopMOE.mobjDocOps = null;

			lopMOE.Execute();

        	lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OtherEntityType),
        			lopMOE.marrModify[0].midType);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		entity.typeLabel = lobjType.getLabel();

		return entity;
	}

	public void deleteOtherEntity(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageOtherEntities lopMOE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMOE = new ManageOtherEntities(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMOE.marrDelete = new ManageOtherEntities.EntityData[1];
			lopMOE.marrDelete[0] = lopMOE.new EntityData();
			lopMOE.marrDelete[0].mid = UUID.fromString(id);
			lopMOE.marrDelete[0].mstrName = null;
			lopMOE.marrDelete[0].midType = null;
			lopMOE.marrDelete[0].mstrNotes = null;
			lopMOE.marrDelete[0].mstrAddress1 = null;
			lopMOE.marrDelete[0].mstrAddress2 = null;
			lopMOE.marrDelete[0].midZipCode = null;
			lopMOE.marrDelete[0].mstrFiscal = null;
			lopMOE.marrCreate = null;
			lopMOE.marrModify = null;
			lopMOE.mobjContactOps = null;
			lopMOE.mobjDocOps = null;

			lopMOE.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
