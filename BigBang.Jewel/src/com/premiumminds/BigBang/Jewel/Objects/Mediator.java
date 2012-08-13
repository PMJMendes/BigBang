package com.premiumminds.BigBang.Jewel.Objects;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.MediatorBase;

public class Mediator
	extends ObjectBase
{
	public static class I
	{
		public static int NAME         =  0;
		public static int ISPNUMBER    =  1;
		public static int FISCALNUMBER =  2;
		public static int BANKINGID    =  3;
		public static int PROFILE      =  4;
		public static int ADDRESS1     =  5;
		public static int ADDRESS2     =  6;
		public static int ZIPCODE      =  7;
		public static int MIGRATIONID  =  8;
		public static int PERCENT      =  9;
		public static int CALCCLASS    = 10;
		public static int HASRETENTION = 11;
	}

    public static Mediator GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Mediator)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Mediator), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Mediator GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Mediator)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Mediator), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private Class<?> mrefClass;
    private Constructor<?> mrefConst;

	public void Initialize()
		throws JewelEngineException
	{
	}

    public Contact[] GetCurrentContacts()
    	throws BigBangJewelException
    {
		ArrayList<Contact> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContacts;
        MasterDB ldb;
        ResultSet lrsContacts;

		larrAux = new ArrayList<Contact>();

		larrMembers = new int[2];
		larrMembers[0] = Constants.FKOwnerType_In_Contact;
		larrMembers[1] = Constants.FKOwner_In_Contact;
		larrParams = new java.lang.Object[2];
		larrParams[0] = Constants.ObjID_Mediator;
		larrParams[1] = getKey();

		try
		{
			lrefContacts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Contact)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsContacts = lrefContacts.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsContacts.next() )
				larrAux.add(Contact.GetInstance(getNameSpace(), lrsContacts));
		}
		catch (BigBangJewelException e)
		{
			try { lrsContacts.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsContacts.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsContacts.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new Contact[larrAux.size()]);
    }

    public Document[] GetCurrentDocs()
    	throws BigBangJewelException
    {
		ArrayList<Document> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefDocuments;
        MasterDB ldb;
        ResultSet lrsDocuments;

		larrAux = new ArrayList<Document>();

		larrMembers = new int[2];
		larrMembers[0] = Constants.FKOwnerType_In_Document;
		larrMembers[1] = Constants.FKOwner_In_Document;
		larrParams = new java.lang.Object[2];
		larrParams[0] = Constants.ObjID_Mediator;
		larrParams[1] = getKey();

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Document)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDocuments = lrefDocuments.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDocuments.next() )
				larrAux.add(Document.GetInstance(getNameSpace(), lrsDocuments));
		}
		catch (BigBangJewelException e)
		{
			try { lrsDocuments.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDocuments.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDocuments.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new Document[larrAux.size()]);
    }

    public MediatorDeal[] GetCurrentDeals()
        throws BigBangJewelException
    {
		ArrayList<MediatorDeal> larrAux;
		IEntity lrefDeals;
        MasterDB ldb;
        ResultSet lrsDeals;

		larrAux = new ArrayList<MediatorDeal>();

		try
		{
			lrefDeals = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MediatorDeal)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDeals = lrefDeals.SelectByMembers(ldb, new int[] {MediatorDeal.I.MEDIATOR},
					new java.lang.Object[] {getKey()}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDeals.next() )
				larrAux.add(MediatorDeal.GetInstance(getNameSpace(), lrsDeals));
		}
		catch (BigBangJewelException e)
		{
			try { lrsDeals.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDeals.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDeals.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new MediatorDeal[larrAux.size()]);
    }

    public UUID getProfile()
    {
    	return (UUID)getAt(I.PROFILE);
    }

    public BigDecimal getPercent()
    {
    	return (BigDecimal)getAt(I.PERCENT);
    }

    public boolean getHasRetention()
    {
    	return (Boolean)getAt(I.HASRETENTION);
    }

    public BigDecimal GetCurrentDealFor(UUID pidSubLine)
        throws BigBangJewelException
    {
		MediatorDeal lobjAux;
		IEntity lrefDeals;
        MasterDB ldb;
        ResultSet lrsDeals;

		lobjAux = null;

		try
		{
			lrefDeals = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MediatorDeal)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDeals = lrefDeals.SelectByMembers(ldb, new int[] {MediatorDeal.I.MEDIATOR, MediatorDeal.I.SUBLINE},
					new java.lang.Object[] {getKey(), pidSubLine}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			if ( lrsDeals.next() )
				lobjAux = MediatorDeal.GetInstance(getNameSpace(), lrsDeals);
		}
		catch (BigBangJewelException e)
		{
			try { lrsDeals.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDeals.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDeals.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		if ( lobjAux == null )
			return null;

		return (BigDecimal)lobjAux.getAt(MediatorDeal.I.PERCENT);
    }

    public MediatorBase GetDetailedObject(Receipt pobjReceipt)
    	throws BigBangJewelException
    {
    	if ( getAt(I.CALCCLASS) == null )
    		return null;

		try
		{
			if ( mrefClass == null )
				mrefClass = Class.forName(((String)getAt(I.CALCCLASS)).replaceAll("MADDS", "Jewel"));
			if ( mrefConst == null )
				mrefConst = mrefClass.getConstructor(new Class<?>[] {Receipt.class});
			return (MediatorBase)mrefConst.newInstance(new java.lang.Object[] {pobjReceipt});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
    }
}
