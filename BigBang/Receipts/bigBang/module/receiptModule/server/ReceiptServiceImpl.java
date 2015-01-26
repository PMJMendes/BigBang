package bigBang.module.receiptModule.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.ImageItem;
import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.definitions.shared.OwnerRef;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.Rectangle;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.FileServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingSet;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.AssociateWithDebitNote;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CancelPaymentNotice;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateDASRequest;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateInternalReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateReceiptBase;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSecondPaymentNotice;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSignatureRequest;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.DASNotNecessary;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.DeleteReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.InsurerAccounting;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.MediatorAccounting;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.NotPayedIndication;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReceiveImage;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnPayment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SendPayment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SendReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SetReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.TransferToPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ValidateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.VoidInternal;
import com.premiumminds.BigBang.Jewel.SysObjects.ImageHelper;
import com.premiumminds.BigBang.Jewel.SysObjects.PDFHelper;

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
		SubCasualty lobjSubCasualty;
		Company lobjCompany;
		Client lobjClient;
		Timestamp ldtEndDate;
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
			lobjSubCasualty = lobjReceipt.getSubCasualty();
			lobjSubPolicy = lobjReceipt.getSubPolicy();
			lobjPolicy = lobjReceipt.getAbsolutePolicy();
			lobjClient = lobjReceipt.getClient();
			if ( lobjReceipt.getDirectPolicy() != null )
				ldtEndDate = (Timestamp)lobjPolicy.getAt(9);
			else if ( lobjSubPolicy != null )
				ldtEndDate = (Timestamp)lobjSubPolicy.getAt(4);
			else
				ldtEndDate = null;

			lobjCompany = lobjPolicy.GetCompany();
			lobjMed = lobjReceipt.getMediator();
			lobjSubLine = lobjPolicy.GetSubLine();
			lobjLine = lobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType),
					(UUID)lobjReceipt.getAt(1));
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
					getStatus((UUID)lobjReceipt.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.STATUS),
							(Timestamp)lobjReceipt.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.DUEDATE)));
			
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
		lobjResult.insurerId = lobjCompany.getKey().toString();
		lobjResult.insurerName = (String)lobjCompany.getAt(1);
		if ( lobjSubCasualty != null )
		{
			lobjResult.ownerId = lobjSubCasualty.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_SubCasualty.toString();
			lobjResult.ownerNumber = lobjSubCasualty.getLabel();
		}
		else if ( lobjSubPolicy != null )
		{
			lobjResult.ownerId = lobjSubPolicy.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_SubPolicy.toString();
			lobjResult.ownerNumber = lobjSubPolicy.getLabel();
		}
		else
		{
			lobjResult.ownerId = lobjPolicy.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_Policy.toString();
			lobjResult.ownerNumber = lobjPolicy.getLabel();
		}
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
		lobjResult.endDate = (lobjReceipt.getAt(10) == null ? null :
				((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
		lobjResult.description = (String)lobjReceipt.getAt(14);
		lobjResult.statusId = lobjStatus.getKey().toString();
		lobjResult.statusText= lobjStatus.getLabel();
		lobjResult.statusIcon = translateStatus((Integer)lobjStatus.getAt(1));
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.salesPremium = (lobjReceipt.getAt(4) == null ? null : ((BigDecimal)lobjReceipt.getAt(4)).doubleValue());
		lobjResult.comissions = (lobjReceipt.getAt(5) == null ? null : ((BigDecimal)lobjReceipt.getAt(5)).doubleValue());
		lobjResult.retrocessions = (lobjReceipt.getAt(6) == null ? null : ((BigDecimal)lobjReceipt.getAt(6)).doubleValue());
		lobjResult.FATValue = (lobjReceipt.getAt(7) == null ? null : ((BigDecimal)lobjReceipt.getAt(7)).doubleValue());
		lobjResult.bonusMalus = (lobjReceipt.getAt(17) == null ? null : ((BigDecimal)lobjReceipt.getAt(17)).doubleValue());
		lobjResult.isMalus = (Boolean)lobjReceipt.getAt(18);
		lobjResult.issueDate = (lobjReceipt.getAt(8) == null ? null : ((Timestamp)lobjReceipt.getAt(8)).toString().substring(0, 10));
		lobjResult.dueDate = (lobjReceipt.getAt(11) == null ? null :
				((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
		lobjResult.mediatorId = (lobjReceipt.getAt(12) == null ? null : ((UUID)lobjReceipt.getAt(12)).toString());
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.notes = (String)lobjReceipt.getAt(13);

		lobjResult.managerId = lobjProc.GetManagerID().toString();

		lobjResult.inheritEndDate = (ldtEndDate == null ? null : ldtEndDate.toString().substring(0, 10));

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

		return lobjResult;
	}

	public static ReceiptData sClientToServer(Receipt receipt)
	{
		ReceiptData lobjData;

		lobjData = new ReceiptData();

		lobjData.mid = null;

		lobjData.mstrNumber = receipt.number;
		lobjData.midType = UUID.fromString(receipt.typeId);
		lobjData.mdblTotal = new BigDecimal(receipt.totalPremium+"");
		lobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium+""));
		lobjData.mdblCommissions = (receipt.comissions == null ? BigDecimal.ZERO : new BigDecimal(receipt.comissions+""));
		lobjData.mdblRetrocessions = (receipt.retrocessions == null ? null : new BigDecimal(receipt.retrocessions+""));
		lobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue+""));
		lobjData.mdblBonusMalus = (receipt.bonusMalus == null ? null : new BigDecimal(receipt.bonusMalus + ""));
		lobjData.mbIsMalus = receipt.isMalus;
		lobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
		lobjData.mdtMaturity = (receipt.maturityDate == null ? null :
				Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
		lobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
		lobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
		lobjData.midMediator = (receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId));
		lobjData.mstrNotes = receipt.notes;
		lobjData.mstrDescription = receipt.description;

		lobjData.midManager = ( receipt.managerId == null ? null : UUID.fromString(receipt.managerId) );
		lobjData.midProcess = ( receipt.processId == null ? null : UUID.fromString(receipt.processId) );

		lobjData.mobjPrevValues = null;

		return lobjData;
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
		SubCasualty lobjSubCasualty;
		Company lobjCompany;
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

    			lobjSubCasualty = lobjReceipt.getSubCasualty();
    			lobjSubPolicy = lobjReceipt.getSubPolicy();
    			lobjPolicy = lobjReceipt.getAbsolutePolicy();
    			lobjClient = lobjReceipt.getClient();

    			lobjCompany = lobjPolicy.GetCompany();
    			lobjSubLine = lobjPolicy.GetSubLine();
    			lobjLine = lobjSubLine.getLine();
    			lobjCategory = lobjLine.getCategory();
    			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType),
    					(UUID)lobjReceipt.getAt(1));
    			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
    					getStatus((UUID)lobjReceipt.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.STATUS),
    							(Timestamp)lobjReceipt.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.DUEDATE)));

            	lobjStub = new ReceiptStub();

            	lobjStub.id = lobjReceipt.getKey().toString();
            	lobjStub.processId = lobjProc.getKey().toString();
            	lobjStub.number = (String)lobjReceipt.getAt(0);
            	lobjStub.clientId = lobjClient.getKey().toString();
            	lobjStub.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
            	lobjStub.clientName = lobjClient.getLabel();
            	lobjStub.insurerId = lobjCompany.getKey().toString();
            	lobjStub.insurerName = (String)lobjCompany.getAt(1);
        		if ( lobjSubCasualty != null )
        		{
        			lobjStub.ownerId = lobjSubCasualty.getKey().toString();
        			lobjStub.ownerTypeId = Constants.ObjID_SubCasualty.toString();
        			lobjStub.ownerNumber = lobjSubCasualty.getLabel();
        		}
        		else if ( lobjSubPolicy != null )
        		{
        			lobjStub.ownerId = lobjSubPolicy.getKey().toString();
        			lobjStub.ownerTypeId = Constants.ObjID_SubPolicy.toString();
        			lobjStub.ownerNumber = lobjSubPolicy.getLabel();
        		}
        		else
        		{
        			lobjStub.ownerId = lobjPolicy.getKey().toString();
        			lobjStub.ownerTypeId = Constants.ObjID_Policy.toString();
        			lobjStub.ownerNumber = lobjPolicy.getLabel();
        		}
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
            	lobjStub.endDate = (lobjReceipt.getAt(10) == null ? null :
    					((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
            	lobjStub.description = (String)lobjReceipt.getAt(14);
            	lobjStub.statusId = lobjStatus.getKey().toString();
            	lobjStub.statusText= lobjStatus.getLabel();
        		lobjStub.statusIcon = translateStatus((Integer)lobjStatus.getAt(1));
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

	public ImageItem getItemAsImage(String pstrItem, int pageNumber)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		FileXfer lobjFile;
		ByteArrayInputStream lstreamInput;
		PDDocument lobjDocument;
		ImageItem lobjResult;
		BufferedImage lobjImage;
		ByteArrayOutputStream lstreamOutput;
		byte[] larrBuffer;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(pstrItem));
			lobjDocument = lobjReceipt.getOriginal();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjDocument == null )
			return null;


		try
		{
			lobjResult = new ImageItem();
			lobjResult.id = pstrItem;
			lobjResult.pageNumber = pageNumber;
			lobjResult.pageCount = PDFHelper.getPageCount(lobjDocument);
			lobjImage = PDFHelper.getPage(lobjDocument, pageNumber);
		}
		catch (Throwable e)
		{
			try { lobjDocument.close(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjDocument.close();

			lstreamOutput = new ByteArrayOutputStream();
			ImageIO.write(lobjImage, "png", lstreamOutput);
//			ImageIO.write(lobjImage, "jpg", lstreamOutput);
			larrBuffer = lstreamOutput.toByteArray();
			lstreamInput = new ByteArrayInputStream(larrBuffer);
			lobjFile = new FileXfer(larrBuffer.length, "image/png", "pdfPage.png", lstreamInput);
//			lobjFile = new FileXfer(larrBuffer.length, "image/jpeg", "pdfPage.jpg", lstreamInput);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
		lobjResult.imageId = lidKey.toString();
		return lobjResult;
	}

	public Receipt editReceipt(Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ManageData lopMRD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.id));
			lopMRD = new ManageData(lobjReceipt.GetProcessID());
			lopMRD.mobjData = toServerData(receipt, lobjReceipt);

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

	public Receipt editAndValidateReceipt(Receipt receipt, Rectangle rect)
			throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		BufferedImage lobjImg;
		ByteArrayOutputStream lstreamOutput;
		byte[] larrBuffer;
		ByteArrayInputStream lstreamInput;
		FileXfer lobjFile;
		ValidateReceipt lopVR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.id));

			lopVR = new ValidateReceipt(lobjReceipt.GetProcessID());
			lopVR.mobjData = toServerData(receipt, lobjReceipt);

			if ( rect == null )
				lopVR.mobjDocOps = null;
			else
			{
				lobjImg = ImageHelper.cropAndStamp(PDFHelper.getPage(lobjReceipt.getOriginal(), 0),
						rect.x1, rect.y1, rect.x2-rect.x1, rect.y2-rect.y1);

				lstreamOutput = new ByteArrayOutputStream();
				ImageIO.write(lobjImg, "png", lstreamOutput);
				larrBuffer = lstreamOutput.toByteArray();
				lstreamInput = new ByteArrayInputStream(larrBuffer);
				lobjFile = new FileXfer(larrBuffer.length, "image/png", "tratado.png", lstreamInput);

				lopVR.mobjDocOps = new DocOps();
				lopVR.mobjDocOps.marrDelete2 = null;
				lopVR.mobjDocOps.marrModify2 = null;
				lopVR.mobjDocOps.marrCreate2 = new DocDataLight[] {new DocDataLight()};
				lopVR.mobjDocOps.marrCreate2[0].mstrName = "Tratado";
				lopVR.mobjDocOps.marrCreate2[0].midOwnerType = Constants.ObjID_Receipt;
				lopVR.mobjDocOps.marrCreate2[0].midOwnerId = lobjReceipt.getKey();
				lopVR.mobjDocOps.marrCreate2[0].midDocType = Constants.DocID_CutReceiptImage;
				lopVR.mobjDocOps.marrCreate2[0].mstrText = null;
				lopVR.mobjDocOps.marrCreate2[0].mobjFile = lobjFile.GetVarData();
			}

			lopVR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}
	
	public Receipt receiveImage(Receipt receipt, DocuShareHandle source)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ReceiveImage lopRI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receipt.id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRI = new ReceiveImage(lobjReceipt.GetProcessID());
		lopRI.mobjData = toServerData(receipt, lobjReceipt);
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

	public Receipt transferToOwner(String receiptId, OwnerRef newOwner)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		TransferToPolicy lopTTP;
		ProcessData lobjOwner;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjOwner = null;
		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));

			if ( Constants.ObjID_Policy.equals(UUID.fromString(newOwner.ownerTypeId)) )
				lobjOwner = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newOwner.ownerId));
			if ( Constants.ObjID_SubPolicy.equals(UUID.fromString(newOwner.ownerTypeId)) )
				lobjOwner = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newOwner.ownerId));
			if ( Constants.ObjID_SubCasualty.equals(UUID.fromString(newOwner.ownerTypeId)) )
				lobjOwner = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newOwner.ownerId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjOwner == null )
			throw new BigBangException("Entidade incompatível com a transferência de recibos.");

		lopTTP = new TransferToPolicy(lobjReceipt.GetProcessID());
		lopTTP.midNewProcess = lobjOwner.GetProcessID();

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
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receiptId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVR = new ValidateReceipt(lobjReceipt.GetProcessID());
		lopVR.mobjData = null;
		lopVR.mobjDocOps = null;

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
		lopSRTI.midMotive = UUID.fromString(message.motiveId);
