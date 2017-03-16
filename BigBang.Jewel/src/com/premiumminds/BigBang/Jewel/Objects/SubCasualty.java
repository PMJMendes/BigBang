package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class SubCasualty
	extends ProcessData
{
	public static class I
	{
		public static int NUMBER          =  0;
		public static int PROCESS         =  1;
		public static int POLICY          =  2;
		public static int SUBPOLICY       =  3;
		public static int INSURERPROCESS  =  4;
		public static int DESCRIPTION     =  5;
		public static int NOTES           =  6;
		public static int BODY            =  7;
		public static int FROM            =  8;
		public static int SUBJECT         =  9;
		public static int LIMITDATE       = 10;
		public static int HASJUDICIAL     = 11;
		public static int REVIEWER        = 12;
		public static int REVIEWDATE      = 13;
		public static int POLICYOBJECT    = 14;
		public static int SUBPOLICYOBJECT = 15;
		public static int GENERICOBJECT   = 16;
		public static int CASUALTY        = 17;
		public static int SERVICECENTER   = 18;
	}

    public static SubCasualty GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualty), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubCasualty GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualty), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID GetProcessID()
	{
		return (UUID)getAt(I.PROCESS);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(I.PROCESS, pidProcess);
	}

	public String GetObjectName()
		throws BigBangJewelException
	{
		if ( getAt(14) != null )
			return PolicyObject.GetInstance(getNameSpace(), (UUID)getAt(14)).getLabel();
		
		if ( getAt(15) != null )
			return SubPolicyObject.GetInstance(getNameSpace(), (UUID)getAt(15)).getLabel();
		
		return (String)getAt(16);
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
		larrParams[0] = Constants.ObjID_SubCasualty;
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
		larrParams[0] = Constants.ObjID_SubCasualty;
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

    public SubCasualtyItem[] GetCurrentItems()
    	throws BigBangJewelException
    {
		ArrayList<SubCasualtyItem> larrAux;
		IEntity lrefItems;
        MasterDB ldb;
        ResultSet lrsItems;

		larrAux = new ArrayList<SubCasualtyItem>();

		try
		{
			lrefItems = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualtyItem)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsItems = lrefItems.SelectByMembers(ldb, new int[] {SubCasualtyItem.I.SUBCASUALTY},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsItems.next() )
				larrAux.add(SubCasualtyItem.GetInstance(getNameSpace(), lrsItems));
		}
		catch (BigBangJewelException e)
		{
			try { lrsItems.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsItems.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsItems.close();
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

		return larrAux.toArray(new SubCasualtyItem[larrAux.size()]);
    }
    
    public SubCasualtyInsurerRequest[] GetCurrentInsurerRequests() throws BigBangJewelException {
    	ArrayList<SubCasualtyInsurerRequest> iReqs;
    	IEntity reqEntity;
        MasterDB ldb;
        ResultSet resultSet;

        iReqs = new ArrayList<SubCasualtyInsurerRequest>();

    	try {
    		reqEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualtyInsurerRequest)); 
    		ldb = new MasterDB();
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		resultSet = reqEntity.SelectByMembers(ldb, new int[] {SubCasualtyInsurerRequest.I.SUBCASUALTY},
    				new java.lang.Object[] {getKey()}, new int[0]);
    	} catch (Throwable e) {
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		while (resultSet.next()) {
    			iReqs.add(SubCasualtyInsurerRequest.GetInstance(getNameSpace(), resultSet));
    		}
    	} catch (BigBangJewelException e) {
    		try { 
    			resultSet.close(); 
    		} catch (Throwable e1) {
    			
    		}
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw e;
    	} catch (Throwable e) {
    		try { 
    			resultSet.close(); 
    		} catch (Throwable e1) {
    			
    		}
    		
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}

    	try {
    		resultSet.close();
    	} catch (Throwable e) {
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		ldb.Disconnect();
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}

    	return iReqs.toArray(new SubCasualtyInsurerRequest[iReqs.size()]);
    }

    public Casualty GetCasualty()
    	throws BigBangJewelException
    {
    	return Casualty.GetInstance(getNameSpace(), (UUID)getAt(I.CASUALTY));
    }

    public Policy GetPolicy()
    	throws BigBangJewelException
    {
    	if ( getAt(I.POLICY) != null )
    		return Policy.GetInstance(getNameSpace(), (UUID)getAt(I.POLICY));

    	return null;
    }

    public SubPolicy GetSubPolicy()
    	throws BigBangJewelException
    {
    	if ( getAt(I.SUBPOLICY) != null )
    		return SubPolicy.GetInstance(getNameSpace(), (UUID)getAt(I.SUBPOLICY));

    	return null;
    }

    public Policy getAbsolutePolicy()
    	throws BigBangJewelException
    {
    	Policy lobjAux;
    	SubPolicy lobjSPAux;

    	lobjAux = GetPolicy();
    	if ( lobjAux != null )
    		return lobjAux;

    	lobjSPAux = GetSubPolicy();
    	if ( lobjSPAux != null )
    		return lobjSPAux.GetOwner();

    	return null;
    }

	public SubLine GetSubLine()
		throws BigBangJewelException
	{
		return getAbsolutePolicy().GetSubLine();
	}

	public Company GetCompany()
		throws BigBangJewelException
	{
		return getAbsolutePolicy().GetCompany();
	}

    public Client GetClient()
    	throws BigBangJewelException
    {
    	Policy lobjAux;
    	SubPolicy lobjSPAux;

    	lobjAux = GetPolicy();
    	if ( lobjAux != null )
    		return lobjAux.GetClient();

    	lobjSPAux = GetSubPolicy();
    	if ( lobjSPAux != null )
    		return lobjSPAux.GetClient();

    	return null;
    }

    public OtherEntity GetServiceCenter()
    	throws BigBangJewelException
    {
    	if ( getAt(I.SERVICECENTER) != null )
    		return OtherEntity.GetInstance(getNameSpace(), (UUID)getAt(I.SERVICECENTER));
    	
    	return null;
    }
}
