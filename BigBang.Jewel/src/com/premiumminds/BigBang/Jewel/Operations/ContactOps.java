package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;

public class ContactOps
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public class ContactData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public class ContactInfoData
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public UUID midType;
			public String mstrValue;
		}

		public UUID mid;
		public String mstrName;
		public UUID midOwnerType;
		public UUID midOwnerId;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
		public ContactInfoData[] marrInfo;
		public ContactData[] marrSubContacts;
		public ContactData mobjPrevValues;
	}

	public ContactData[] marrCreate;
	public ContactData[] marrModify;
	public ContactData[] marrDelete;

	public void RunSubOp(MasterDB pdb, UUID pidOwnerType, UUID pidOwner)
		throws BigBangJewelException
	{
		int i;

		if ( marrCreate != null )
		{
			for ( i = 0; i < marrCreate.length; i++ )
			{
				if ( pidOwner == null )
					CreateContact(pdb, marrCreate[i]);
				else
					CreateContact(pdb, marrCreate[i], pidOwnerType, pidOwner);
			}
		}

		if ( marrModify != null )
		{
			for ( i = 0; i < marrModify.length; i++ )
				ModifyContact(pdb, marrModify[i]);
		}

		if ( marrDelete != null )
		{
			for ( i = 0; i < marrDelete.length; i++ )
				DeleteContact(pdb, marrDelete[i]);
		}
	}

	private void CreateContact(MasterDB pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		CreateContact(pdb, pobjData, pobjData.midOwnerType, pobjData.midOwnerId);
	}

	private void CreateContact(MasterDB pdb, ContactData pobjData, UUID pidOwnerType, UUID pidOwner)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		try
		{
			lobjAux.setAt(0, pobjData.mstrName);
			lobjAux.setAt(1, pidOwnerType);
			lobjAux.setAt(2, pidOwner);
			lobjAux.setAt(3, pobjData.mstrAddress1);
			lobjAux.setAt(4, pobjData.mstrAddress2);
			lobjAux.setAt(5, pobjData.midZipCode);
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].midType);
					lobjAuxInfo.setAt(2, pobjData.marrInfo[i].mstrValue);
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
		}

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				CreateContact(pdb, pobjData.marrSubContacts[i], Constants.ObjID_Contact, lobjAux.getKey());
		}

		pobjData.mid = lobjAux.getKey();
		pobjData.midOwnerType = pidOwnerType;
		pobjData.midOwnerId = pidOwner;
		pobjData.mobjPrevValues = null;
	}

	private void ModifyContact(MasterDB pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;
		Entity lrefContactInfo;
		ContactInfo[] larrCIAux;

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_ContactInfo));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

		pobjData.mobjPrevValues = new ContactData();

		pobjData.mobjPrevValues.mid = lobjAux.getKey();
		pobjData.mobjPrevValues.mstrName = (String)lobjAux.getAt(0);
		pobjData.mobjPrevValues.midOwnerType = (UUID)lobjAux.getAt(1);
		pobjData.mobjPrevValues.midOwnerId = (UUID)lobjAux.getAt(2);
		pobjData.mobjPrevValues.mstrAddress1 = (String)lobjAux.getAt(3);
		pobjData.mobjPrevValues.mstrAddress2 = (String)lobjAux.getAt(4);
		pobjData.mobjPrevValues.midZipCode = (UUID)lobjAux.getAt(5);
		pobjData.mobjPrevValues.marrSubContacts = null;
		pobjData.mobjPrevValues.mobjPrevValues = null;

		larrCIAux = lobjAux.getCurrentInfo();
		pobjData.mobjPrevValues.marrInfo = new ContactData.ContactInfoData[larrCIAux.length];
		for ( i = 0; i < larrCIAux.length; i++ )
		{
			pobjData.mobjPrevValues.marrInfo[i].midType = (UUID)larrCIAux[i].getAt(0);
			pobjData.mobjPrevValues.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(1);
			try
			{
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		try
		{
			lobjAux.setAt(0, pobjData.mstrName);
			lobjAux.setAt(1, pobjData.midOwnerType);
			lobjAux.setAt(2, pobjData.midOwnerId);
			lobjAux.setAt(3, pobjData.mstrAddress1);
			lobjAux.setAt(4, pobjData.mstrAddress2);
			lobjAux.setAt(5, pobjData.midZipCode);
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].midType);
					lobjAuxInfo.setAt(2, pobjData.marrInfo[i].mstrValue);
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
		}
	}

	private void DeleteContact(MasterDB pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		Entity lrefContacts;
		Entity lrefContactInfo;
		int i;
		Contact[] larrCAux;
		ContactInfo[] larrCIAux;

		try
		{
			lrefContacts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Contact));
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_ContactInfo));

			lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

			larrCAux = lobjAux.getCurrentSubContacts();
			pobjData.marrSubContacts = new ContactData[larrCAux.length];
			for ( i = 0; i < larrCAux.length; i++ )
			{
				pobjData.marrSubContacts[i] = new ContactData();
				pobjData.marrSubContacts[i].mid = larrCAux[i].getKey();
				DeleteContact(pdb, pobjData.marrSubContacts[i]);
			}

			larrCIAux = lobjAux.getCurrentInfo();
			pobjData.marrInfo = new ContactData.ContactInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i].midType = (UUID)larrCIAux[i].getAt(1);
				pobjData.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(2);
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.mstrName = (String)lobjAux.getAt(0);
			pobjData.midOwnerType = (UUID)lobjAux.getAt(1);
			pobjData.midOwnerId = (UUID)lobjAux.getAt(2);
			pobjData.mstrAddress1 = (String)lobjAux.getAt(3);
			pobjData.mstrAddress2 = (String)lobjAux.getAt(4);
			pobjData.midZipCode = (UUID)lobjAux.getAt(5);
			pobjData.mobjPrevValues = null;

			lrefContacts.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
