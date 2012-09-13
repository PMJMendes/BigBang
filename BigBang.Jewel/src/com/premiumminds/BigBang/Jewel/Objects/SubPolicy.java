package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class SubPolicy
	extends ProcessData
{
    public static SubPolicy GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubPolicy)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicy), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubPolicy GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubPolicy)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicy), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private Policy mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
		if ( GetProcessID() == null )
			return;

		try
		{
			mrefOwner = (Policy)PNProcess.GetInstance(getNameSpace(), GetProcessID()).GetParent().GetData();
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

    public String AfterSave()
    	throws JewelEngineException
    {
    	if ( mrefOwner == null )
    		Initialize();

        return "";
    }

	public UUID GetProcessID()
	{
		return (UUID)getAt(1);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(1, pidProcess);

    	if ( mrefOwner == null )
    	{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
    	}
	}

	public Client GetClient()
		throws BigBangJewelException
	{
		if ( getAt(2) == null )
			return null;

		return Client.GetInstance(getNameSpace(), (UUID)getAt(2));
	}

    public Policy GetOwner()
    {
    	if ( mrefOwner == null )
    	{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
    	}

    	return mrefOwner;
    }

    public Policy ResetOwner()
    	throws BigBangJewelException
    {
		try
		{
			mrefOwner = (Policy)PNProcess.GetInstance(getNameSpace(), GetProcessID()).GetParent().GetData();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return mrefOwner;
    }

    public Contact[] GetCurrentContacts()
    	throws BigBangJewelException
    {
		ArrayList<Contact> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefContactInfo;
        MasterDB ldb;
        ResultSet lrsInfo;

		larrAux = new ArrayList<Contact>();

		larrMembers = new int[2];
		larrMembers[0] = Constants.FKOwnerType_In_Contact;
		larrMembers[1] = Constants.FKOwner_In_Contact;
		larrParams = new java.lang.Object[2];
		larrParams[0] = Constants.ObjID_SubPolicy;
		larrParams[1] = getKey();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Contact)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefContactInfo.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
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
		larrParams[0] = Constants.ObjID_SubPolicy;
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

    public SubPolicyCoverage[] GetCurrentCoverages()
    	throws BigBangJewelException
    {
		ArrayList<SubPolicyCoverage> larrAux;
		IEntity lrefSubPolicyCoverages;
        MasterDB ldb;
        ResultSet lrsCoverages;

		larrAux = new ArrayList<SubPolicyCoverage>();

		try
		{
			lrefSubPolicyCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyCoverage)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages = lrefSubPolicyCoverages.SelectByMembers(ldb, new int[] {Constants.FKSubPolicy_In_SubPolicyCoverage},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoverages.next() )
				larrAux.add(SubPolicyCoverage.GetInstance(getNameSpace(), lrsCoverages));
		}
		catch (BigBangJewelException e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages.close();
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

		return larrAux.toArray(new SubPolicyCoverage[larrAux.size()]);
    }

    public SubPolicyObject[] GetCurrentObjects()
    	throws BigBangJewelException
    {
		ArrayList<SubPolicyObject> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<SubPolicyObject>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {Constants.FKSubPolicy_In_SubPolicyObject},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(SubPolicyObject.GetInstance(getNameSpace(), lrsObjects));
		}
		catch (BigBangJewelException e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
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

		return larrAux.toArray(new SubPolicyObject[larrAux.size()]);
    }

    public PolicyExercise[] GetCurrentExercises()
    	throws BigBangJewelException
    {
    	try
    	{
			return GetOwner().GetCurrentExercises();
		}
    	catch (BigBangJewelException e)
    	{
    		throw e;
		}
    	catch (Throwable e)
    	{
			throw new BigBangJewelException(e.getMessage(), e);
		}
    }

    public SubPolicyValue[] GetCurrentValues()
    	throws BigBangJewelException
    {
		ArrayList<SubPolicyValue> larrAux;
		IEntity lrefContactInfo;
        MasterDB ldb;
        ResultSet lrsInfo;

		larrAux = new ArrayList<SubPolicyValue>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyValue)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefContactInfo.SelectByMembers(ldb, new int[] {Constants.FKSubPolicy_In_SubPolicyValue},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(SubPolicyValue.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
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

		return larrAux.toArray(new SubPolicyValue[larrAux.size()]);
    }

    public SubPolicyValue[] GetCurrentKeyedValues(UUID pidObject, UUID pidExercise)
    	throws BigBangJewelException
    {
		ArrayList<SubPolicyValue> larrAux;
		IEntity lrefContactInfo;
        MasterDB ldb;
        ResultSet lrsInfo;

		larrAux = new ArrayList<SubPolicyValue>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyValue)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefContactInfo.SelectByMembers(ldb, new int[] {Constants.FKSubPolicy_In_SubPolicyValue,
					Constants.FKObject_In_SubPolicyValue, Constants.FKExercise_In_SubPolicyValue}, new java.lang.Object[] {getKey(),
					pidObject, pidExercise}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(SubPolicyValue.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
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

		return larrAux.toArray(new SubPolicyValue[larrAux.size()]);
    }

    public DetailedBase GetDetailedObject()
    	throws BigBangJewelException
    {
    	return GetOwner().GetDetailedObject(this);
    }
}
