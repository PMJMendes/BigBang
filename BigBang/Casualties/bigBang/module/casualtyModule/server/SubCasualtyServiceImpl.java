package bigBang.module.casualtyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;
import bigBang.module.receiptModule.server.ReceiptServiceImpl;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyItemData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateAssessment;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateMedicalFile;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateTotalLoss;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.DeleteSubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.MarkForClosing;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.RejectClosing;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.SendNotification;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class SubCasualtyServiceImpl
	extends SearchServiceBase
	implements SubCasualtyService
{
	private static final long serialVersionUID = 1L;

	public static SubCasualty sGetSubCasualty(UUID pidSubCasualty)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		IProcess lobjProcess;
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ObjectBase lobjOwner;
		Policy lobjPolicy;
		Client lobjClient;
		Mediator lobjMed;
		Company lobjComp;
		SubCasualtyItem[] larrItems;
		SubCasualty lobjResult;
		BigDecimal ldblTotal;
		BigDecimal ldblLocal;
		int i;

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(), pidSubCasualty);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubCasualty.GetProcessID());
			lobjCasualty = lobjSubCasualty.GetCasualty();
			lobjOwner = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY) == null ?
					SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICY)) :
					Policy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY)) );
			lobjPolicy = (Policy)( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) ? lobjOwner :
					PNProcess.GetInstance(Engine.getCurrentNameSpace(), ((SubPolicy)lobjOwner).GetProcessID()).GetParent().GetData() );
			lobjComp = lobjPolicy.GetCompany();
			lobjClient = lobjPolicy.GetClient();
			lobjMed = lobjClient.getMediator();
			larrItems = lobjSubCasualty.GetCurrentItems();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new SubCasualty();
		lobjResult.id = lobjSubCasualty.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.number = lobjSubCasualty.getLabel();
		lobjResult.casualtyId = lobjCasualty.getKey().toString();
		lobjResult.referenceId = lobjOwner.getKey().toString();
		lobjResult.referenceTypeId = lobjOwner.getDefinition().getDefObject().getKey().toString();
		lobjResult.referenceNumber = lobjOwner.getLabel();
		lobjResult.categoryName = lobjPolicy.GetSubLine().getLine().getCategory().getLabel();
		lobjResult.lineName = lobjPolicy.GetSubLine().getLine().getLabel();
		lobjResult.subLineName = lobjPolicy.GetSubLine().getLabel();
		lobjResult.insurerProcessNumber = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.INSURERPROCESS);
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.hasJudicial = (Boolean)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.HASJUDICIAL);
		lobjResult.text = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.DESCRIPTION);
		lobjResult.managerId = lobjProcess.GetManagerID().toString();
		lobjResult.internalNotes = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.NOTES);
		lobjResult.inheritCasualtyNumber = lobjCasualty.getLabel();
		lobjResult.inheritInsurerId = lobjComp.getKey().toString();
		lobjResult.inheritInsurerName = lobjComp.getLabel();
		lobjResult.inheritMasterClientId = lobjClient.getKey().toString();
		lobjResult.inheritMasterClientNumber = ((Integer)lobjClient.getAt(Client.I.NUMBER)).toString();
		lobjResult.inheritMasterClientName = lobjClient.getLabel();
		lobjResult.inheritMasterMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMasterMediatorName = lobjMed.getLabel();
		if ( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) )
			lobjResult.insuredObjectId = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICYOBJECT) == null ?
					null : ((UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICYOBJECT)).toString() );
		else
			lobjResult.insuredObjectId = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICYOBJECT) == null ?
					null : ((UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICYOBJECT)).toString() );
		lobjResult.insuredObjectName = (String)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.GENERICOBJECT);
		lobjResult.serviceCenterId = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SERVICECENTER) == null ?
				null : ((UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SERVICECENTER)).toString() );

		ldblTotal = null;
		lobjResult.items = new SubCasualty.SubCasualtyItem[larrItems.length];
		for ( i = 0; i < lobjResult.items.length; i++ )
		{
			ldblLocal = (BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.DAMAGES);

			lobjResult.items[i] = new SubCasualty.SubCasualtyItem();
			lobjResult.items[i].id = larrItems[i].getKey().toString();
			if ( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) )
				lobjResult.items[i].coverageId = ( larrItems[i].getAt(SubCasualtyItem.I.POLICYCOVERAGE) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.POLICYCOVERAGE)).toString() );
			else
				lobjResult.items[i].coverageId = ( larrItems[i].getAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE) == null ? null :
						((UUID)larrItems[i].getAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE)).toString() );
			lobjResult.items[i].damageTypeId = ( larrItems[i].getAt(SubCasualtyItem.I.TYPE) == null ? null :
					((UUID)larrItems[i].getAt(SubCasualtyItem.I.TYPE)).toString() );
			lobjResult.items[i].damages = ( ldblLocal == null ? null : ldblLocal.doubleValue());
			lobjResult.items[i].settlement = ( larrItems[i].getAt(SubCasualtyItem.I.SETTLEMENT) == null ? null :
					((BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.SETTLEMENT)).doubleValue() );
			lobjResult.items[i].isManual = (Boolean)larrItems[i].getAt(SubCasualtyItem.I.MANUAL);
			lobjResult.items[i].value = ( larrItems[i].getAt(SubCasualtyItem.I.CAPITAL) == null ? null :
					((BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.CAPITAL)).doubleValue() );
			lobjResult.items[i].deductible = ( larrItems[i].getAt(SubCasualtyItem.I.DEDUCTIBLE) == null ? null :
					((BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.DEDUCTIBLE)).doubleValue() );
			lobjResult.items[i].injuryCauseId = ( larrItems[i].getAt(SubCasualtyItem.I.INJURYCAUSE) == null ? null :
					((UUID)larrItems[i].getAt(SubCasualtyItem.I.INJURYCAUSE)).toString() );
			lobjResult.items[i].injuryTypeId = ( larrItems[i].getAt(SubCasualtyItem.I.INJURYTYPE) == null ? null :
					((UUID)larrItems[i].getAt(SubCasualtyItem.I.INJURYTYPE)).toString() );
			lobjResult.items[i].injuredPartId = ( larrItems[i].getAt(SubCasualtyItem.I.INJUREDPART) == null ? null :
					((UUID)larrItems[i].getAt(SubCasualtyItem.I.INJUREDPART)).toString() );
			lobjResult.items[i].isThirdParty = (Boolean)larrItems[i].getAt(SubCasualtyItem.I.THIRDPARTY);
			lobjResult.items[i].notes = (String)larrItems[i].getAt(SubCasualtyItem.I.NOTES);

			ldblTotal = ( ldblTotal == null ? ldblLocal : (ldblLocal == null ? ldblTotal : ldblTotal.add(ldblLocal)) );
		}
		lobjResult.totalDamages = (ldblTotal == null ? null : ldblTotal.toPlainString());

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public SubCasualty getSubCasualty(String subCasualtyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetSubCasualty(UUID.fromString(subCasualtyId));
	}

	public SubCasualty editSubCasualty(SubCasualty subCasualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		boolean lbPolicy;
		ManageData lopMD;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualty.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbPolicy = (Constants.ObjID_Policy.equals(UUID.fromString(subCasualty.referenceTypeId)));

		lopMD = new ManageData(lobjSubCasualty.GetProcessID());
		lopMD.mobjData = new SubCasualtyData();
		lopMD.mobjData.mid = lobjSubCasualty.getKey();
		lopMD.mobjData.mstrNumber = subCasualty.number;
		lopMD.mobjData.midProcess = UUID.fromString(subCasualty.processId);
		lopMD.mobjData.midPolicy = ( lbPolicy ? UUID.fromString(subCasualty.referenceId) : null );
		lopMD.mobjData.midSubPolicy = ( lbPolicy ? null : UUID.fromString(subCasualty.referenceId) );
		lopMD.mobjData.mstrInsurerProc = subCasualty.insurerProcessNumber;
		lopMD.mobjData.mstrDescription = subCasualty.text;
		lopMD.mobjData.mstrNotes = subCasualty.internalNotes;
		lopMD.mobjData.mbHasJudicial = subCasualty.hasJudicial;
		lopMD.mobjData.midPolicyObject = ( subCasualty.insuredObjectId == null ? null :
				(lbPolicy ? UUID.fromString(subCasualty.insuredObjectId) : null) );
		lopMD.mobjData.midSubPolicyObject = ( subCasualty.insuredObjectId == null ? null :
				(lbPolicy ? null : UUID.fromString(subCasualty.insuredObjectId)) );
		lopMD.mobjData.mstrGenericObject = subCasualty.insuredObjectName;
		lopMD.mobjData.midCasualty = (UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.CASUALTY);
		lopMD.mobjData.midServiceCenter = ( subCasualty.serviceCenterId == null ? null :
				UUID.fromString(subCasualty.serviceCenterId) );

		if ( subCasualty.items != null )
		{
			lopMD.mobjData.marrItems = new SubCasualtyItemData[subCasualty.items.length];
			for ( i = 0; i < lopMD.mobjData.marrItems.length; i++ )
			{
				lopMD.mobjData.marrItems[i] = new SubCasualtyItemData();
				lopMD.mobjData.marrItems[i].mid = ( subCasualty.items[i].id == null ? null : UUID.fromString(subCasualty.items[i].id) );
				lopMD.mobjData.marrItems[i].midSubCasualty = lopMD.mobjData.mid;
				lopMD.mobjData.marrItems[i].midPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? UUID.fromString(subCasualty.items[i].coverageId) : null) );
				lopMD.mobjData.marrItems[i].midSubPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? null : UUID.fromString(subCasualty.items[i].coverageId)) );
				lopMD.mobjData.marrItems[i].midType = ( subCasualty.items[i].damageTypeId == null ? null :
						UUID.fromString(subCasualty.items[i].damageTypeId) );
				lopMD.mobjData.marrItems[i].mdblDamages = ( subCasualty.items[i].damages == null ? null :
						new BigDecimal(subCasualty.items[i].damages+"") );
				lopMD.mobjData.marrItems[i].mdblSettlement = ( subCasualty.items[i].settlement == null ? null :
						new BigDecimal(subCasualty.items[i].settlement+"") );
				lopMD.mobjData.marrItems[i].mbIsManual = subCasualty.items[i].isManual;
				lopMD.mobjData.marrItems[i].mdblCapital = ( subCasualty.items[i].value == null ? null :
						new BigDecimal(subCasualty.items[i].value + "") );
				lopMD.mobjData.marrItems[i].mdblDeductible = ( subCasualty.items[i].deductible == null ? null :
						new BigDecimal(subCasualty.items[i].deductible + "") );
				lopMD.mobjData.marrItems[i].midInjuryCause = ( subCasualty.items[i].injuryCauseId == null ? null :
						UUID.fromString(subCasualty.items[i].injuryCauseId) );
				lopMD.mobjData.marrItems[i].midInjuryType = ( subCasualty.items[i].injuryTypeId == null ? null :
						UUID.fromString(subCasualty.items[i].injuryTypeId) );
				lopMD.mobjData.marrItems[i].midInjuredPart = ( subCasualty.items[i].injuredPartId == null ? null :
						UUID.fromString(subCasualty.items[i].injuredPartId) );
				lopMD.mobjData.marrItems[i].mbThirdParty = subCasualty.items[i].isThirdParty;
				lopMD.mobjData.marrItems[i].mstrNotes = subCasualty.items[i].notes;

				lopMD.mobjData.marrItems[i].mbNew = ( !subCasualty.items[i].deleted && (subCasualty.items[i].id == null) );
				lopMD.mobjData.marrItems[i].mbDeleted = subCasualty.items[i].deleted;
			}
		}
		else
			lopMD.mobjData.marrItems = null;

		lopMD.mobjContactOps = null;
		lopMD.mobjDocOps = null;

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lopMD.mobjData.mid);
	}

	public SubCasualty sendNotification(String subCasualtyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		SendNotification lopSN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		lopSN = new SendNotification(lobjSubCasualty.GetProcessID());

		try
		{
			lopSN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public Receipt createReceipt(String subCasualtyId, Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));

			lopCR = new CreateReceipt(lobjSubCasualty.GetProcessID());
			lopCR.mobjData = ReceiptServiceImpl.sClientToServer(receipt);

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

		return ReceiptServiceImpl.sGetReceipt(lopCR.mobjData.mid);
	}

	public Assessment createAssessment(Assessment assessment)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateAssessment lopCA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(assessment.subCasualtyId));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCA = new CreateAssessment(lobjSubCasualty.GetProcessID());
		lopCA.mobjData = AssessmentServiceImpl.sParseClient(assessment);

		try
		{
			lopCA.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return AssessmentServiceImpl.sGetAssessment(lopCA.mobjData.mid);
	}

	public MedicalFile createMedicalFile(MedicalFile file)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateMedicalFile lopCMF;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(file.subCasualtyId));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCMF = new CreateMedicalFile(lobjSubCasualty.GetProcessID());
		lopCMF.mobjData = MedicalFileServiceImpl.sParseClient(file);

		try
		{
			lopCMF.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return MedicalFileServiceImpl.sGetMedicalFile(lopCMF.mobjData.mid);
	}

	public TotalLossFile createTotalLoss(TotalLossFile file)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CreateTotalLoss lopCTL;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(file.subCasualtyId));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCTL = new CreateTotalLoss(lobjSubCasualty.GetProcessID());
		lopCTL.mobjData = TotalLossServiceImpl.sParseClient(file);

		try
		{
			lopCTL.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return TotalLossServiceImpl.sGetTotalLoss(lopCTL.mobjData.mid);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
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
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjSubCasualty.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				lobjSubCasualty.getKey(), Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
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
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 559 ", e);
		}

		lopCC = new CreateConversation(lobjSubCasualty.GetProcessID());
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
		
		javax.mail.Message storedMessage = null;
		try {
			storedMessage = MailConnector.getStoredMessage();
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() +  " 580 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				lobjSubCasualty.getKey(), lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 591 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public SubCasualty markForClosing(String subCasualtyId, String revisorId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		MarkForClosing lopMFC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMFC = new MarkForClosing(lobjSubCasualty.GetProcessID());
		lopMFC.midReviewer = UUID.fromString(revisorId);

		try
		{
			lopMFC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public SubCasualty closeProcess(String subCasualtyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjSubCasualty.GetProcessID());

		try
		{
			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public SubCasualty rejectClosing(String subCasualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		RejectClosing lopRC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRC = new RejectClosing(lobjSubCasualty.GetProcessID());
		lopRC.mstrReason = reason;

		try
		{
			lopRC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetSubCasualty(lobjSubCasualty.getKey());
	}

	public void deleteSubCasualty(String subCasualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		DeleteSubCasualty lobjDSC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjDSC = new DeleteSubCasualty(lobjSubCasualty.GetProcessID());
		lobjDSC.midSubCasualty = UUID.fromString(subCasualtyId);
		lobjDSC.mstrReason = reason;

		try
		{
			lobjDSC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_SubCasualty;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Insurer Process]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		SubCasualtySearchParameter lParam;
		String lstrAux;

		if ( !(pParam instanceof SubCasualtySearchParameter) )
			return false;
		lParam = (SubCasualtySearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}
		else if ( lParam.closedOnly )
		{
			pstrBuffer.append(" AND [:Process:Running] = 0");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Policy:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Sub Policy:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Insurer Process] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Casualty] = '").append(lParam.ownerId).append("'");
		}

		if ( lParam.casualtyDate != null )
		{
			pstrBuffer.append(" AND [:Casualty:Date] = '").append(lParam.casualtyDate).append("'");
		}

		if ( lParam.clientId != null )
		{
			pstrBuffer.append(" AND [:Casualty:Client] = '").append(lParam.clientId).append("'");
		}

		if ( lParam.policyId != null )
		{
			pstrBuffer.append(" AND [:Policy] = '").append(lParam.policyId).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubCasualtySortParameter lParam;

		if ( !(pParam instanceof SubCasualtySortParameter) )
			return false;
		lParam = (SubCasualtySortParameter)pParam;

		if ( lParam.field == SubCasualtySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == SubCasualtySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSubCasualty;
		IProcess lobjProcess;
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ObjectBase lobjOwner;
		Policy lobjPolicy;
		SubCasualtyItem[] larrItems;
		SubCasualtyStub lobjResult;
		BigDecimal ldblTotal;
		BigDecimal ldblLocal;
		int i;

		lobjSubCasualty = null;
		lobjOwner = null;
		lobjPolicy = null;
		try
		{
			lobjSubCasualty = com.premiumminds.BigBang.Jewel.Objects.SubCasualty.GetInstance(Engine.getCurrentNameSpace(), pid);
			lobjOwner = ( lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY) == null ?
					SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.SUBPOLICY)) :
					Policy.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjSubCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.SubCasualty.I.POLICY)) );
			lobjPolicy = (Policy)( Constants.ObjID_Policy.equals(lobjOwner.getDefinition().getDefObject().getKey()) ? lobjOwner :
					PNProcess.GetInstance(Engine.getCurrentNameSpace(), ((SubPolicy)lobjOwner).GetProcessID()).GetParent().GetData() );
		}
		catch (Throwable e)
		{
		}
		lobjProcess = null;
		lobjCasualty = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjCasualty = lobjSubCasualty.GetCasualty();
		}
		catch (Throwable e)
		{
		}
		larrItems = null;
		try
		{
			larrItems = lobjSubCasualty.GetCurrentItems();
		}
		catch (Throwable e)
		{
		}

		lobjResult = new SubCasualtyStub();
		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.processId = ((UUID)parrValues[1]).toString();
		lobjResult.casualtyId = (lobjCasualty == null ? null : lobjCasualty.getKey().toString());
		lobjResult.referenceId = (lobjOwner == null ? null : lobjOwner.getKey().toString());
		lobjResult.referenceTypeId = (lobjOwner == null ? null : lobjOwner.getDefinition().getDefObject().getKey().toString());
		lobjResult.referenceNumber = (lobjOwner == null ? null : lobjOwner.getLabel());
		lobjResult.categoryName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLine().getCategory().getLabel());
		lobjResult.lineName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLine().getLabel());
		lobjResult.subLineName = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLabel());
		lobjResult.insurerProcessNumber = (String)parrValues[2];
		lobjResult.isOpen = lobjProcess.IsRunning();

		ldblTotal = null;
		if ( larrItems != null )
		{
			for ( i = 0; i < larrItems.length; i++ )
			{
				ldblLocal = (BigDecimal)larrItems[i].getAt(SubCasualtyItem.I.DAMAGES);
				ldblTotal = ( ldblTotal == null ? ldblLocal : (ldblLocal == null ? ldblTotal : ldblTotal.add(ldblLocal)) );
			}
		}
		lobjResult.totalDamages = (ldblTotal == null ? null : ldblTotal.toPlainString());

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubCasualtySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof SubCasualtySearchParameter) )
				continue;
			lParam = (SubCasualtySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Insurer Process] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Insurer Process]) ELSE ")
					.append("CASE WHEN [:Policy:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', [:Policy:Number]) ELSE ")
					.append("CASE WHEN [:Sub Policy:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', [:Sub Policy:Number]) ELSE ")
					.append("0 END END END END");
		}

		return lbFound;
	}
}