//		lopSRTI.mstrSubject = message.subject;
//		lopSRTI.mstrText = message.text;

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

	public Receipt createSecondPaymentNotice(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreateSecondPaymentNotice lopCSPN;

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

		lopCSPN = new CreateSecondPaymentNotice(lobjReceipt.GetProcessID());
		lopCSPN.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopCSPN.mbUseSets = false;

		try
		{
			lopCSPN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetReceipt(lobjReceipt.getKey());
	}

	public Receipt cancelPaymentNotice(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CancelPaymentNotice lopCPN;

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

		lopCPN = new CancelPaymentNotice(lobjReceipt.GetProcessID());

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
				lopP.marrData[i].mdblValue = (info.payments[i].value == null ? null : new BigDecimal(info.payments[i].value+""));
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

	public Receipt voidInternal(Receipt.ReturnMessage message)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		VoidInternal lopVI;

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

		lopVI = new VoidInternal(lobjReceipt.GetProcessID());
		lopVI.midMotive = UUID.fromString(message.motiveId);

		try
		{
			lopVI.Execute();
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

	public Receipt createInternalReceipt(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreateInternalReceipt lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(receiptId));

			lopCIR = new CreateInternalReceipt(lobjReceipt.GetProcessID());

			lopCIR.Execute();
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

	public Receipt returnPayment(String receiptId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		ReturnPayment lopRP;

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

		lopRP = new ReturnPayment(lobjReceipt.GetProcessID());
		lopRP.marrReceiptIDs = new UUID[] {UUID.fromString(receiptId)};
		lopRP.mbUseSets = false;

		try
		{
			lopRP.Execute();
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

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjReceipt.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Receipt,
				lobjReceipt.getKey(), Constants.MsgDir_Outgoing);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public Conversation receiveMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjReceipt.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming ); // On NULL, default is INCOMING
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing ) );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Receipt,
				lobjReceipt.getKey(), lopCC.mobjData.midStartDir);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
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
		ProcessData lobjPolicy;
		CreateReceiptBase lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lopCR = null;
		try
		{
			if ( Constants.ObjID_Policy.equals(UUID.fromString(receipt.ownerTypeId)) )
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.ownerId));
				lopCR = new com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt(lobjPolicy.GetProcessID());
			}
			if ( Constants.ObjID_SubPolicy.equals(UUID.fromString(receipt.ownerTypeId)) )
			{
				lobjPolicy = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.ownerId));
				lopCR = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt(lobjPolicy.GetProcessID());
			}
			if ( Constants.ObjID_SubCasualty.equals(UUID.fromString(receipt.ownerTypeId)) )
			{
				lobjPolicy = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(receipt.ownerId));
				lopCR = new com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateReceipt(lobjPolicy.GetProcessID());
			}

			if ( null == lopCR )
				throw new BigBangException("Tipo de objecto incompatível com criação de recibos.");

			lopCR.mobjData = sClientToServer(receipt);

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
				lopCR.mobjDocOps.marrCreate2 = DocumentServiceImpl.buildTreeLight(receipt.documents);
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
		HashMap<String, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		boolean lbEmail;
		String lstrClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		MasterDB ldb;
		UUID lidC;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		ConversationData lobjConvData;
		CreatePaymentNotice lopCPN;
		Client lobjClient;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation lopCCC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new HashMap<String, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lbEmail = (Constants.ProfID_Email.equals(lobjReceipt.getProfile()) || Constants.ProfID_EmailNoDAS.equals(lobjReceipt.getProfile()));
				lstrClient = (lbEmail ? "E" : "S") + lobjReceipt.getClient().getKey().toString();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrReceipts.get(lstrClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrReceipts.put(lstrClient, larrByClient);
			}
			larrByClient.add(lobjReceipt.getKey());
		}

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidSet = null;
		for(String lstrC : larrReceipts.keySet())
		{
			lbEmail = lstrC.startsWith("E");
			lidC = UUID.fromString(lstrC.substring(1));

			lidSetClient = null;
			lobjDocOps = null;
			lobjConvData = null;
			larrByClient = larrReceipts.get(lstrC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);

			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

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
					lopCPN.mbTryEmail = lbEmail;
					lopCPN.mobjConvData = lobjConvData;

					lopCPN.Execute(ldb);

					lobjConvData = lopCPN.mobjConvData;
					lbEmail = lopCPN.mbTryEmail;
					lobjDocOps = lopCPN.mobjDocOps;
					lidSetClient = lopCPN.midSetDocument;
					lidSet = lopCPN.midSet;
				}
				catch (Throwable e)
				{
					try { ldb.Rollback(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
			}

			if ( lobjConvData != null )
			{
				try
				{
					lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lidC);
					lopCCC = new com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation(lobjClient.GetProcessID());
					lopCCC.mobjData = lobjConvData;
					lopCCC.Execute(ldb);
				}
				catch (Throwable e)
				{
					try { ldb.Rollback(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public void massCreateSecondPaymentNotice(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		MasterDB ldb;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		Boolean lbTryEmail;
		ConversationData lobjConvData;
		CreateSecondPaymentNotice lopCPN;
		Client lobjClient;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation lopCCC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidClient = lobjReceipt.getClient().getKey();
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

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidSet = null;
		for(UUID lidC : larrReceipts.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			lbTryEmail = null;
			lobjConvData = null;
			larrByClient = larrReceipts.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);

			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopCPN = new CreateSecondPaymentNotice(lobjReceipt.GetProcessID());
					lopCPN.marrReceiptIDs = larrFinal;
					lopCPN.mbUseSets = true;
					lopCPN.midSet = lidSet;
					lopCPN.midSetDocument = lidSetClient;
					lopCPN.mobjDocOps = lobjDocOps;
					lopCPN.mbTryEmail = lbTryEmail;
					lopCPN.mobjConvData = lobjConvData;

					lopCPN.Execute(ldb);

					lobjConvData = lopCPN.mobjConvData;
					lbTryEmail = lopCPN.mbTryEmail;
					lobjDocOps = lopCPN.mobjDocOps;
					lidSetClient = lopCPN.midSetDocument;
					lidSet = lopCPN.midSet;
				}
				catch (Throwable e)
				{
					try { ldb.Rollback(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
			}

			if ( lobjConvData != null )
			{
				try
				{
					lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lidC);
					lopCCC = new com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation(lobjClient.GetProcessID());
					lopCCC.mobjData = lobjConvData;
					lopCCC.Execute(ldb);
				}
				catch (Throwable e)
				{
					try { ldb.Rollback(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public void massCreateSignatureRequest(String[] receiptIds, int replylimit)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		CreateSignatureRequest lopCSR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidClient = lobjReceipt.getClient().getKey();
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

					lopCSR = new CreateSignatureRequest(lobjReceipt.GetProcessID());
					lopCSR.marrReceiptIDs = larrFinal;
					lopCSR.mlngDays = replylimit;
					lopCSR.mbUseSets = true;
					lopCSR.midSet = lidSet;
					lopCSR.midSetDocument = lidSetClient;
					lopCSR.mobjDocOps = lobjDocOps;

					lopCSR.Execute();

					lobjDocOps = lopCSR.mobjDocOps;
					lidSetClient = lopCSR.midSetDocument;
					lidSet = lopCSR.midSet;
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
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
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

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidClient = lobjReceipt.getClient().getKey();
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

	public void massCreateInternalReceipt(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		CreateInternalReceipt lopCIR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));

				lopCIR = new CreateInternalReceipt(lobjReceipt.GetProcessID());

				lopCIR.Execute();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}
	}

	public void massSendPayment(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
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

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidClient = lobjReceipt.getClient().getKey();
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

	public void massInsurerAccounting(String[] receiptIds, InsurerAccountingExtra[] extraInfo)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		UUID lidInsurer;
		ArrayList<UUID> larrByInsurer;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidMap;
		String lstrExtraText;
		BigDecimal ldblExtraValue;
		Boolean lbIsCommissions;
		Boolean lbHasTax;
		InsurerAccounting lopIA;
		InsurerAccountingSet lobjSet;
		InsurerAccountingMap lobjMap;
		MasterDB ldb;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidInsurer = lobjReceipt.getAbsolutePolicy().GetCompany().getKey();
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

		if ( extraInfo != null )
		{
			for ( i = 0; i < extraInfo.length; i++ )
			{
				lidInsurer = UUID.fromString(extraInfo[i].insurerId);
				if ( larrReceipts.get(lidInsurer) == null )
					larrReceipts.put(lidInsurer, new ArrayList<UUID>());
			}
		}

		lidSet = null;
		for(UUID lidI : larrReceipts.keySet())
		{
			larrByInsurer = larrReceipts.get(lidI);
			larrFinal = larrByInsurer.toArray(new UUID[larrByInsurer.size()]);
			if ( larrFinal.length == 0 )
				continue;

			lstrExtraText = null;
			ldblExtraValue = null;
			lbIsCommissions = null;
			lbHasTax = null;
			if ( extraInfo != null )
			{
				for ( i = 0; i < extraInfo.length; i++ )
				{
					if ( lidI.equals(UUID.fromString(extraInfo[i].insurerId)) )
					{
						lstrExtraText = extraInfo[i].text;
						ldblExtraValue = ( extraInfo[i].value == null ? null : new BigDecimal(extraInfo[i].value + "") );
						lbIsCommissions = extraInfo[i].isCommissions;
						lbHasTax = extraInfo[i].hasTax;
						break;
					}
				}
			}

			lidMap = null;
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopIA = new InsurerAccounting(lobjReceipt.GetProcessID());
					lopIA.midSet = lidSet;
					lopIA.midMap = lidMap;
					lopIA.mstrExtraText = lstrExtraText;
					lopIA.mdblExtraValue = ldblExtraValue;
					lopIA.mbIsCommissions = lbIsCommissions;
					lopIA.mbHasTax = lbHasTax;

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

		ldb = null;
		for(UUID lidI : larrReceipts.keySet())
		{
			larrByInsurer = larrReceipts.get(lidI);
			larrFinal = larrByInsurer.toArray(new UUID[larrByInsurer.size()]);
			if ( larrFinal.length != 0 )
				continue;

			lstrExtraText = null;
			ldblExtraValue = null;
			lbIsCommissions = null;
			lbHasTax = null;
			if ( extraInfo != null )
			{
				for ( i = 0; i < extraInfo.length; i++ )
				{
					if ( lidI.equals(UUID.fromString(extraInfo[i].insurerId)) )
					{
						lstrExtraText = extraInfo[i].text;
						ldblExtraValue = ( extraInfo[i].value == null ? null : new BigDecimal(extraInfo[i].value + "") );
						lbIsCommissions = extraInfo[i].isCommissions;
						lbHasTax = extraInfo[i].hasTax;
						break;
					}
				}
			}
			if ( ldblExtraValue == null )
				continue;

			if ( ldb == null )
			{
				try
				{
					ldb = new MasterDB();
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}

			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try {ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			try
			{
				if ( lidSet == null )
				{
					lobjSet = InsurerAccountingSet.GetInstance(Engine.getCurrentNameSpace(), null);
					lobjSet.setAt(0, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(1, Engine.getCurrentUser());
					lobjSet.setAt(2, lobjSet.GetNewSetNumber(ldb));
					lobjSet.SaveToDb(ldb);
					lidSet = lobjSet.getKey();
				}

				lobjMap = InsurerAccountingMap.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjMap.setAt(InsurerAccountingMap.I.SET, lidSet);
				lobjMap.setAt(InsurerAccountingMap.I.OWNER, lidI);
				lobjMap.setAt(InsurerAccountingMap.I.SETTLEDON, (Timestamp)null);
				lobjMap.setAt(InsurerAccountingMap.I.EXTRATEXT, lstrExtraText);
				lobjMap.setAt(InsurerAccountingMap.I.EXTRAVALUE, ldblExtraValue);
				lobjMap.setAt(InsurerAccountingMap.I.ISCOMMISSION, lbIsCommissions);
				lobjMap.setAt(InsurerAccountingMap.I.HASTAX, lbHasTax);
				lobjMap.SaveToDb(ldb);
			}
			catch (Throwable e)
			{
				try {ldb.Rollback(); } catch (Throwable e1) {}
				try {ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try {ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}
		}
		if ( ldb != null )
		{
			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}
	}

	public void massMediatorAccounting(String[] receiptIds)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		UUID lidMediator;
		ArrayList<UUID> larrByMediator;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidMap;
		MediatorAccounting lopMA;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidMediator = lobjReceipt.getMediator().getKey();
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
		HashMap<UUID, ArrayList<UUID>> larrReceipts;
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
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

		larrReceipts = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < receiptIds.length; i++ )
		{
			try
			{
				lobjReceipt = com.premiumminds.BigBang.Jewel.Objects.Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(receiptIds[i]));
				lidInsurer = lobjReceipt.getAbsolutePolicy().GetCompany().getKey();
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
				"[:End Date]", "[:Description]", "[:Status]", "[:Due Date]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
		filterByMediator(pstrBuffer, pidMediator.toString());
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
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
			pstrBuffer.append("(CAST([:Total Premium] AS NVARCHAR(20)) = N'").append(lstrAux).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Policy:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Policy:SubLine:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Policy:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Policy:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Policy:Client:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Policy:Client:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Subscriber:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Sub Policy:Subscriber:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")");
		}

		if ( lParam.companyId != null )
		{
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Policy:Company] = '").append(lParam.companyId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:Company] = '").append(lParam.companyId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Policy] IN (SELECT [PK] FROM (");
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
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Sub Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE [:Policy:Company] = '").append(lParam.companyId).append("'))");
			pstrBuffer.append(")");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.mediatorId != null )
		{
			filterByMediator(pstrBuffer, lParam.mediatorId);
		}

		if ( lParam.ownerId != null )
		{
			if ( (lParam.ownerTypeId == null) || Constants.ObjID_Policy.equals(UUID.fromString(lParam.ownerTypeId)) )
				pstrBuffer.append(" AND [:Policy] = '").append(lParam.ownerId).append("'");
			else if ( Constants.ObjID_SubPolicy.equals(UUID.fromString(lParam.ownerTypeId)) )
				pstrBuffer.append(" AND [:Sub Policy] = '").append(lParam.ownerId).append("'");
			else if ( Constants.ObjID_SubCasualty.equals(UUID.fromString(lParam.ownerTypeId)) )
				pstrBuffer.append(" AND [:Sub Casualty] = '").append(lParam.ownerId).append("'");
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
				pstrBuffer.append("' AND [:Timestamp] >= '").append(lParam.paymentFrom);
			if ( lParam.paymentTo != null )
			{
	        	ldtAux = Calendar.getInstance();
	        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.paymentTo + " 00:00:00.0").getTime());
	        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
	        	pstrBuffer.append("' AND [:Timestamp] < '");
	        	pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10));
			}
			pstrBuffer.append("')");
		}

		if ( lParam.subLineId != null )
		{
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Policy:SubLine] = '").append(lParam.subLineId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine] = '").append(lParam.subLineId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine] = '").append(lParam.subLineId).append("'))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Sub Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE [:Policy:SubLine] = '").append(lParam.subLineId).append("'))");
			pstrBuffer.append(")");
		}
		else if ( lParam.lineId != null )
		{
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Policy:SubLine:Line] = '").append(lParam.lineId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine:Line] = '").append(lParam.lineId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line] = '").append(lParam.lineId).append("'))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Sub Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE [:Policy:SubLine:Line] = '").append(lParam.lineId).append("'))");
			pstrBuffer.append(")");
		}
		else if ( lParam.categoryId != null )
		{
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Policy:SubLine:Line:Category] = '").append(lParam.categoryId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Policy:Policy:SubLine:Line:Category] = '").append(lParam.categoryId).append("')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line:Category] = '").append(lParam.categoryId).append("'))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Sub Casualty:Sub Policy] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE [:Policy:SubLine:Line:Category] = '").append(lParam.categoryId).append("'))");
			pstrBuffer.append(")");
		}

		if ( lParam.internalOnly )
		{
			pstrBuffer.append(" AND [:Is Internal] = 1");
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
		com.premiumminds.BigBang.Jewel.Objects.Receipt lobjReceipt;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		SubCasualty lobjSubCasualty;
		Company lobjCompany;
		Client lobjClient;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;
		ReceiptStub lobjResult;

		lobjProcess = null;
		lobjReceipt = null;
		lobjPolicy = null;
		lobjSubPolicy = null;
		lobjSubCasualty = null;
		lobjCompany = null;
		lobjSubLine = null;
		lobjLine = null;
		lobjCategory = null;
		lobjClient = null;
		lobjStatus = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjReceipt = (com.premiumminds.BigBang.Jewel.Objects.Receipt)lobjProcess.GetData();
			lobjClient = lobjReceipt.getClient();
			lobjSubCasualty = lobjReceipt.getSubCasualty();
			lobjSubPolicy = lobjReceipt.getSubPolicy();
			lobjPolicy = lobjReceipt.getAbsolutePolicy();
			lobjSubLine = lobjPolicy.GetSubLine();
			lobjLine = lobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
			lobjCompany = lobjPolicy.GetCompany();
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus),
					getStatus((UUID)parrValues[8], (Timestamp)parrValues[9]));
		}
		catch (Throwable e)
		{
		}

		lobjResult = new ReceiptStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.insurerId = (lobjCompany == null ? null : lobjCompany.getKey().toString());
		lobjResult.insurerName = (lobjCompany == null ? "(Erro)" : (String)lobjCompany.getAt(1));
		if ( lobjSubCasualty != null )
		{
			lobjResult.ownerId = lobjSubCasualty.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_SubCasualty.toString();
			lobjResult.ownerNumber = lobjSubCasualty.getLabel();
		}
		else if ( lobjSubPolicy != null )
		{
			lobjResult.ownerId = lobjSubPolicy.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_SubPolicy.toString();
			lobjResult.ownerNumber = lobjSubPolicy.getLabel();
		}
		else
		{
			lobjResult.ownerId = lobjPolicy.getKey().toString();
			lobjResult.ownerTypeId = Constants.ObjID_Policy.toString();
			lobjResult.ownerNumber = lobjPolicy.getLabel();
		}
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
		lobjResult.endDate = (parrValues[6] == null ? null : ((Timestamp)parrValues[6]).toString().substring(0, 10));
		lobjResult.description = (String)parrValues[7];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.statusId = (lobjStatus == null ? null : lobjStatus.getKey().toString());
		lobjResult.statusText= (lobjStatus == null ? null : lobjStatus.getLabel());
		if ( lobjStatus == null )
			lobjResult.statusIcon = ReceiptStub.ReceiptStatus.ERROR;
		else
			lobjResult.statusIcon = translateStatus((Integer)lobjStatus.getAt(1));

		return lobjResult;
	}

	private void filterByMediator(StringBuilder pstrBuffer, String pstrMedId)
		throws BigBangException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefGroups;

		pstrBuffer.append(" AND ([:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR ([:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Policy:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR ([:Policy:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Policy:Client:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Policy:Client:Group] IN (SELECT [PK] FROM (");
		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));
			pstrBuffer.append(lrefGroups.SQLForSelectByMembers(new int[] {ClientGroup.I.MEDIATOR}, new java.lang.Object[] {pstrMedId}, null));
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxGrp])))");
		pstrBuffer.append(" OR [:Sub Policy:Subscriber:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Sub Policy:Subscriber:Group] IN (SELECT [PK] FROM (");
		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));
			pstrBuffer.append(lrefGroups.SQLForSelectByMembers(new int[] {ClientGroup.I.MEDIATOR}, new java.lang.Object[] {pstrMedId}, null));
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxGrp])");
		pstrBuffer.append(" OR [:Sub Policy:Policy:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR ([:Sub Policy:Policy:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Sub Policy:Policy:Client:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Sub Policy:Policy:Client:Group] IN (SELECT [PK] FROM (");
		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));
			pstrBuffer.append(lrefGroups.SQLForSelectByMembers(new int[] {ClientGroup.I.MEDIATOR}, new java.lang.Object[] {pstrMedId}, null));
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxGrp])))");
		pstrBuffer.append(" OR [:Sub Casualty:Casualty:Client:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Sub Casualty:Casualty:Client:Group] IN (SELECT [PK] FROM (");
		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));
			pstrBuffer.append(lrefGroups.SQLForSelectByMembers(new int[] {ClientGroup.I.MEDIATOR}, new java.lang.Object[] {pstrMedId}, null));
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxGrp])");
		pstrBuffer.append(" OR [:Sub Casualty:Policy] IN (SELECT [PK] FROM (");
		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxPols] WHERE ([:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR ([:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Client:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Client:Group:Mediator] = '").append(pstrMedId).append("'))))");
		pstrBuffer.append(" OR [:Sub Casualty:Sub Policy] IN (SELECT [PK] FROM (");
		try
		{
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxSPols] WHERE ([:Subscriber:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Subscriber:Group:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Policy:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR ([:Policy:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Policy:Client:Mediator] = '").append(pstrMedId).append("'");
		pstrBuffer.append(" OR [:Policy:Client:Group:Mediator] = '").append(pstrMedId).append("')))))))");
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ReceiptSearchParameter lParam;
		String lstrAux;
//		IEntity lrefPolicies;
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
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ");

//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
//				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Number] LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-100*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Number] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ");
//
//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE CAST([:Client:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-10000*PATINDEX(N'%").append(lstrAux).append("%', CAST((SELECT [:Client:Number] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [AuxPols].[:Process] = [Aux].[:Process:Parent]) AS NVARCHAR(20))) ");
//
//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Client:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Client:Name] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [AuxPols].[:Process] = [Aux].[:Process:Parent])) ");
//
//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-100000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Name] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ");
//
//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-10000000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Line:Name] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ");
//
//			pstrBuffer.append("WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
//					.append("-1000000000000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:SubLine:Line:Category:Name] FROM (");
//			try
//			{
//				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//				throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxPols] WHERE [:Process] = [Aux].[:Process:Parent])) ");

			pstrBuffer.append("ELSE 0 END"); // END"); O END a mais parte do trim por complexidade
		}

		return lbFound;
	}

	private static UUID getStatus(UUID pidStatus, Timestamp pdtLimit)
		throws BigBangException
	{
		if ( (pdtLimit == null) || pdtLimit.compareTo(new Timestamp(new java.util.Date().getTime())) >= 0 )
			return pidStatus;

		if ( Constants.StatusID_Initial.equals(pidStatus) )
			return Constants.StatusID_InitialExpired;

		if ( Constants.StatusID_Payable.equals(pidStatus) )
			return Constants.StatusID_PayableExpired;

		if ( Constants.StatusID_DASPending.equals(pidStatus) )
			return Constants.StatusID_DASExpired;

		if ( Constants.StatusID_SignaturePending.equals(pidStatus) )
			return Constants.StatusID_SignatureExpired;

		return pidStatus;
	}

	private static ReceiptStub.ReceiptStatus translateStatus(int plngStatus)
	{
		switch ( plngStatus )
		{
		case 0:
			return ReceiptStub.ReceiptStatus.WHITE;

		case 1:
			return ReceiptStub.ReceiptStatus.GREEN;

		case 2:
			return ReceiptStub.ReceiptStatus.YELLOW;

		case 3:
			return ReceiptStub.ReceiptStatus.ORANGE;

		case 4:
			return ReceiptStub.ReceiptStatus.RED;

		case 5:
			return ReceiptStub.ReceiptStatus.GRAY;

		default:
			return ReceiptStub.ReceiptStatus.ERROR;
		}
	}

	private static ReceiptData toServerData(Receipt lobjSource, com.premiumminds.BigBang.Jewel.Objects.Receipt lobjOrig)
	{
		ReceiptData lobjResult = sClientToServer(lobjSource);

		if ( lobjOrig != null )
		{
			lobjResult.mid = lobjOrig.getKey();
			lobjResult.mbInternal = (Boolean)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.ISINTERNAL);
			lobjResult.mlngEntryNumber = (Integer)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.ENTRYNUMBER);
			lobjResult.mlngEntryYear = (Integer)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.ENTRYYEAR);
			lobjResult.midStatus = (UUID)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.STATUS);
			lobjResult.midPolicy = (UUID)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.POLICY);
			lobjResult.midSubPolicy = (UUID)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.SUBPOLICY);
			lobjResult.midSubCasualty = (UUID)lobjOrig.getAt(com.premiumminds.BigBang.Jewel.Objects.Receipt.I.SUBCASUALTY);
		}

		return lobjResult;
	}
}
