package bigBang.module.receiptModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.AssociateWithDebitNote;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateDASRequest;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSignatureRequest;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.DASNotNecessary;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.DeleteReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.InsurerAccounting;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.MediatorAccounting;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.NotPayedIndication;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReceiveImage;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SendPayment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SendReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SetReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.TransferToPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ValidateReceipt;

public class ReceiptServiceImpl
	extends SearchServiceBase
	implements ReceiptService
{
	private static final long serialVersionUID = 1L;

	public static Receipt sGetReceipt(UUID pidReceipt)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Receipt lobjResult;
		IProcess lobjProc;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		Client lobjClient;
		Mediator lobjMed;
		SubLine lobjSubLine;
		Line lobjLine;
		ObjectBase lobjCategory;
		ObjectBase lobjType;
		ObjectBase lobjStatus;

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), pidReceipt);
			if ( lobjReceipt.GetProcessID() == null )
				throw new BigBangException("Erro: Recibo sem processo de suporte. (Recibo n. "
						+ lobjReceipt.getAt(0).toString() + ")");
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
			if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
			{
				lobjPolicy = (Policy)lobjProc.GetParent().GetData();
				lobjSubPolicy = null;
				lobjClient = (Client)lobjProc.GetParent().GetParent().GetData();
			}
			else
			{
				lobjSubPolicy = (SubPolicy)lobjProc.GetParent().GetData();
				lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();;
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubPolicy.getAt(2));
			}
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(),
					(lobjPolicy.getAt(11) == null ?  (UUID)lobjClient.getAt(8) : (UUID)lobjPolicy.getAt(11)) );
			lobjSubLine = lobjPolicy.GetSubLine();
			lobjLine = lobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType),
					(UUID)lobjReceipt.getAt(1));
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
					getStatus(lobjReceipt.getKey()));
			
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Receipt();

		lobjResult.id = lobjReceipt.getKey().toString();
		lobjResult.number = (String)lobjReceipt.getAt(0);
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.policyId = ( lobjSubPolicy == null ? lobjPolicy.getKey().toString() : lobjSubPolicy.getKey().toString() );
		lobjResult.policyNumber = ( lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel() );
		lobjResult.categoryId = lobjCategory.getKey().toString();
		lobjResult.categoryName = lobjCategory.getLabel();
		lobjResult.lineId = lobjLine.getKey().toString();
		lobjResult.lineName = lobjLine.getLabel();
		lobjResult.subLineId = lobjSubLine.getKey().toString();
		lobjResult.subLineName = lobjSubLine.getLabel();
		lobjResult.typeId = lobjType.getKey().toString();
		lobjResult.typeName = (String)lobjType.getAt(1);
		lobjResult.totalPremium = ((BigDecimal)lobjReceipt.getAt(3)).doubleValue();
		lobjResult.maturityDate = (lobjReceipt.getAt(9) == null ? null :
				((Timestamp)lobjReceipt.getAt(9)).toString().substring(0, 10));
		lobjResult.description = (String)lobjReceipt.getAt(14);
		lobjResult.statusId = lobjStatus.getKey().toString();
		lobjResult.statusText= lobjStatus.getLabel();
		switch ( (Integer)lobjStatus.getAt(1) )
		{
		case 0:
			lobjResult.statusIcon = ReceiptStub.ReceiptStatus.NEW;
			break;

		case 1:
			lobjResult.statusIcon = ReceiptStub.ReceiptStatus.PAYABLE;
			break;

		case 2:
			lobjResult.statusIcon = ReceiptStub.ReceiptStatus.PAID;
			break;
		}
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.salesPremium = (lobjReceipt.getAt(4) == null ? null : ((BigDecimal)lobjReceipt.getAt(4)).doubleValue());
		lobjResult.comissions = ((BigDecimal)lobjReceipt.getAt(5)).doubleValue();
		lobjResult.retrocessions = ((BigDecimal)lobjReceipt.getAt(6)).doubleValue();
		lobjResult.FATValue = (lobjReceipt.getAt(7) == null ? null : ((BigDecimal)lobjReceipt.getAt(7)).doubleValue());
		lobjResult.issueDate = (lobjReceipt.getAt(8) == null ? null : ((Timestamp)lobjReceipt.getAt(8)).toString().substring(0, 10));
		lobjResult.endDate = (lobjReceipt.getAt(10) == null ? null :
			((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
		lobjResult.dueDate = (lobjReceipt.getAt(11) == null ? null :
			((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
		lobjResult.mediatorId = (lobjReceipt.getAt(12) == null ? null : ((UUID)lobjReceipt.getAt(12)).toString());
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.notes = (String)lobjReceipt.getAt(13);

		lobjResult.managerId = lobjProc.GetManagerID().toString();

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

		return lobjResult;
	}

	public Receipt getReceipt(String receiptId)
			throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetReceipt(UUID.fromString(receiptId));
	}

	public SearchResult[] getExactResults(String label)
		throws SessionExpiredException, BigBangException
	{
		return getExactResultsByOp(label, null);
	}

	public SearchResult[] getExactResultsByOp(String label, String operationId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<SearchResult> larrResult;
        IEntity lrefReceipts;
        MasterDB ldb;
        ResultSet lrsReceipts;
        com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProc;
		IStep lobjStep;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		Client lobjClient;
		SubLine lobjSubLine;
		Line lobjLine;
		ObjectBase lobjCategory;
		ObjectBase lobjType;
		ObjectBase lobjStatus;
		ReceiptStub lobjStub;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrResult = new ArrayList<SearchResult>();

		try
        {
            lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
            ldb = new MasterDB();
        }
        catch (Throwable e)
        {
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsReceipts = lrefReceipts.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {"!" + label}, null);
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            while (lrsReceipts.next())
            {
            	lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsReceipts);
    			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());

    			if ( operationId != null )
    			{
    				lobjStep = lobjProc.GetOperation(UUID.fromString(operationId), ldb);
    				if ( (lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
    					continue;
    			}

    			if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
    			{
    				lobjPolicy = (Policy)lobjProc.GetParent().GetData();
    				lobjSubPolicy = null;
    				lobjClient = (Client)lobjProc.GetParent().GetParent().GetData();
    			}
    			else
    			{
    				lobjSubPolicy = (SubPolicy)lobjProc.GetParent().GetData();
    				lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();;
    				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubPolicy.getAt(2));
    			}
    			lobjSubLine = lobjPolicy.GetSubLine();
    			lobjLine = lobjSubLine.getLine();
    			lobjCategory = lobjLine.getCategory();
    			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType),
    					(UUID)lobjReceipt.getAt(1));
    			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
    					getStatus(lobjReceipt.getKey()));

            	lobjStub = new ReceiptStub();

            	lobjStub.id = lobjReceipt.getKey().toString();
            	lobjStub.processId = lobjProc.getKey().toString();
            	lobjStub.number = (String)lobjReceipt.getAt(0);
            	lobjStub.clientId = lobjClient.getKey().toString();
            	lobjStub.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
            	lobjStub.clientName = lobjClient.getLabel();
            	lobjStub.policyId = ( lobjSubPolicy == null ? lobjPolicy.getKey().toString() : lobjSubPolicy.getKey().toString() );
            	lobjStub.policyNumber = ( lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel() );
            	lobjStub.categoryId = lobjCategory.getKey().toString();
            	lobjStub.categoryName = lobjCategory.getLabel();
            	lobjStub.lineId = lobjLine.getKey().toString();
            	lobjStub.lineName = lobjLine.getLabel();
            	lobjStub.subLineId = lobjSubLine.getKey().toString();
            	lobjStub.subLineName = lobjSubLine.getLabel();
            	lobjStub.typeId = lobjType.getKey().toString();
            	lobjStub.typeName = (String)lobjType.getAt(1);
            	lobjStub.totalPremium = ((BigDecimal)lobjReceipt.getAt(3)).doubleValue();
            	lobjStub.maturityDate = (lobjReceipt.getAt(9) == null ? null :
        				((Timestamp)lobjReceipt.getAt(9)).toString().substring(0, 10));
            	lobjStub.description = (String)lobjReceipt.getAt(14);
            	lobjStub.statusId = lobjStatus.getKey().toString();
            	lobjStub.statusText= lobjStatus.getLabel();
    			switch ( (Integer)lobjStatus.getAt(1) )
    			{
    			case 0:
    				lobjStub.statusIcon = ReceiptStub.ReceiptStatus.NEW;
    				break;

    			case 1:
    				lobjStub.statusIcon = ReceiptStub.ReceiptStatus.PAYABLE;
    				break;

    			case 2:
    				lobjStub.statusIcon = ReceiptStub.ReceiptStatus.PAID;
    				break;
    			}

    			lobjStub.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

            	larrResult.add(lobjStub);
            }
        }
        catch (Throwable e)
        {
        	try {lrsReceipts.close();} catch (Throwable e2) {}
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsReceipts.close();
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
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

		return larrResult.toArray(new SearchResult[larrResult.size()]);
	}

	public Receipt editReceipt(Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		ManageData lopMRD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMRD = new ManageData(UUID.fromString(receipt.processId));
			lopMRD.mobjData = new ReceiptData();

			lopMRD.mobjData.mid = UUID.fromString(receipt.id);

			lopMRD.mobjData.mstrNumber = receipt.number;
			lopMRD.mobjData.midType = UUID.fromString(receipt.typeId);
			lopMRD.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium+"");
			lopMRD.mobjData.midType = UUID.fromString(receipt.typeId);
			lopMRD.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium+"");
			lopMRD.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium+""));
			lopMRD.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions+""));
			lopMRD.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? new BigDecimal(0) :
					new BigDecimal(receipt.retrocessions+""));
			lopMRD.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue+""));
			lopMRD.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
			lopMRD.mobjData.mdtMaturity = (receipt.maturityDate == null ? null :
					Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
			lopMRD.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
			lopMRD.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
			lopMRD.mobjData.midMediator = ( receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId) );
			lopMRD.mobjData.mstrNotes = receipt.notes;
			lopMRD.mobjData.mstrDescription = receipt.description;
			lopMRD.mobjData.midProcess = UUID.fromString(receipt.processId);

			lopMRD.mobjData.midManager = null;

			lopMRD.mobjData.mobjPrevValues = null;

			lopMRD.mobjContactOps = null;
			lopMRD.mobjDocOps = null;

			lopMRD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(UUID.fromString(receipt.id));
	}

	public Receipt receiveImage(String receiptId, DocuShareHandle source)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ReceiveImage lopRI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRI = new ReceiveImage(lobjReceipt.GetProcessID());
		lopRI.mobjImage = new DSBridgeData();
		lopRI.mobjImage.mstrDSHandle = source.handle;
		lopRI.mobjImage.mstrDSLoc = source.locationHandle;
		lopRI.mobjImage.mstrDSTitle = null;
		lopRI.mobjImage.mbDelete = true;

		try
		{
			lopRI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt transferToPolicy(String receiptId, String newPolicyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		TransferToPolicy lopTTP;
		Policy lobjPolicy;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopTTP = new TransferToPolicy(lobjReceipt.GetProcessID());
		lopTTP.midNewProcess = lobjPolicy.GetProcessID();

		try
		{
			lopTTP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt validateReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ValidateReceipt lopVR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVR = new ValidateReceipt(lobjReceipt.GetProcessID());

		try
		{
			lopVR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt setForReturn(Receipt.ReturnMessage message)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		SetReturnToInsurer lopSRTI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(message.receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopSRTI = new SetReturnToInsurer(lobjReceipt.GetProcessID());
		lopSRTI.mstrSubject = message.subject;
		lopSRTI.mstrText = message.text;

		try
		{
			lopSRTI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt createPaymentNotice(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreatePaymentNotice lopCPN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCPN = new CreatePaymentNotice(lobjReceipt.GetProcessID());
		lopCPN.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopCPN.mbUseSets = false;

		try
		{
			lopCPN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt markPayed(Receipt.PaymentInfo info)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Payment lopP;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(info.receiptId));

			lopP = new Payment(lobjReceipt.GetProcessID());
			lopP.marrData = new PaymentData[info.payments.length];
			for ( i = 0; i < info.payments.length; i++ )
			{
				lopP.marrData[i] = new PaymentData();
				lopP.marrData[i].midPaymentType = UUID.fromString(info.payments[i].paymentTypeId);
				lopP.marrData[i].mdblValue = new BigDecimal(info.payments[i].value);
				lopP.marrData[i].midBank = (info.payments[i].bankId == null ? null : UUID.fromString(info.payments[i].bankId));
				lopP.marrData[i].mstrCheque = info.payments[i].chequeOrTransferNumber;
				lopP.marrData[i].midReceipt = (info.payments[i].otherReceiptId == null ? null :
						UUID.fromString(info.payments[i].otherReceiptId));
				lopP.marrData[i].mbCreateCounter = info.payments[i].markOtherAsPayed;
				lopP.marrData[i].midLog = null;
			}

			lopP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public DebitNote[] getRelevantDebitNotes(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<DebitNote> larrNotes;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		IEntity lrefProcesses;
        MasterDB ldb;
        ResultSet lrsNotes;
        com.premiumminds.BigBang.Jewel.Objects.DebitNote lobjAux;
        DebitNote lobjNote;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrNotes = new ArrayList<DebitNote>();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
			lrefProcesses = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DebitNote)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsNotes = lrefProcesses.SelectByMembers(ldb, new int[] {Constants.FKProcess_In_DebitNote},
					new java.lang.Object[] {lobjProcess.getKey()}, new int[] {4});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsNotes.next() )
			{
				lobjAux = com.premiumminds.BigBang.Jewel.Objects.DebitNote.GetInstance(Engine.getCurrentNameSpace(), lrsNotes);
				lobjNote = new DebitNote();
				lobjNote.id = lobjAux.getKey().toString();
				lobjNote.number = lobjAux.getLabel();
				lobjNote.value = ( lobjAux.getAt(2) == null ? null : ((BigDecimal)lobjAux.getAt(2)).doubleValue() );
				lobjNote.maturityDate = ( lobjAux.getAt(3) == null ? null : ((Timestamp)lobjAux.getAt(3)).toString().substring(0, 10) );
				larrNotes.add(lobjNote);
			}
		}
		catch (Throwable e)
		{
			try { lrsNotes.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsNotes.close();
			lrsNotes = lrefProcesses.SelectByMembers(ldb, new int[] {Constants.FKProcess_In_DebitNote, Constants.FKReceipt_In_DebitNote},
					new java.lang.Object[] {lobjProcess.GetParent().getKey(), null}, new int[] {4});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsNotes.next() )
			{
				lobjAux = com.premiumminds.BigBang.Jewel.Objects.DebitNote.GetInstance(Engine.getCurrentNameSpace(), lrsNotes);
				lobjNote = new DebitNote();
				lobjNote.id = lobjAux.getKey().toString();
				lobjNote.number = lobjAux.getLabel();
				lobjNote.value = ( lobjAux.getAt(2) == null ? null : ((BigDecimal)lobjAux.getAt(2)).doubleValue() );
				lobjNote.maturityDate = ( lobjAux.getAt(3) == null ? null : ((Timestamp)lobjAux.getAt(3)).toString().substring(0, 10) );
				larrNotes.add(lobjNote);
			}
		}
		catch (Throwable e)
		{
			try { lrsNotes.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsNotes.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrNotes.toArray(new DebitNote[larrNotes.size()]);
	}

	public Receipt associateWithDebitNote(String receiptId, String debitNoteId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		AssociateWithDebitNote lopAWDN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopAWDN = new AssociateWithDebitNote(lobjReceipt.GetProcessID());
		lopAWDN.midDebitNote = UUID.fromString(debitNoteId);

		try
		{
			lopAWDN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt markNotPayed(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		NotPayedIndication lopNPI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopNPI = new NotPayedIndication(lobjReceipt.GetProcessID());

		try
		{
			lopNPI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt setDASNotNecessary(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		DASNotNecessary lopDNN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopDNN = new DASNotNecessary(lobjReceipt.GetProcessID());

		try
		{
			lopDNN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt createDASRequest(DASRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreateDASRequest lopCDR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCDR = new CreateDASRequest(lobjReceipt.GetProcessID());
		lopCDR.mlngDays = request.replylimit;

		try
		{
			lopCDR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt createSignatureRequest(SignatureRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreateSignatureRequest lopCSR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCSR = new CreateSignatureRequest(lobjReceipt.GetProcessID());
		lopCSR.marrReceiptIDs = new UUID[] {UUID.fromString(request.receiptId)};
		lopCSR.mlngDays = request.replylimit;
		lopCSR.mbUseSets = false;

		try
		{
			lopCSR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt sendPayment(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		SendPayment lopSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopSP = new SendPayment(lobjReceipt.GetProcessID());
		lopSP.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopSP.mbUseSets = false;

		try
		{
			lopSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt sendReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		SendReceipt lopSR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopSR = new SendReceipt(lobjReceipt.GetProcessID());
		lopSR.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopSR.mbUseSets = false;

		try
		{
			lopSR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt insurerAccouting(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		InsurerAccounting lopIA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopIA = new InsurerAccounting(lobjReceipt.GetProcessID());

		try
		{
			lopIA.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt mediatorAccounting(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		MediatorAccounting lopMA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMA = new MediatorAccounting(lobjReceipt.GetProcessID());

		try
		{
			lopMA.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt returnToInsurer(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ReturnToInsurer lopRTI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRTI = new ReturnToInsurer(lobjReceipt.GetProcessID());
		lopRTI.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopRTI.mbUseSets = false;

		try
		{
			lopRTI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public void deleteReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		DeleteReceipt lopDP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));

			lopDP = new DeleteReceipt(lobjReceipt.GetProcessID());
			lopDP.midReceipt = UUID.fromString(receiptId);
			lopDP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public Receipt serialCreateReceipt(Receipt receipt, DocuShareHandle source)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.policyId));

			lopCR = new CreateReceipt(lobjPolicy.GetProcessID());
			lopCR.mobjData = new ReceiptData();

			lopCR.mobjData.mid = null;

			lopCR.mobjData.mstrNumber = receipt.number;
			lopCR.mobjData.midType = UUID.fromString(receipt.typeId);
			lopCR.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium+"");
			lopCR.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium+""));
			lopCR.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions+""));
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? new BigDecimal(0) :
					new BigDecimal(receipt.retrocessions+""));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue+""));
			lopCR.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
			lopCR.mobjData.mdtMaturity = (receipt.maturityDate == null ? null :
					Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
			lopCR.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
			lopCR.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
			lopCR.mobjData.midMediator = (receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId));
			lopCR.mobjData.mstrNotes = receipt.notes;
			lopCR.mobjData.mstrDescription = receipt.description;

			lopCR.mobjData.midManager = ( receipt.managerId == null ? null : UUID.fromString(receipt.managerId) );
			lopCR.mobjData.midProcess = null;

			lopCR.mobjData.mobjPrevValues = null;

			if ( source != null )
			{
				lopCR.mobjImage = new DSBridgeData();
				lopCR.mobjImage.mstrDSHandle = source.handle;
				lopCR.mobjImage.mstrDSLoc = source.locationHandle;
				lopCR.mobjImage.mstrDSTitle = null;
				lopCR.mobjImage.mbDelete = true;
			}
			else
				lopCR.mobjImage = null;

			if ( (receipt.contacts != null) && (receipt.contacts.length > 0) )
			{
				lopCR.mobjContactOps = new ContactOps();
				lopCR.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(receipt.contacts);
			}
			else
				lopCR.mobjContactOps = null;

			if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			{
				lopCR.mobjDocOps = new DocOps();
				lopCR.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(receipt.documents);
			}
			else
				lopCR.mobjDocOps = null;

			lopCR.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lopCR.mobjData.mid);
	}

	public void massCreatePaymentNotice(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		CreatePaymentNotice lopCPN;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
				else
					lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrReceipts.get(lidClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrReceipts.put(lidClient, larrByClient);
			}
			larrByClient.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrReceipts.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			larrByClient = larrReceipts.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopCPN = new CreatePaymentNotice(lobjReceipt.GetProcessID());
					lopCPN.marrReceiptIDs = larrFinal;
					lopCPN.mbUseSets = true;
					lopCPN.midSet = lidSet;
					lopCPN.midSetDocument = lidSetClient;
					lopCPN.mobjDocOps = lobjDocOps;

					lopCPN.Execute();

					lobjDocOps = lopCPN.mobjDocOps;
					lidSetClient = lopCPN.midSetDocument;
					lidSet = lopCPN.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massSendReceipt(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		SendReceipt lopSR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
				else
					lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrReceipts.get(lidClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrReceipts.put(lidClient, larrByClient);
			}
			larrByClient.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrReceipts.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			larrByClient = larrReceipts.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopSR = new SendReceipt(lobjReceipt.GetProcessID());
					lopSR.marrReceiptIDs = larrFinal;
					lopSR.mbUseSets = true;
					lopSR.midSet = lidSet;
					lopSR.midSetDocument = lidSetClient;
					lopSR.mobjDocOps = lobjDocOps;

					lopSR.Execute();

					lobjDocOps = lopSR.mobjDocOps;
					lidSetClient = lopSR.midSetDocument;
					lidSet = lopSR.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massSendPayment(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		SendPayment lopSR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
				else
					lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrReceipts.get(lidClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrReceipts.put(lidClient, larrByClient);
			}
			larrByClient.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrReceipts.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			larrByClient = larrReceipts.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopSR = new SendPayment(lobjReceipt.GetProcessID());
					lopSR.marrReceiptIDs = larrFinal;
					lopSR.mbUseSets = true;
					lopSR.midSet = lidSet;
					lopSR.midSetDocument = lidSetClient;
					lopSR.mobjDocOps = lobjDocOps;

					lopSR.Execute();

					lobjDocOps = lopSR.mobjDocOps;
					lidSetClient = lopSR.midSetDocument;
					lidSet = lopSR.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massInsurerAccounting(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidInsurer;
		ArrayList<UUID> larrByInsurer;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidMap;
		InsurerAccounting lopIA;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidInsurer = (UUID)lobjProcess.GetParent().GetData().getAt(2);
				else
					lidInsurer = (UUID)lobjProcess.GetParent().GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByInsurer = larrReceipts.get(lidInsurer);
			if ( larrByInsurer == null )
			{
				larrByInsurer = new ArrayList<UUID>();
				larrReceipts.put(lidInsurer, larrByInsurer);
			}
			larrByInsurer.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidI : larrReceipts.keySet())
		{
			lidMap = null;
			larrByInsurer = larrReceipts.get(lidI);
			larrFinal = larrByInsurer.toArray(new UUID[larrByInsurer.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopIA = new InsurerAccounting(lobjReceipt.GetProcessID());
					lopIA.midSet = lidSet;
					lopIA.midMap = lidMap;

					lopIA.Execute();

					lidMap = lopIA.midMap;
					lidSet = lopIA.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massMediatorAccounting(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidMediator;
		ArrayList<UUID> larrByMediator;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidMap;
		MediatorAccounting lopMA;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				lidMediator = (UUID)lobjReceipt.getAt(12);
				if ( lidMediator == null )
				{
					if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					{
						lidMediator = (UUID)lobjProcess.GetParent().GetData().getAt(11);
						if ( lidMediator == null )
							lidMediator = (UUID)lobjProcess.GetParent().GetParent().GetData().getAt(8);
					}
					else
					{
						lidMediator = (UUID)lobjProcess.GetParent().GetParent().GetData().getAt(11);
						if ( lidMediator == null )
							lidMediator = (UUID)lobjProcess.GetParent().GetParent().GetParent().GetData().getAt(8);
					}
				}

			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByMediator = larrReceipts.get(lidMediator);
			if ( larrByMediator == null )
			{
				larrByMediator = new ArrayList<UUID>();
				larrReceipts.put(lidMediator, larrByMediator);
			}
			larrByMediator.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidM : larrReceipts.keySet())
		{
			lidMap = null;
			larrByMediator = larrReceipts.get(lidM);
			larrFinal = larrByMediator.toArray(new UUID[larrByMediator.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopMA = new MediatorAccounting(lobjReceipt.GetProcessID());
					lopMA.midSet = lidSet;
					lopMA.midMap = lidMap;

					lopMA.Execute();

					lidMap = lopMA.midMap;
					lidSet = lopMA.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massReturnToInsurer(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProcess;
		UUID lidInsurer;
		ArrayList<UUID> larrByInsurer;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetInsurer;
		DocOps lobjDocOps;
		ReturnToInsurer lopRTI;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidInsurer = (UUID)lobjProcess.GetParent().GetData().getAt(2);
				else
					lidInsurer = (UUID)lobjProcess.GetParent().GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByInsurer = larrReceipts.get(lidInsurer);
			if ( larrByInsurer == null )
			{
				larrByInsurer = new ArrayList<UUID>();
				larrReceipts.put(lidInsurer, larrByInsurer);
			}
			larrByInsurer.add(lobjReceipt.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrReceipts.keySet())
		{
			lidSetInsurer = null;
			lobjDocOps = null;
			larrByInsurer = larrReceipts.get(lidC);
			larrFinal = larrByInsurer.toArray(new UUID[larrByInsurer.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopRTI = new ReturnToInsurer(lobjReceipt.GetProcessID());
					lopRTI.marrReceiptIDs = larrFinal;
					lopRTI.mbUseSets = true;
					lopRTI.midSet = lidSet;
					lopRTI.midSetDocument = lidSetInsurer;
					lopRTI.mobjDocOps = lobjDocOps;

					lopRTI.Execute();

					lobjDocOps = lopRTI.mobjDocOps;
					lidSetInsurer = lopRTI.midSetDocument;
					lidSet = lopRTI.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Receipt;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Type]", "[:Type:Indicator]", "[:Total Premium]", "[:Maturity Date]",
				"[:Description]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;
		IEntity lrefClients;
        Calendar ldtAux;
		IEntity lrefLogs;
		int i;

		if ( !(pParam instanceof ReceiptSearchParameter) )
			return false;
		lParam = (ReceiptSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Type:Indicator] = N'").append(lstrAux).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(LEFT(CONVERT(NVARCHAR, [:Maturity Date], 120), 10) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Total Premium] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE (");
			pstrBuffer.append("([:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append("))))))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Subscriber:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Subscriber:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSMPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))))))");
		}

		if ( lParam.companyId != null )
		{
			pstrBuffer.append(" AND (([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Company] = '").append(lParam.companyId).append("'))");
			pstrBuffer.append(" OR ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSMPols] WHERE [:Company] = '").append(lParam.companyId).append("')))))");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND ([:Mediator] = '").append(lParam.mediatorId).append("'");
			pstrBuffer.append(" OR ([:Mediator] IS NULL");
			pstrBuffer.append(" AND (([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE ([:Mediator] = '").append(lParam.mediatorId).append("'");
			pstrBuffer.append(" OR ([:Mediator] IS NULL");
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxCli] WHERE [:Mediator] = '").append(lParam.mediatorId).append("'))))))");
			pstrBuffer.append(" OR ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSMPols] WHERE ([:Mediator] = '").append(lParam.mediatorId).append("'");
			pstrBuffer.append(" OR ([:Mediator] IS NULL");
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxCli] WHERE [:Mediator] = '").append(lParam.mediatorId).append("')))))))))))");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND (([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("'))");
			pstrBuffer.append(" OR ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')))");
		}

		if ( (lParam.typeIds != null ) && (lParam.typeIds.length > 0) )
		{
			pstrBuffer.append(" AND [:Type] IN ('");
			for ( i = 0; i < lParam.typeIds.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append("', '");
				pstrBuffer.append(lParam.typeIds[i]);
			}
			pstrBuffer.append("')");
		}

		if ( lParam.emitedFrom != null )
		{
			pstrBuffer.append(" AND [:Issue Date] >= '").append(lParam.emitedFrom).append("'");
		}

		if ( lParam.emitedTo != null )
		{
			pstrBuffer.append(" AND [:Issue Date] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.emitedTo + " 00:00:00.0").getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		if ( lParam.maturityFrom != null )
		{
			pstrBuffer.append(" AND [:Maturity Date] >= '").append(lParam.maturityFrom).append("'");
		}

		if ( lParam.maturityTo != null )
		{
			pstrBuffer.append(" AND [:Maturity Date] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.maturityTo + " 00:00:00.0").getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		if ( (lParam.paymentFrom != null) || (lParam.paymentTo != null) )
		{
			pstrBuffer.append(" AND [:Process] IN (SELECT [:Process] FROM (");
			try
			{
				lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
				pstrBuffer.append(lrefLogs.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPayment] WHERE [:Operation] = '").append(Constants.OPID_Receipt_Payment);
			if ( lParam.paymentFrom != null )
				pstrBuffer.append("' AND [:Timestamp] >= '").append(lParam.paymentFrom).append("'");
			if ( lParam.paymentTo != null )
			{
	        	ldtAux = Calendar.getInstance();
	        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.paymentTo + " 00:00:00.0").getTime());
	        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
	        	pstrBuffer.append("' AND [:Timestamp] < '");
	        	pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
			}
			pstrBuffer.append(")");
		}

		if ( (lParam.subLineId != null) || (lParam.lineId != null) || (lParam.categoryId != null) )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxLine] WHERE ");
			if ( lParam.subLineId != null )
			{
				pstrBuffer.append("[:SubLine] = '").append(lParam.subLineId);
			}
			else if ( lParam.lineId != null )
			{
				pstrBuffer.append("[:SubLine:Line] = '").append(lParam.lineId);
			}
			else if ( lParam.categoryId != null )
			{
				pstrBuffer.append("[:SubLine:Line:Category] = '").append(lParam.categoryId);
			}
			pstrBuffer.append("')");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		ReceiptSortParameter lParam;
		IEntity lrefPolicies;
		IEntity lrefClients;
		IEntity lrefLogs;

		if ( !(pParam instanceof ReceiptSortParameter) )
			return false;
		lParam = (ReceiptSortParameter)pParam;

		if ( lParam.field == ReceiptSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ReceiptSortParameter.SortableField.TYPE )
			pstrBuffer.append("[:Type:Indicator]");

		if ( lParam.field == ReceiptSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == ReceiptSortParameter.SortableField.CLIENT )
		{
			pstrBuffer.append("(SELECT [AuxClients].[:Number] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols], (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [AuxPols].[:Process] = [Aux].[:Process:Parent] AND ")
					.append("[AuxClients].[:Process] = [AuxPols].[:Process:Parent])");
		}

		if ( lParam.field == ReceiptSortParameter.SortableField.SUB_LINE )
		{
			pstrBuffer.append("(SELECT [:SubLine:Line:Category:Name] + ' / ' + [:SubLine:Line:Name] + ' / ' + [:SubLine:Name] ")
					.append("FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == ReceiptSortParameter.SortableField.EMISSION_DATE )
			pstrBuffer.append("[:Issue Date]");
			
		if ( lParam.field == ReceiptSortParameter.SortableField.LIMIT_DATE )
			pstrBuffer.append("[:Due Date]");
			
		if ( lParam.field == ReceiptSortParameter.SortableField.MATURITY_DATE )
			pstrBuffer.append("[:Maturity Date]");

		if ( lParam.field == ReceiptSortParameter.SortableField.PAYMENT_DATE )
		{
			pstrBuffer.append("(SELECT MAX([:Timestamp]) FROM (");
			try
			{
				lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
				pstrBuffer.append(lrefLogs.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxLogs] WHERE [:Process] = [Aux].[:Process] AND ")
					.append("[:Operation] = '").append(Constants.OPID_Receipt_Payment).append(")");
		}

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		Client lobjClient;
		ObjectBase lobjSubLine, lobjLine, lobjCategory, lobjStatus;
		ReceiptStub lobjResult;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
				{
					lobjPolicy = (Policy)lobjProcess.GetParent().GetData();
					lobjSubPolicy = null;
					lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
				}
				else
				{
					lobjSubPolicy = (SubPolicy)lobjProcess.GetParent().GetData();
					lobjPolicy = (Policy)lobjProcess.GetParent().GetParent().GetData();
					lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubPolicy.getAt(2));
				}
				try
				{
					lobjSubLine = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubLine),
							(UUID)lobjPolicy.getAt(3));
					try
					{
						lobjLine = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Line),
								(UUID)lobjSubLine.getAt(1));
						try
						{
							lobjCategory = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
									Constants.ObjID_LineCategory), (UUID)lobjLine.getAt(1));
						}
						catch (Throwable e)
						{
							lobjCategory = null;
						}
					}
					catch (Throwable e)
					{
						lobjLine = null;
						lobjCategory = null;
					}
				}
				catch (Throwable e)
				{
					lobjSubLine = null;
					lobjLine = null;
					lobjCategory = null;
				}
			}
			catch (Throwable e)
			{
				lobjPolicy = null;
				lobjSubPolicy = null;
				lobjClient = null;
				lobjSubLine = null;
				lobjLine = null;
				lobjCategory = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjPolicy = null;
			lobjSubPolicy = null;
			lobjSubLine = null;
			lobjLine = null;
			lobjCategory = null;
			lobjClient = null;
		}

		try
		{
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
					getStatus(pid));
		}
		catch (Throwable e)
		{
			lobjStatus = null;
		}

		lobjResult = new ReceiptStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.policyId = (lobjSubPolicy == null ? (lobjPolicy == null ? null : lobjPolicy.getKey().toString()) :
				lobjSubPolicy.getKey().toString());
		lobjResult.policyNumber = (lobjSubPolicy == null ? (lobjPolicy == null ? "(Erro)" : lobjPolicy.getLabel()) :
				lobjSubPolicy.getLabel());
		lobjResult.categoryId = (lobjCategory == null ? null : lobjCategory.getKey().toString());
		lobjResult.categoryName = (lobjCategory == null ? null : lobjCategory.getLabel());
		lobjResult.lineId = (lobjLine == null ? null : lobjLine.getKey().toString());
		lobjResult.lineName = (lobjLine == null ? null : lobjLine.getLabel());
		lobjResult.subLineId = (lobjSubLine == null ? null : lobjSubLine.getKey().toString());
		lobjResult.subLineName = (lobjSubLine == null ? null : lobjSubLine.getLabel());
		lobjResult.typeId = ((UUID)parrValues[2]).toString();
		lobjResult.typeName = (String)parrValues[3];
		lobjResult.totalPremium = ((BigDecimal)parrValues[4]).doubleValue();
		lobjResult.maturityDate = (parrValues[5] == null ? null : ((Timestamp)parrValues[5]).toString().substring(0, 10));
		lobjResult.description = (String)parrValues[6];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.statusId = (lobjStatus == null ? null : lobjStatus.getKey().toString());
		lobjResult.statusText= (lobjStatus == null ? null : lobjStatus.getLabel());
		if ( lobjStatus == null )
			lobjResult.statusIcon = ReceiptStub.ReceiptStatus.ERROR;
		else
		{
			switch ( (Integer)lobjStatus.getAt(1) )
			{
			case 0:
				lobjResult.statusIcon = ReceiptStub.ReceiptStatus.NEW;
				break;

			case 1:
				lobjResult.statusIcon = ReceiptStub.ReceiptStatus.PAYABLE;
				break;

			case 2:
				lobjResult.statusIcon = ReceiptStub.ReceiptStatus.PAID;
				break;
			}
		}

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;
		IEntity lrefClients;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ReceiptSearchParameter) )
				continue;
			lParam = (ReceiptSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ");
			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Number] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-100*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Number] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");

