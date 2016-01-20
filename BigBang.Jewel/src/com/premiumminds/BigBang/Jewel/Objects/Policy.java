package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.GenericElement;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyExternWeekly;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyHistoryAutoVoiding;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyHistoryCreation;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyHistoryReactivation;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyHistoryValidation;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyHistoryVoiding;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyPendingReceipt;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyPendingValidation;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyPortfolioLive;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicyPortfolioNoReceipts;
import com.premiumminds.BigBang.Jewel.Listings.Policy.PolicySpecialShamir;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class Policy
	extends ProcessData
{
	public static class I
	{
		public static int NUMBER        =  0;
		public static int PROCESS       =  1;
		public static int COMPANY       =  2;
		public static int SUBLINE       =  3;
		public static int BEGINDATE     =  4;
		public static int DURATION      =  5;
		public static int FRACTIONING   =  6;
		public static int MATURITYDAY   =  7;
		public static int MATURITYMONTH =  8;
		public static int ENDDATE       =  9;
		public static int NOTES         = 10;
		public static int MEDIATOR      = 11;
		public static int CASESTUDY     = 12;
		public static int STATUS        = 13;
		public static int PREMIUM       = 14;
		public static int DOCUSHARE     = 15;
		public static int MIGRATIONID   = 16;
		public static int CLIENT        = 17;
		public static int PROFILE       = 18;
		public static int TOTALPREMIUM  = 19;
	}

    public static Policy GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Policy)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Policy), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Policy GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Policy)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Policy), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static GenericElement[] printReportPortfolioLive(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyPortfolioLive().doReport(parrParams);
	}

	public static GenericElement[] printReportPortfolioNoReceipts(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyPortfolioNoReceipts().doReport(parrParams);
	}

	public static GenericElement[] printReportPendingValidation(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyPendingValidation().doReport(parrParams);
	}

	public static GenericElement[] printReportPendingReceipt(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyPendingReceipt().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryCreation(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyHistoryCreation().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryValidation(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyHistoryValidation().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryVoiding(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyHistoryVoiding().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryAutoVoiding(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyHistoryAutoVoiding().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryReactivation(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyHistoryReactivation().doReport(parrParams);
	}

	public static GenericElement[] printImportReport(String[] parrParams)
		throws BigBangJewelException
	{
		FileImportSession lobjSession;

		if ( (parrParams == null) || (parrParams.length < 2) )
			throw new BigBangJewelException("Erro: Número de parâmetros inválido.");

		lobjSession = FileImportSession.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(parrParams[1]));
		return lobjSession.printReport(parrParams);
	}

	public static GenericElement[] printReportSpecialShamir(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicySpecialShamir().doReport(parrParams);
	}

	public static GenericElement[] printReportExternWeekly(String[] parrParams)
		throws BigBangJewelException
	{
		return new PolicyExternWeekly().doReport(parrParams);
	}

    private SubLine mrefSubLine;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefSubLine = SubLine.GetInstance(getNameSpace(), (UUID)getAt(3));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

    public String AfterSave()
    	throws JewelEngineException
    {
    	if ( mrefSubLine == null )
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
	}

    public String GetObjectFootprint()
    	throws BigBangJewelException
    {
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;
        String lstrResult;
        boolean b;

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {Constants.FKPolicy_In_Object},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lstrResult = "<Sem unidades de risco.>";
		b = false;
		try
		{
			while ( lrsObjects.next() )
			{
				if ( b )
				{
					lstrResult = "<Várias unidades de risco.>";
					break;
				}
				lstrResult = PolicyObject.GetInstance(getNameSpace(), lrsObjects).getLabel();
				b = true;
			}
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

		return lstrResult;
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
		larrParams[0] = Constants.ObjID_Policy;
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
		larrParams[0] = Constants.ObjID_Policy;
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

    public PolicyCoInsurer[] GetCurrentCoInsurers()
    	throws BigBangJewelException
    {
		ArrayList<PolicyCoInsurer> larrAux;
		IEntity lrefPolicyCoInsurers;
        MasterDB ldb;
        ResultSet lrsCoInsurers;

		larrAux = new ArrayList<PolicyCoInsurer>();

		try
		{
			lrefPolicyCoInsurers = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_PolicyCoInsurer)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoInsurers = lrefPolicyCoInsurers.SelectByMembers(ldb, new int[] {Constants.FKPolicy_In_CoInsurer},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoInsurers.next() )
				larrAux.add(PolicyCoInsurer.GetInstance(getNameSpace(), lrsCoInsurers));
		}
		catch (BigBangJewelException e)
		{
			try { lrsCoInsurers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsCoInsurers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoInsurers.close();
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

		return larrAux.toArray(new PolicyCoInsurer[larrAux.size()]);
    }

    public PolicyCoverage[] GetCurrentCoverages()
    	throws BigBangJewelException
    {
		PolicyCoverage[] larrAux;
        MasterDB ldb;

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
			larrAux = GetCurrentCoverages(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

		return larrAux;
    }

    public PolicyCoverage[] GetCurrentCoverages(SQLServer pdb)
    	throws BigBangJewelException
    {
		ArrayList<PolicyCoverage> larrAux;
		IEntity lrefPolicyCoverages;
        ResultSet lrsCoverages;

		larrAux = new ArrayList<PolicyCoverage>();

		try
		{
			lrefPolicyCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyCoverage)); 
			lrsCoverages = lrefPolicyCoverages.SelectByMembers(pdb, new int[] {Constants.FKPolicy_In_PolicyCoverage},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoverages.next() )
				larrAux.add(PolicyCoverage.GetInstance(getNameSpace(), lrsCoverages));
		}
		catch (BigBangJewelException e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new PolicyCoverage[larrAux.size()]);
    }

    public PolicyObject[] GetCurrentObjects()
    	throws BigBangJewelException
    {
		PolicyObject[] larrAux;
        MasterDB ldb;

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
			larrAux = GetCurrentObjects(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

		return larrAux;
    }

    public PolicyObject[] GetCurrentObjects(SQLServer pdb)
    	throws BigBangJewelException
    {
		ArrayList<PolicyObject> larrAux;
		IEntity lrefObjects;
        ResultSet lrsObjects;

		larrAux = new ArrayList<PolicyObject>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject)); 
			lrsObjects = lrefObjects.SelectByMembers(pdb, new int[] {Constants.FKPolicy_In_Object},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(PolicyObject.GetInstance(getNameSpace(), lrsObjects));
		}
		catch (BigBangJewelException e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new PolicyObject[larrAux.size()]);
    }

    public PolicyExercise[] GetCurrentExercises()
    	throws BigBangJewelException
    {
		PolicyExercise[] larrAux;
        MasterDB ldb;

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
			larrAux = GetCurrentExercises(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

		return larrAux;
    }

    public PolicyExercise[] GetCurrentExercises(SQLServer pdb)
    	throws BigBangJewelException
    {
		ArrayList<PolicyExercise> larrAux;
		IEntity lrefExercises;
        ResultSet lrsExercises;

		larrAux = new ArrayList<PolicyExercise>();

		try
		{
			lrefExercises = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyExercise)); 
			lrsExercises = lrefExercises.SelectByMembers(pdb, new int[] {Constants.FKPolicy_In_Exercise},
					new java.lang.Object[] {getKey()}, new int[] {-PolicyExercise.I.STARTDATE});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsExercises.next() )
				larrAux.add(PolicyExercise.GetInstance(getNameSpace(), lrsExercises));
		}
		catch (BigBangJewelException e)
		{
			try { lrsExercises.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsExercises.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsExercises.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new PolicyExercise[larrAux.size()]);
    }

    public PolicyValue[] GetCurrentValues()
    	throws BigBangJewelException
    {
		PolicyValue[] larrAux;
        MasterDB ldb;

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
			larrAux = GetCurrentValues(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

		return larrAux;
    }

    public PolicyValue[] GetCurrentValues(SQLServer pdb)
    	throws BigBangJewelException
    {
		ArrayList<PolicyValue> larrAux;
		IEntity lrefContactInfo;
        ResultSet lrsInfo;

		larrAux = new ArrayList<PolicyValue>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyValue)); 
			lrsInfo = lrefContactInfo.SelectByMembers(pdb, new int[] {Constants.FKPolicy_In_Value},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(PolicyValue.GetInstance(getNameSpace(), lrsInfo));
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

		return larrAux.toArray(new PolicyValue[larrAux.size()]);
    }

    public PolicyValue[] GetCurrentKeyedValues(UUID pidObject, UUID pidExercise)
    	throws BigBangJewelException
    {
		ArrayList<PolicyValue> larrAux;
		IEntity lrefContactInfo;
        MasterDB ldb;
        ResultSet lrsInfo;

		larrAux = new ArrayList<PolicyValue>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyValue)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefContactInfo.SelectByMembers(ldb, new int[] {Constants.FKPolicy_In_Value, Constants.FKObject_In_Value,
					Constants.FKExercise_In_Value}, new java.lang.Object[] {getKey(), pidObject, pidExercise}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(PolicyValue.GetInstance(getNameSpace(), lrsInfo));
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

		return larrAux.toArray(new PolicyValue[larrAux.size()]);
    }

    public SubPolicy[] GetCurrentSubPoliciesForDebit(UUID pidFrac, Timestamp pdtFrom)
    	throws BigBangJewelException
    {
		ArrayList<SubPolicy> larrAux;
		IEntity lrefContactInfo;
        MasterDB ldb;
        ResultSet lrsInfo;
        SubPolicy lobjAux;

		larrAux = new ArrayList<SubPolicy>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Jewel.Petri.Constants.ObjID_PNProcess)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefContactInfo.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKScript_In_Process,
					Jewel.Petri.Constants.FKParent_In_Process}, new java.lang.Object[] {Constants.ProcID_SubPolicy,
					GetProcessID()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
			{
				lobjAux = (SubPolicy)PNProcess.GetInstance(getNameSpace(), lrsInfo).GetData();
				if ( ((pidFrac == null) || (pidFrac.equals(lobjAux.getAt(5)))) &&
						((lobjAux.getAt(4) == null) || ((Timestamp)lobjAux.getAt(4)).after(pdtFrom)) )
					larrAux.add(lobjAux);
			}
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

		return larrAux.toArray(new SubPolicy[larrAux.size()]);
    }

    public SubLine GetSubLine()
    {
    	return mrefSubLine;
    }

    public Company GetCompany()
    	throws BigBangJewelException
    {
		return Company.GetInstance(getNameSpace(), (UUID)getAt(2));
    }

    public Client GetClient()
    	throws BigBangJewelException
    {
    	IProcess lobjProcess;

    	try
    	{
			lobjProcess = PNProcess.GetInstance(getNameSpace(), GetProcessID());
	    	return (Client)lobjProcess.GetParent().GetData();
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}
    }

    public Mediator getMediator()
    	throws BigBangJewelException
    {
    	if ( getAt(11) == null )
    		return GetClient().getMediator();

    	return Mediator.GetInstance(getNameSpace(), (UUID)getAt(11));
    }

    public UUID getProfile()
    	throws BigBangJewelException
    {
    	if ( getAt(18) == null )
    		return GetClient().getProfile();

    	return (UUID)getAt(18);
    }

    public BigDecimal getPercentOverride()
    	throws BigBangJewelException
    {
    	BigDecimal ldblResult;

    	ldblResult = getMediator().GetCurrentPolicyException(getKey());

    	if ( ldblResult == null )
    		ldblResult = getMediator().GetCurrentClientException(GetClient().getKey());

    	return ldblResult;
    }

    public DetailedBase GetDetailedObject()
    	throws BigBangJewelException
    {
    	return GetSubLine().GetDetailedObject(this, null);
    }

    public DetailedBase GetDetailedObject(SubPolicy pobjSubPolicy)
    	throws BigBangJewelException
    {
    	return GetSubLine().GetDetailedObject(this, pobjSubPolicy);
    }
    
    /**
	 * This method sets the sales premium of the policy. It's called 
	 * when a continuing receipt is created.
	 * 
	 * @whotoblame jcamilo
	 */
    public void SetSalesPremium(SQLServer pdb, Object salesPremium) 
    		throws BigBangJewelException {
    	try {
    		BigDecimal newPremium = calculateValueWithFractioning(salesPremium);
    		internalSetAt(I.PREMIUM, newPremium);
    		SaveToDb(pdb);
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
	}
    
	/**
	 * This method sets the total premium of the policy. It's called 
	 * when a continuing receipt is created.
	 * 
	 * @whotoblame jcamilo
	 */
    public void SetTotalPremium(SQLServer pdb, Object totalPremium) 
    		throws BigBangJewelException {
    	try {
    		BigDecimal newPremium = calculateValueWithFractioning(totalPremium);
    		internalSetAt(I.TOTALPREMIUM, newPremium);
    		SaveToDb(pdb);
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
	}
    
    /**
	 * This method calculates the new value for a premium, according to the type of fractioning
	 * of the policy
	 * 
	 * @whotoblame jcamilo
	 */
    private BigDecimal calculateValueWithFractioning(Object premium) 
    	throws BigBangJewelException {
    	
    	// Previous verifications for mandatory data for calculations
    	if (this.getAt(I.FRACTIONING) == null) {
    		throw new BigBangJewelException("Erro no update de prémios de " +
    				"apólice associada ao recibo: o tipo de fraccionamento não está definido na apólice.");
    	}
		
		if (premium == null) {
			throw new BigBangJewelException("Erro no update de prémios de " +
    				"apólice associada ao recibo: o recibo não tem prémio definido.");
		}
		
		// Defines the coefficient according to the policy fractioning
		int coefficient;
		UUID fractioning = (UUID) this.getAt(I.FRACTIONING);
		if (Constants.FracID_Month.equals(fractioning)) {
			coefficient = 1;
		} else if (Constants.FracID_Semester.equals(fractioning)) {
			coefficient = 2;
		} else if (Constants.FracID_Quarter.equals(fractioning)) {
			coefficient = 4;
		} else if (Constants.FracID_Month.equals(fractioning)) {
			coefficient = 12;
		} else {
			throw new BigBangJewelException("Erro no update de prémios de apólice associada ao recibo: " +
					"o tipo de fraccionamento não foi considerado no cálculo de coeficiente.");
		}
		
		return ((BigDecimal) premium).multiply(new BigDecimal(coefficient));
	}

}
