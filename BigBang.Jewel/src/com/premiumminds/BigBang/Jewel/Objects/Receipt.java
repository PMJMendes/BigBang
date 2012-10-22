package com.premiumminds.BigBang.Jewel.Objects;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.pdfbox.pdmodel.PDDocument;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryImage;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryInsurerAccounting;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryMediatorAccounting;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryPayment;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryPaymentAcct;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistorySendPaymentNotice;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryValidation;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptHistoryAutoValidation;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingCreateDASRequest;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingCreateSignatureRequest;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingImage;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingInsurerAccounting;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingMediatorAccounting;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingPayment;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingPendingPaymentReturn;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingSendPayment;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingSendPaymentNotice;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingSendReceipt;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptPendingValidation;
import com.premiumminds.BigBang.Jewel.SysObjects.MediatorBase;

public class Receipt
	extends ProcessData
{
	public static class I
	{
		public static int NUMBER            =  0;
		public static int TYPE              =  1;
		public static int PROCESS           =  2;
		public static int TOTALPREMIUM      =  3;
		public static int COMMERCIALPREMIUM =  4;
		public static int COMMISSIONS       =  5;
		public static int RETROCESSIONS     =  6;
		public static int FAT               =  7;
		public static int ISSUEDATE         =  8;
		public static int MATURITYDATE      =  9;
		public static int ENDDATE           = 10;
		public static int DUEDATE           = 11;
		public static int MEDIATOR          = 12;
		public static int NOTES             = 13;
		public static int DESCRIPTION       = 14;
		public static int RETURNTEXT        = 15;
		public static int MIGRATIONID       = 16;
	}

    public static Receipt GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Receipt)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Receipt), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Receipt GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Receipt)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Receipt), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static GenericElement[] printReportPendingPayment(String[] parrParams)
		throws BigBangJewelException
	{
		return new ReceiptPendingPayment().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingValidation(String[] parrParams)
		throws BigBangJewelException
	{
		return new ReceiptPendingValidation().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingImage(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingImage().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingCreateSignatureRequest(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingCreateSignatureRequest().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingCreateDASRequest(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingCreateDASRequest().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingPaymentReturn(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingPendingPaymentReturn().doReport(parrParams);
	}

	public static GenericElement[] printReportPendingReturnToInsurer(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingReturnToInsurer().doReport(parrParams);
	}
	
	
	public static GenericElement[] printReportPendingSendPaymentNotice(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingSendPaymentNotice().doReport(parrParams);
	}
	
	
	public static GenericElement[] printReportPendingSendPayment(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingSendPayment().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingMediatorAccounting(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingMediatorAccounting().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingInsurerAccounting(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptPendingInsurerAccounting().doReport(parrParams);
	}
	
	public static GenericElement[] printReportPendingSendReceipt(String[] parrParams)
			throws BigBangJewelException
		{
			return new ReceiptPendingSendReceipt().doReport(parrParams);
		}

	
	public static GenericElement[] printReportHistoryPayment(String[] parrParams)
		throws BigBangJewelException
	{
		return new ReceiptHistoryPayment().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistoryImage(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptHistoryImage().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistoryInsurerAccounting(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptHistoryInsurerAccounting().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistoryMediatorAccounting(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptHistoryMediatorAccounting().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistorySendPaymentNotice(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptHistorySendPaymentNotice().doReport(parrParams);
	}

	public static GenericElement[] printReportHistoryPaymentAcct(String[] parrParams)
		throws BigBangJewelException
	{
		return new ReceiptHistoryPaymentAcct().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistoryValidation(String[] parrParams)
		throws BigBangJewelException
	{
		return new ReceiptHistoryValidation().doReport(parrParams);
	}
	
	public static GenericElement[] printReportHistoryAutoValidation(String[] parrParams)
			throws BigBangJewelException
	{
		return new ReceiptHistoryAutoValidation().doReport(parrParams);
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

    private IProcess lrefProcess;
    private ILog lrefPayment;

	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID GetProcessID()
	{
		return (UUID)getAt(2);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(2, pidProcess);
	}

	public IProcess getProcess()
		throws BigBangJewelException
	{
		if ( GetProcessID() == null )
			return null;

		if ( lrefProcess == null )
		{
			try
			{
				lrefProcess = PNProcess.GetInstance(getNameSpace(), GetProcessID());
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		return lrefProcess;
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
		larrParams[0] = Constants.ObjID_Receipt;
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
		larrParams[0] = Constants.ObjID_Receipt;
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

    public boolean canAutoValidate()
    	throws BigBangJewelException
    {
    	UUID lidProfile;
    	IProcess lobjProcess;
    	Client lobjClient;

    	lidProfile = (UUID)getAbsolutePolicy().getProfile();

    	if ( lidProfile == null )
    	{
	    	try
	    	{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), GetProcessID());
	
		    	if ( Constants.ProcID_SubPolicy.equals(lobjProcess.GetParent().GetScriptID()) )
		    		lobjClient = Client.GetInstance(getNameSpace(), (UUID)((SubPolicy)lobjProcess.GetParent().GetData()).getAt(2));
		    	else
		    		lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
	    	lidProfile = (UUID)lobjClient.getAt(9);
    	}

    	if ( Constants.ProfID_VIP.equals(lidProfile) )
    		return false;

    	if ( Constants.RecType_Continuing.equals(getAt(1)) )
    		return true;

    	return false;
    }

    public boolean isReverseCircuit()
    {
    	if ( Constants.RecType_Reversal.equals(getAt(1)) )
    		return true;

    	if ( Constants.RecType_Casualty.equals(getAt(1)) )
    		return true;

    	return false;
    }

    public boolean isForCasualties()
    {
    	if ( Constants.RecType_Backcharge.equals(getAt(1)) )
    		return true;

    	if ( Constants.RecType_Casualty.equals(getAt(1)) )
    		return true;

    	return false;
    }

    public String getReceiptType()
    	throws BigBangJewelException
    {
    	try
    	{
			return (String)Engine.GetWorkInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_ReceiptType), (UUID)getAt(1)).getAt(1);
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}
    }

    public SubPolicy getSubPolicy()
    	throws BigBangJewelException
    {
    	IProcess lobjProcess;

    	try
    	{
			lobjProcess = getProcess().GetParent();

	    	if ( Constants.ProcID_SubPolicy.equals(lobjProcess.GetScriptID()) )
	    		return (SubPolicy)lobjProcess.GetData();
		}
    	catch (BigBangJewelException e)
    	{
    		throw e;
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	return null;
    }

    public Policy getDirectPolicy()
    	throws BigBangJewelException
    {
    	IProcess lobjProcess;

    	try
    	{
			lobjProcess = getProcess().GetParent();

	    	if ( Constants.ProcID_Policy.equals(lobjProcess.GetScriptID()) )
	    		return (Policy)lobjProcess.GetData();
		}
    	catch (BigBangJewelException e)
    	{
    		throw e;
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	return null;
    }

    public Policy getAbsolutePolicy()
    	throws BigBangJewelException
    {
    	IProcess lobjProcess;

    	try
    	{
			lobjProcess = getProcess().GetParent();

	    	if ( Constants.ProcID_Policy.equals(lobjProcess.GetScriptID()) )
	    		return (Policy)lobjProcess.GetData();

	    	return (Policy)lobjProcess.GetParent().GetData();
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

    public Mediator getMediator()
    	throws BigBangJewelException
    {
    	if ( getAt(12) == null )
    		return getAbsolutePolicy().getMediator();

    	return Mediator.GetInstance(getNameSpace(), (UUID)getAt(12));
    }

    public UUID getProfile()
    	throws BigBangJewelException
    {
    	return getAbsolutePolicy().getProfile();
    }

    public ILog getPaymentLog()
    	throws BigBangJewelException
    {
    	if ( lrefPayment != null )
    		return lrefPayment;

    	try
    	{
        	if ( isReverseCircuit() )
        		lrefPayment = getProcess().GetLiveLog(Constants.OPID_Receipt_SendPayment);
        	
        	if ( lrefPayment == null )
        		lrefPayment = getProcess().GetLiveLog(Constants.OPID_Receipt_Payment);
		}
    	catch (BigBangJewelException e)
    	{
    		throw e;
		}
    	catch (Throwable e)
    	{
    		throw new BigBangJewelException(e.getMessage(), e);
		}

    	return lrefPayment;
    }

    public void clearPaymentLog()
    {
    	lrefPayment = null;
    }

    public boolean doCalcRetrocession()
    	throws BigBangJewelException
    {
    	BigDecimal ldblResult;

    	ldblResult = internalCalcRetrocession();

    	if ( ldblResult == null )
    		return false;

		try
		{
			setAt(6, ldblResult);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return true;
    }

    public PDDocument getOriginal()
    	throws BigBangJewelException
    {
		Document[] larrDocs;
		int i;
		FileXfer lobjFile;
		ByteArrayInputStream lstreamInput;

		larrDocs = GetCurrentDocs();

		lstreamInput = null;
		for ( i = 0; i < larrDocs.length; i++ )
		{
			if ( Constants.DocID_ReceiptScan.equals(larrDocs[i].getAt(3)) )
			{
				if ( larrDocs[i].getAt(5) == null )
					return null;
		    	if ( larrDocs[i].getAt(5) instanceof FileXfer )
		    		lobjFile = (FileXfer)larrDocs[i].getAt(5);
		    	else
		    		lobjFile = new FileXfer((byte[])larrDocs[i].getAt(5));
		    	lstreamInput = new ByteArrayInputStream(lobjFile.getData());
		    	break;
			}
		}

		if ( lstreamInput == null )
			return null;

		try
		{
			return PDDocument.load(lstreamInput);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
    }

    public FileXfer getStamped()
    	throws BigBangJewelException
    {
		Document[] larrDocs;
		int i;
		FileXfer lobjFile;

		larrDocs = GetCurrentDocs();

		lobjFile = null;
		for ( i = 0; i < larrDocs.length; i++ )
		{
			if ( Constants.DocID_CutReceiptImage.equals(larrDocs[i].getAt(3)) )
			{
				if ( larrDocs[i].getAt(5) == null )
					return null;
		    	if ( larrDocs[i].getAt(5) instanceof FileXfer )
		    		lobjFile = (FileXfer)larrDocs[i].getAt(5);
		    	else
		    		lobjFile = new FileXfer((byte[])larrDocs[i].getAt(5));
		    	break;
			}
		}

		return lobjFile;
    }

    private BigDecimal internalCalcRetrocession()
    	throws BigBangJewelException
    {
    	Mediator lobjMed;
    	UUID lidProfile;
    	MediatorBase lobjCalc;
    	BigDecimal ldblPercent;
    	BigDecimal ldblBase;
    	boolean lbFound;

    	lobjMed = getMediator();
    	lidProfile = lobjMed.getProfile();

		if ( Constants.MCPID_Special.equals(lidProfile) )
		{
			lobjCalc = lobjMed.GetDetailedObject(this);

			if ( lobjCalc == null )
				return null;

			return lobjCalc.getResult();
		}

    	if ( getAt(I.RETROCESSIONS) != null )
    		return null;

    	if ( Constants.MCPID_None.equals(lidProfile) )
			return new BigDecimal(0.00);

		ldblPercent = null;
		ldblBase = null;
		lbFound = false;

		if ( Constants.MCPID_Issuing.equals(lidProfile) )
		{
			ldblPercent = getAbsolutePolicy().GetSubLine().getPercent();
			ldblBase = (BigDecimal)getAt(4);
			if ( (ldblBase != null) && (getAt(17) != null) )
			{
				if ( (getAt(18) == null) || !((Boolean)getAt(18)) )
					ldblBase = ldblBase.subtract((BigDecimal)getAt(17));
				else
					ldblBase = ldblBase.add((BigDecimal)getAt(17));
			}
			lbFound = true;
		}

		if ( Constants.MCPID_Percentage.equals(lidProfile) )
		{
			ldblPercent = getAbsolutePolicy().getPercentOverride();
			if ( ldblPercent == null )
				ldblPercent = lobjMed.getPercent();
			ldblBase = (BigDecimal)getAt(5);
			lbFound = true;
		}

		if ( Constants.MCPID_Negotiated.equals(lidProfile) )
		{
			ldblPercent = lobjMed.GetCurrentDealFor(getAbsolutePolicy().GetSubLine().getKey());
			if ( ldblPercent == null )
				ldblPercent = getAbsolutePolicy().GetSubLine().getPercent();
			ldblBase = (BigDecimal)getAt(4);
			if ( (ldblBase != null) && (getAt(17) != null) )
			{
				if ( (getAt(18) == null) || !((Boolean)getAt(18)) )
					ldblBase = ldblBase.subtract((BigDecimal)getAt(17));
				else
					ldblBase = ldblBase.add((BigDecimal)getAt(17));
			}
			lbFound = true;
		}

		if ( !lbFound )
			throw new BigBangJewelException("Perfil de comissionamento desconhecido.");

    	if ( (ldblPercent == null) || (ldblBase == null) )
    		return null;

		return ldblPercent.abs().multiply(ldblBase).divide(new BigDecimal(100.00)).setScale(2, RoundingMode.HALF_UP);
    }
}