//			//Retirado por complexidade
//			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefClients.SQLForSelectSingle());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxClients] WHERE CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')) THEN ")
//					.append("-10000*PATINDEX(N'%").append(lstrAux).append("%', CAST((SELECT [AuxClients].[:Number] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols], (");
//			try
//			{
//				pstrBuffer.append(lrefClients.SQLForSelectSingle());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxClients] WHERE [AuxPols].[:Process] = [Aux].[:Process:Parent] AND ")
//					.append("[AuxClients].[:Process] = [AuxPols].[:Process:Parent]) AS NVARCHAR(20))) ELSE ");

			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%')) THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [AuxClients].[:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols], (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [AuxPols].[:Process] = [Aux].[:Process:Parent] AND ")
					.append("[AuxClients].[:Process] = [AuxPols].[:Process:Parent])) ELSE ");
			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-100000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");
			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-10000000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Line:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");
			pstrBuffer.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000000000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Line:Category:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");
			pstrBuffer.append("0 END END END END END END"); // END"); O END a mais parte do trim por complexidade
		}

		return lbFound;
	}

	private static UUID getStatus(UUID pidReceipt)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		IProcess lobjProc;

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), pidReceipt);
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());

			if ( !lobjProc.IsRunning() )
				return Constants.StatusID_Closed;

			if ( (lobjProc.GetValidOperation(Constants.OPID_Receipt_CreateDASRequest) != null) ||
					(lobjProc.GetValidOperation(Constants.OPID_Receipt_ExternReceiveDAS) != null) )
				return Constants.StatusID_DASPending;

			if ( (lobjProc.GetLiveLog(Constants.OPID_Receipt_Payment) != null) ||
					(lobjProc.GetLiveLog(Constants.OPID_Receipt_AssociateWithDebitNote) != null) )
				return Constants.StatusID_Paid;

			if ( (lobjProc.GetValidOperation(Constants.OPID_Receipt_Payment) != null) ||
					(lobjProc.GetValidOperation(Constants.OPID_Receipt_AssociateWithDebitNote) != null) )
				return Constants.StatusID_Payable;
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return Constants.StatusID_New;
	}
}
