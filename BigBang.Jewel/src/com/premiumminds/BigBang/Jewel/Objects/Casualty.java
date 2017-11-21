package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.GenericElement;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyExternAT;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyExternAuto;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyExternGeneral;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyHistoryClosing;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyHistoryCreation;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyHistoryMarkForReview;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyHistoryReport;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyOtherClosingTimes;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyPendingAnything;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyPendingReport;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyPendingReview;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtyProductivityMap;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtySinistralityMap;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualty.SubCasualtySpecialShamir;

public class Casualty
	extends ProcessData
{
	public static class I
	{
		public static int NUMBER       = 0;
		public static int PROCESS      = 1;
		public static int DATE         = 2;
		public static int DESCRIPTION  = 3;
		public static int NOTES        = 4;
		public static int CASESTUDY    = 5;
		public static int CLIENT       = 6;
		public static int PERCENTFAULT = 7;
		public static int FRAUD 	   = 8;
	}

    public static Casualty GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Casualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Casualty), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Casualty GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Casualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Casualty), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static GenericElement[] printReportSubPendingAnything(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyPendingAnything().doReport(parrParams);
	}

	public static GenericElement[] printReportSubPendingReport(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyPendingReport().doReport(parrParams);
	}

	public static GenericElement[] printReportSubPendingReview(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyPendingReview().doReport(parrParams);
	}

	public static GenericElement[] printReportSubHistoryCreation(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyHistoryCreation().doReport(parrParams);
	}

	public static GenericElement[] printReportSubHistoryReport(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyHistoryReport().doReport(parrParams);
	}

	public static GenericElement[] printReportSubHistoryMarkForReview(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyHistoryMarkForReview().doReport(parrParams);
	}

	public static GenericElement[] printReportSubHistoryClosing(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyHistoryClosing().doReport(parrParams);
	}

	public static GenericElement[] printReportExternGeneral(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyExternGeneral().doReport(parrParams);
	}

	public static GenericElement[] printReportExternAT(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyExternAT().doReport(parrParams);
	}

	public static GenericElement[] printReportExternAuto(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyExternAuto().doReport(parrParams);
	}

	public static GenericElement[] printReportSubOtherClosingTimes(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyOtherClosingTimes().doReport(parrParams);
	}

	public static GenericElement[] printReportSubSpecialShamir(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtySpecialShamir().doReport(parrParams);
	}
	
	public static GenericElement[] printReportSinistralityMap(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtySinistralityMap().doReport(parrParams);
	}
	
	public static GenericElement[] printReportProductivityMap(String[] parrParams)
		throws BigBangJewelException
	{
		return new SubCasualtyProductivityMap().doReport(parrParams);
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

    public SubCasualty GetFirstSubCasualty()
    	throws BigBangJewelException
    {
		SubCasualty lobjAux;
		IEntity lrefSubs;
        MasterDB ldb;
        ResultSet lrsSubs;

		lobjAux = null;

		try
		{
			lrefSubs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubs = lrefSubs.SelectByMembers(ldb, new int[] {SubCasualty.I.CASUALTY}, new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			if ( lrsSubs.next() )
				lobjAux = SubCasualty.GetInstance(getNameSpace(), lrsSubs);
		}
		catch (BigBangJewelException e)
		{
			try { lrsSubs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsSubs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubs.close();
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

		return lobjAux;
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
		larrParams[0] = Constants.ObjID_Casualty;
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
		larrParams[0] = Constants.ObjID_Casualty;
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

    public Client GetClient()
    	throws BigBangJewelException
    {
    	return Client.GetInstance(getNameSpace(), (UUID)getAt(I.CLIENT));
    }
}
