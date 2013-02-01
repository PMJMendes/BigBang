package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class Contact
	extends ObjectBase
{
	public static class I
	{
		public static int NAME        = 0;
		public static int OWNERTYPE   = 1;
		public static int OWNER       = 2;
		public static int ADDRESS1    = 3;
		public static int ADDRESS2    = 4;
		public static int ZIPCODE     = 5;
		public static int TYPE        = 6;
		public static int MIGRATIONID = 7;
	}

    public static Contact GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (Contact)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Contact), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Contact GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Contact)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Contact), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private ObjectBase mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID getOwnerType()
	{
		return (UUID)getAt(1);
	}

	public UUID getOwnerID()
	{
		return (UUID)getAt(2);
	}

    public ObjectBase getOwner()
    	throws BigBangJewelException
    {
    	if ( mrefOwner == null )
    	{
    		try
			{
    			mrefOwner = Engine.GetWorkInstance(Engine.FindEntity(getNameSpace(), (UUID)getAt(1)), (UUID)getAt(2));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
    	}

    	return mrefOwner;
    }

    public ContactInfo[] getCurrentInfo()
    	throws BigBangJewelException
    {
    	MasterDB ldb;
    	ContactInfo[] larrResult;

    	try
    	{
			ldb = new MasterDB();
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	try
    	{
    		larrResult = getCurrentInfo(ldb);
    	}
    	catch (BigBangJewelException e)
    	{
    		try {ldb.Disconnect();} catch (Throwable e1) {}
    		throw e;
		}
    	catch (Throwable e)
    	{
    		try {ldb.Disconnect();} catch (Throwable e1) {}
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	try
    	{
			ldb.Disconnect();
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	return larrResult;
    }

    public Contact[] getCurrentSubContacts()
    	throws BigBangJewelException
    {
    	MasterDB ldb;
    	Contact[] larrResult;

    	try
    	{
			ldb = new MasterDB();
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	try
    	{
    		larrResult = getCurrentSubContacts(ldb);
    	}
    	catch (BigBangJewelException e)
    	{
    		try {ldb.Disconnect();} catch (Throwable e1) {}
    		throw e;
		}
    	catch (Throwable e)
    	{
    		try {ldb.Disconnect();} catch (Throwable e1) {}
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	try
    	{
			ldb.Disconnect();
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	return larrResult;
    }

	public ContactInfo[] getCurrentInfo(SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<ContactInfo> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContactInfo;
        ResultSet lrsInfo;

		larrAux = new ArrayList<ContactInfo>();

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKOwner_In_ContactInfo;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ContactInfo)); 
			lrsInfo = lrefContactInfo.SelectByMembers(pdb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(ContactInfo.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new ContactInfo[larrAux.size()]);
	}

	public Contact[] getCurrentSubContacts(SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<Contact> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContactInfo;
        ResultSet lrsInfo;

		larrAux = new ArrayList<Contact>();

		larrMembers = new int[2];
		larrMembers[0] = Constants.FKOwnerType_In_Contact;
		larrMembers[1] = Constants.FKOwner_In_Contact;
		larrParams = new java.lang.Object[2];
		larrParams[0] = Constants.ObjID_Contact;
		larrParams[1] = getKey();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Contact)); 
			lrsInfo = lrefContactInfo.SelectByMembers(pdb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(Contact.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new Contact[larrAux.size()]);
	}

	public ContactInfo findAddress(String pstrAddress)
		throws BigBangJewelException
	{
		ContactInfo[] larrInfo;
		Contact[] larrSubs;
		ContactInfo lobjAux;
		int i;

		if ( pstrAddress == null )
			return null;
		pstrAddress = pstrAddress.trim();

		larrInfo = getCurrentInfo();
		if ( larrInfo != null )
		{
			for ( i = 0; i < larrInfo.length; i++ )
			{
				if ( Constants.CInfoID_Email.equals(larrInfo[i].getAt(ContactInfo.I.TYPE)) &&
						(pstrAddress.equalsIgnoreCase((String)larrInfo[i].getAt(ContactInfo.I.VALUE))) )
					return larrInfo[i];
			}
		}

		larrSubs = getCurrentSubContacts();
		if ( larrSubs != null )
		{
			for ( i = 0; i < larrSubs.length; i++ )
			{
				lobjAux = larrSubs[i].findAddress(pstrAddress);
				if ( lobjAux != null )
					return lobjAux;
			}
		}

		return null;
	}

	public Contact findName(String pstrName)
		throws BigBangJewelException
	{
		Contact[] larrSubs;
		Contact lobjAux;
		int i;

		if ( pstrName == null )
			return null;
		pstrName = pstrName.trim();

		if ( pstrName.equalsIgnoreCase(getLabel()) )
			return this;

		larrSubs = getCurrentSubContacts();
		if ( larrSubs != null )
		{
			for ( i = 0; i < larrSubs.length; i++ )
			{
				lobjAux = larrSubs[i].findName(pstrName);
				if ( lobjAux != null )
					return lobjAux;
			}
		}

		return null;
	}
}
