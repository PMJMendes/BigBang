package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Objects.PNOperation;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.ZipCodeBridge;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageClientData;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;

public class ContactsServiceImpl
	extends EngineImplementor
	implements ContactsService
{
	private static final long serialVersionUID = 1L;

	public static ContactData[] BuildContactTree(ContactOps prefAux, Contact[] parrContacts, UUID pidParentType)
		throws BigBangJewelException
	{
		ContactData[] larrResult;
		int i, j;

		if ( (parrContacts == null) || (parrContacts.length == 0) )
			return null;

		larrResult = new ContactData[parrContacts.length];
		for ( i = 0; i < parrContacts.length; i++ )
		{
			larrResult[i] = new ContactData();
			larrResult[i].mid = (parrContacts[i].id == null ? null : UUID.fromString(parrContacts[i].id));
			larrResult[i].mstrName = parrContacts[i].name;
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = (parrContacts[i].ownerId == null ? null : UUID.fromString(parrContacts[i].ownerId));
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
				larrResult[i].marrInfo = new ContactInfoData[parrContacts[i].info.length];
				for ( j = 0; j < parrContacts[i].info.length; j++ )
				{
					larrResult[i].marrInfo[j] = new ContactInfoData();
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

	public static void WalkContactTree(ContactData[] parrResults, Contact[] parrContacts)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
		{
			parrContacts[i].id = parrResults[i].mid.toString();
			if ( (parrContacts[i].subContacts != null) && (parrResults[i].marrSubContacts != null) )
				WalkContactTree(parrResults[i].marrSubContacts, parrContacts[i].subContacts);
		}
	}

	public Contact[] getContacts(String ownerId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<Contact> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContact;
        MasterDB ldb;
        ResultSet lrsContacts;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<Contact>();

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKOwner_In_Contact;
		larrParams = new java.lang.Object[1];
		larrParams[0] = UUID.fromString(ownerId);

		try
		{
			lrefContact = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Contact)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsContacts = lrefContact.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsContacts.next() )
				larrAux.add(fromServer(com.premiumminds.BigBang.Jewel.Objects.Contact
						.GetInstance(Engine.getCurrentNameSpace(), lrsContacts)));
		}
		catch (BigBangException e)
		{
			try { lrsContacts.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsContacts.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsContacts.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return larrAux.toArray(new Contact[larrAux.size()]);
	}

	public Contact createContact(String procId, String opId, Contact contact)
		throws SessionExpiredException, BigBangException
	{
		Contact[] larrAux;
		ContactOps lopCOps;
		Operation lobjOp;

		larrAux = new Contact[1];
		larrAux[0] = contact;

		lopCOps = new ContactOps();
		try
		{
			lopCOps.marrCreate = BuildContactTree(lopCOps, larrAux, (contact.isSubContact ? Constants.ObjID_Contact :
					GetParentType(UUID.fromString(procId), UUID.fromString(opId))));
			lopCOps.marrModify = null;
			lopCOps.marrDelete = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopCOps);
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		WalkContactTree(lopCOps.marrCreate, larrAux);
		return larrAux[0];
	}

	public Contact saveContact(String procId, String opId, Contact contact)
		throws SessionExpiredException, BigBangException
	{
		Contact[] larrAux;
		ContactOps lopCOps;
		Operation lobjOp;

		larrAux = new Contact[1];
		larrAux[0] = contact;

		lopCOps = new ContactOps();
		try
		{
			lopCOps.marrModify = BuildContactTree(lopCOps, larrAux, (contact.isSubContact ? Constants.ObjID_Contact :
					GetParentType(UUID.fromString(procId), UUID.fromString(opId))));
			lopCOps.marrCreate = null;
			lopCOps.marrDelete = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopCOps);
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux[0];
	}

	public void deleteContact(String procId, String opId, Contact contact)
		throws SessionExpiredException, BigBangException
	{
		Contact[] larrAux;
		ContactOps lopCOps;
		Operation lobjOp;

		larrAux = new Contact[1];
		larrAux[0] = contact;

		lopCOps = new ContactOps();
		try
		{
			lopCOps.marrDelete = BuildContactTree(lopCOps, larrAux, (contact.isSubContact ? Constants.ObjID_Contact :
					GetParentType(UUID.fromString(procId), UUID.fromString(opId))));
			lopCOps.marrCreate = null;
			lopCOps.marrModify = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopCOps);
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private Contact fromServer(com.premiumminds.BigBang.Jewel.Objects.Contact pobjContact)
		throws BigBangException
	{
		Contact lobjAux;
		ObjectBase lobjZipCode;
		com.premiumminds.BigBang.Jewel.Objects.ContactInfo[] larrInfo;
		com.premiumminds.BigBang.Jewel.Objects.Contact[] larrSubs;
		int i;

		lobjAux = new Contact();

		lobjAux.id = pobjContact.getKey().toString();
		lobjAux.name = (String)pobjContact.getAt(0);
		lobjAux.isSubContact = ((UUID)pobjContact.getAt(2)).equals(Constants.ObjID_Contact);
		lobjAux.ownerId = ((UUID)pobjContact.getAt(2)).toString();
		lobjAux.address = new Address();
		lobjAux.address.street1 = (String)pobjContact.getAt(3);
		lobjAux.address.street2 = (String)pobjContact.getAt(4);
		if ( pobjContact.getAt(5) == null )
			lobjAux.address.zipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)pobjContact.getAt(5));
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjAux.address.zipCode = new ZipCode();
			lobjAux.address.zipCode.code = (String)lobjZipCode.getAt(0);
			lobjAux.address.zipCode.city = (String)lobjZipCode.getAt(1);
			lobjAux.address.zipCode.county = (String)lobjZipCode.getAt(2);
			lobjAux.address.zipCode.district = (String)lobjZipCode.getAt(3);
			lobjAux.address.zipCode.country = (String)lobjZipCode.getAt(4);
		}

		try
		{
			larrInfo = pobjContact.getCurrentInfo();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjAux.info = new ContactInfo[larrInfo.length];
		for ( i = 0; i < larrInfo.length; i++ )
			lobjAux.info[i] = fromServer(larrInfo[i]);

		try
		{
			larrSubs = pobjContact.getCurrentSubContacts();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjAux.subContacts = new Contact[larrSubs.length];
		for ( i = 0; i < larrSubs.length; i++ )
			lobjAux.subContacts[i] = fromServer(larrSubs[i]);

		return lobjAux;
	}

	private ContactInfo fromServer(com.premiumminds.BigBang.Jewel.Objects.ContactInfo pobjContactInfo)
	{
		ContactInfo lobjAux;

		lobjAux = new ContactInfo();

		lobjAux.typeId = ((UUID)pobjContactInfo.getAt(1)).toString();
		lobjAux.value = (String)pobjContactInfo.getAt(2);
		return lobjAux;
	}

	private UUID GetParentType(UUID pidProc, UUID pidOp)
		throws JewelPetriException
	{
		IOperation lobjOp;
		Operation lobjAux;
		UUID lidResult;

		lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), pidOp);
		lobjAux = lobjOp.GetNewInstance(pidProc);

		lidResult = null;

		if ( lobjAux instanceof ManageInsurers )
			lidResult = Constants.ObjID_Company;

		if ( lobjAux instanceof ManageMediators )
			lidResult = Constants.ObjID_Mediator;

		if ( lobjAux instanceof ManageClientData )
			lidResult = Constants.ObjID_Client;

		if ( lidResult == null )
			throw new JewelPetriException("Erro: A operação pretendida não permite movimentos de Contactos.");

		return lidResult;
	}

	private Operation BuildOuterOp(UUID pidProc, UUID pidOp, ContactOps pobjInner)
		throws JewelPetriException
	{
		IOperation lobjOp;
		Operation lobjResult;
		boolean lbFound;

		lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), pidOp);
		lobjResult = lobjOp.GetNewInstance(pidProc);

		lbFound = false;

		if ( lobjResult instanceof ManageInsurers )
		{
			((ManageInsurers)lobjResult).marrCreate = null;
			((ManageInsurers)lobjResult).marrModify = null;
			((ManageInsurers)lobjResult).marrDelete = null;
			((ManageInsurers)lobjResult).mobjContactOps = pobjInner;
			((ManageInsurers)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof ManageMediators )
		{
			((ManageMediators)lobjResult).marrCreate = null;
			((ManageMediators)lobjResult).marrModify = null;
			((ManageMediators)lobjResult).marrDelete = null;
			((ManageMediators)lobjResult).mobjContactOps = pobjInner;
			((ManageMediators)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof ManageClientData )
		{
			((ManageClientData)lobjResult).mobjData = null;
			((ManageClientData)lobjResult).mobjContactOps = pobjInner;
			((ManageClientData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( !lbFound )
			throw new JewelPetriException("Erro: A operação pretendida não permite movimentos de Contactos.");

		return lobjResult;
	}
}
