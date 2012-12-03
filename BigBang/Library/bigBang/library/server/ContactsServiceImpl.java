package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Objects.PNOperation;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class ContactsServiceImpl
	extends EngineImplementor
	implements ContactsService
{
	private static final long serialVersionUID = 1L;

	public static ContactData[] BuildContactTree(Contact[] parrContacts)
		throws BigBangException
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
			larrResult[i].midOwnerType = UUID.fromString(parrContacts[i].ownerTypeId);
			larrResult[i].midOwnerId = (parrContacts[i].ownerId == null ? null : UUID.fromString(parrContacts[i].ownerId));
			if ( (parrContacts[i].address == null) || ((parrContacts[i].address.street1 == null) &&
					(parrContacts[i].address.street2 == null) && ((parrContacts[i].address.zipCode == null) ||
					((parrContacts[i].address.zipCode.code == null) && (parrContacts[i].address.zipCode.city == null) &&
					(parrContacts[i].address.zipCode.county == null) && (parrContacts[i].address.zipCode.district == null) &&
					(parrContacts[i].address.zipCode.country == null)))) )
			{
				larrResult[i].mstrAddress1 = null;
				larrResult[i].mstrAddress2 = null;
				larrResult[i].midZipCode = null;
			}
			else
			{
				larrResult[i].mstrAddress1 = parrContacts[i].address.street1;
				larrResult[i].mstrAddress2 = parrContacts[i].address.street2;
				if ( (parrContacts[i].address.zipCode == null) || ((parrContacts[i].address.zipCode.code == null) &&
						(parrContacts[i].address.zipCode.city == null) && (parrContacts[i].address.zipCode.county == null) &&
						(parrContacts[i].address.zipCode.district == null) && (parrContacts[i].address.zipCode.country == null)) )
				{
					larrResult[i].midZipCode = null;
				}
				else
				{
					try
					{
						larrResult[i].midZipCode = ZipCodeBridge.GetZipCode(parrContacts[i].address.zipCode.code,
								parrContacts[i].address.zipCode.city, parrContacts[i].address.zipCode.county,
								parrContacts[i].address.zipCode.district, parrContacts[i].address.zipCode.country);
					}
					catch (Throwable e)
					{
						throw new BigBangException(e.getMessage(), e);
					}
				}
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
			larrResult[i].marrSubContacts = BuildContactTree(parrContacts[i].subContacts);
		}

		return larrResult;
	}

	public static void WalkContactTree(ContactData[] parrResults, Contact[] parrContacts)
		throws BigBangException
	{
		ObjectBase lobjType;
		int i;

		for ( i = 0; i < parrResults.length; i++ )
		{
			try
			{
				lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ContactType),
						parrResults[i].midContactType);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			parrContacts[i].id = parrResults[i].mid.toString();
			parrContacts[i].typeLabel = lobjType.getLabel();
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
						.GetInstance(Engine.getCurrentNameSpace(), lrsContacts), ldb, true));
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

	public Contact createContact(Contact contact)
		throws SessionExpiredException, BigBangException
	{
		Contact[] larrAux;
		ContactOps lopCOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Contact[1];
		larrAux[0] = contact;

		lopCOps = new ContactOps();
		lopCOps.marrCreate = BuildContactTree(larrAux);
		lopCOps.marrModify = null;
		lopCOps.marrDelete = null;

		lobjOp = BuildOuterOp(UUID.fromString(contact.ownerTypeId), UUID.fromString(contact.ownerId), lopCOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		WalkContactTree(lopCOps.marrCreate, larrAux);
		return larrAux[0];
	}

	public Contact saveContact(Contact contact)
		throws SessionExpiredException, BigBangException
	{
		Contact[] larrAux;
		ContactOps lopCOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Contact[1];
		larrAux[0] = contact;

		lopCOps = new ContactOps();
		lopCOps.marrModify = BuildContactTree(larrAux);
		lopCOps.marrCreate = null;
		lopCOps.marrDelete = null;

		lobjOp = BuildOuterOp(UUID.fromString(contact.ownerTypeId), UUID.fromString(contact.ownerId), lopCOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		WalkContactTree(lopCOps.marrModify, larrAux);
		return larrAux[0];
	}

	public void deleteContact(String id)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Contact lobjAuxContact;
		ContactData lobjData;
		ContactOps lopCOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAuxContact = com.premiumminds.BigBang.Jewel.Objects.Contact.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjData = new ContactData();
		lobjData.mid = lobjAuxContact.getKey();
		lobjData.midOwnerType = lobjAuxContact.getOwnerType();
		lobjData.midOwnerId = lobjAuxContact.getOwnerID();

		lopCOps = new ContactOps();
		lopCOps.marrDelete = new ContactData[] {lobjData};
		lopCOps.marrCreate = null;
		lopCOps.marrModify = null;

		lobjOp = BuildOuterOp(lobjData.midOwnerType, lobjData.midOwnerId, lopCOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public Contact[] getFlatEmails(String ownerId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<Contact> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContact;
        MasterDB ldb;
        ResultSet lrsContacts;
        com.premiumminds.BigBang.Jewel.Objects.Contact lobjContact;
        Contact[] larrResult;

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
			{
				lobjContact = com.premiumminds.BigBang.Jewel.Objects.Contact.GetInstance(Engine.getCurrentNameSpace(), lrsContacts);
				innerGetFlatEmails(larrAux, lobjContact, ldb);
			}
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

		larrResult = larrAux.toArray(new Contact[larrAux.size()]);
		java.util.Arrays.sort(larrResult, new Comparator<Contact>()
		{
			public int compare(Contact o1, Contact o2)
			{
				return o1.name.compareTo(o2.name);
			}
		});
		return larrResult;
	}

	private Contact fromServer(com.premiumminds.BigBang.Jewel.Objects.Contact pobjContact, SQLServer pdb, boolean pbRecurse)
		throws BigBangException
	{
		Contact lobjAux;
		ObjectBase lobjType, lobjZipCode;
		com.premiumminds.BigBang.Jewel.Objects.ContactInfo[] larrInfo;
		com.premiumminds.BigBang.Jewel.Objects.Contact[] larrSubs;
		int i;

		lobjAux = new Contact();

		lobjAux.id = pobjContact.getKey().toString();
		lobjAux.name = (String)pobjContact.getAt(0);
		lobjAux.ownerTypeId = ((UUID)pobjContact.getAt(1)).toString();
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
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ContactType),
					(UUID)pobjContact.getAt(6));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjAux.typeId = lobjType.getKey().toString();
		lobjAux.typeLabel = lobjType.getLabel();

		if ( pbRecurse )
		{
			try
			{
				larrInfo = pobjContact.getCurrentInfo(pdb);
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
				larrSubs = pobjContact.getCurrentSubContacts(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjAux.subContacts = new Contact[larrSubs.length];
			for ( i = 0; i < larrSubs.length; i++ )
				lobjAux.subContacts[i] = fromServer(larrSubs[i], pdb, true);
		}
		else
		{
			lobjAux.info = null;
			lobjAux.subContacts = null;
		}

		return lobjAux;
	}

	private ContactInfo fromServer(com.premiumminds.BigBang.Jewel.Objects.ContactInfo pobjContactInfo)
	{
		ContactInfo lobjAux;

		lobjAux = new ContactInfo();

		lobjAux.id = pobjContactInfo.getKey().toString();
		lobjAux.typeId = ((UUID)pobjContactInfo.getAt(1)).toString();
		lobjAux.value = (String)pobjContactInfo.getAt(2);
		return lobjAux;
	}

	private Operation BuildOuterOp(UUID pidOwnerType, UUID pidOwner, ContactOps pobjInner)
		throws BigBangException
	{
		ObjectBase lobjOwner;
		UUID lidTopType;
		UUID lidProc;
		UUID lidOp;
		IOperation lobjOp;
		Operation lobjResult;
		boolean lbFound;

		try
		{
			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidOwnerType), pidOwner);
			while ( lobjOwner instanceof com.premiumminds.BigBang.Jewel.Objects.Contact )
				lobjOwner = ((com.premiumminds.BigBang.Jewel.Objects.Contact)lobjOwner).getOwner();
			lidTopType = lobjOwner.getDefinition().getDefObject().getKey();

			if ( (lobjOwner instanceof Company) || (lobjOwner instanceof Mediator) )
				lobjOwner = GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjOwner instanceof ProcessData )
			lidProc = ((ProcessData)lobjOwner).GetProcessID();
		else
			throw new BigBangException("Erro: O tipo de objecto indicado não suporta processos.");

		if ( Constants.ObjID_Company.equals(lidTopType) )
			lidOp = Constants.OPID_General_ManageCompanies;
		else if ( Constants.ObjID_Mediator.equals(lidTopType) )
			lidOp = Constants.OPID_General_ManageMediators;
		else if ( Constants.ObjID_Client.equals(lidTopType) )
			lidOp = Constants.OPID_Client_ManageData;
		else if ( Constants.ObjID_QuoteRequest.equals(lidTopType) )
			lidOp = Constants.OPID_QuoteRequest_ManageData;
		else if ( Constants.ObjID_Negotiation.equals(lidTopType) )
			lidOp = Constants.OPID_Negotiation_ManageData;
		else if ( Constants.ObjID_Policy.equals(lidTopType) )
			lidOp = Constants.OPID_Policy_ManageData;
		else if ( Constants.ObjID_SubPolicy.equals(lidTopType) )
			lidOp = Constants.OPID_SubPolicy_ManageData;
		else if ( Constants.ObjID_Receipt.equals(lidTopType) )
			lidOp = Constants.OPID_Receipt_ManageData;
		else if ( Constants.ObjID_Casualty.equals(lidTopType) )
			lidOp = Constants.OPID_Casualty_ManageData;
		else if ( Constants.ObjID_SubCasualty.equals(lidTopType) )
			lidOp = Constants.OPID_SubCasualty_ManageData;
		else if ( Constants.ObjID_Expense.equals(lidTopType) )
			lidOp = Constants.OPID_Expense_ManageData;
		else
			throw new BigBangException("Erro: O objecto indicado não permite movimentos de Contactos.");

		try
		{
			lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), lidOp);
			lobjResult = lobjOp.GetNewInstance(lidProc);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbFound = false;

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers )
		{
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrCreate = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrModify = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrDelete = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators )
		{
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrCreate = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrModify = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrDelete = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Client.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjContactOps = pobjInner;
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjDocOps = null;
			lbFound = true;
		}

		if ( !lbFound )
			throw new BigBangException("Erro: A operação pretendida não permite movimentos de Contactos.");

		return lobjResult;
	}

	private void innerGetFlatEmails(ArrayList<Contact> parrBuffer, com.premiumminds.BigBang.Jewel.Objects.Contact pobjContact,
			SQLServer pdb)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.ContactInfo[] larrInfo;
		Contact lobjContact;
		ContactInfo lobjInfo;
		com.premiumminds.BigBang.Jewel.Objects.Contact[] larrSubs;
		int i;

		try
		{
			larrInfo = pobjContact.getCurrentInfo(pdb);
			larrSubs = pobjContact.getCurrentSubContacts(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrInfo != null )
		{
			for ( i = 0; i < larrInfo.length; i++ )
			{
				if ( Constants.CInfoID_Email.equals(larrInfo[i].getAt(1)) )
				{
					lobjContact = fromServer(pobjContact, pdb, false);
					lobjInfo = fromServer(larrInfo[i]);
					lobjContact.info = new ContactInfo[] {lobjInfo};
					parrBuffer.add(lobjContact);
				}
			}
		}

		if ( larrSubs != null )
		{
			for ( i = 0; i < larrSubs.length; i++ )
			{
				innerGetFlatEmails(parrBuffer, larrSubs[i], pdb);
			}
		}
	}
}
