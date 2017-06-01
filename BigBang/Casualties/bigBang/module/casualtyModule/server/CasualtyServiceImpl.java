package bigBang.module.casualtyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.CasualtyService;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.CasualtyData;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingEntitiesData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyFramingHeadingsData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyInsurerRequestData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyItemData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateSubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.DeleteCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ReopenProcess;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ReopenSubCasualty;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class CasualtyServiceImpl
	extends SearchServiceBase
	implements CasualtyService
{
	private static final long serialVersionUID = 1L;

	public static Casualty sGetCasualty(UUID pidCasualty)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		IProcess lobjProcess;
		Client lobjClient;
		Casualty lobjResult;
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSub;
		Mediator lobjMed;
		String lstrCat;
		String lstrObj;

		lstrCat = null;
		lstrObj = null;
		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(), pidCasualty);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjCasualty.GetProcessID());
			lobjClient = lobjCasualty.GetClient();
			lobjSub = lobjCasualty.GetFirstSubCasualty();
			lobjMed = lobjClient.getMediator();
			if ( lobjSub != null )
			{
				lstrCat = lobjSub.GetSubLine().getDescription();
				lstrObj = lobjSub.GetObjectName();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Casualty();
		lobjResult.id = lobjCasualty.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.processNumber = lobjCasualty.getLabel();
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.casualtyDate = ((Timestamp)lobjCasualty.getAt(2)).toString().substring(0, 10);
		lobjResult.policyCategory = lstrCat;
		lobjResult.insuredObject = lstrObj;
		lobjResult.caseStudy = (Boolean)lobjCasualty.getAt(5);
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.description = (String)lobjCasualty.getAt(3);
		lobjResult.internalNotes = (String)lobjCasualty.getAt(4);
		lobjResult.percentFault = (lobjCasualty.getAt(com.premiumminds.BigBang.Jewel.Objects.Casualty.I.PERCENTFAULT)
				== null ? null : ((BigDecimal)lobjCasualty.getAt(
				com.premiumminds.BigBang.Jewel.Objects.Casualty.I.PERCENTFAULT)).doubleValue());
		lobjResult.managerId = lobjProcess.GetManagerID().toString();
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public Casualty getCasualty(String casualtyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetCasualty(UUID.fromString(casualtyId));
	}

	public Casualty editCasualty(Casualty casualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(casualty.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjCasualty.GetProcessID());
		lopMD.mobjData = new CasualtyData();
		lopMD.mobjData.mid = lobjCasualty.getKey();
		lopMD.mobjData.mstrNumber = casualty.processNumber;
		lopMD.mobjData.mdtCasualtyDate = Timestamp.valueOf(casualty.casualtyDate + " 00:00:00.0");
		lopMD.mobjData.mstrDescription = casualty.description;
		lopMD.mobjData.mstrNotes = casualty.internalNotes;
		lopMD.mobjData.mdblPercentFault = (casualty.percentFault == null ? null :
				new BigDecimal(casualty.percentFault + ""));
		lopMD.mobjData.mbCaseStudy = casualty.caseStudy;
		lopMD.mobjData.midProcess = lobjCasualty.GetProcessID();
		lopMD.mobjData.midManager = null;
		lopMD.mobjData.mobjPrevValues = null;
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

		return sGetCasualty(lopMD.mobjData.mid);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ExecMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(transfer.dataObjectIds[0]));

			lobjCMX = new ExecMgrXFer(lobjCasualty.GetProcessID());
			lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjCMX.midMassProcess = null;

			lobjCMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
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
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjCasualty.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Casualty,
				lobjCasualty.getKey(), Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
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
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 288 ", e);
		}

		lopCC = new CreateConversation(lobjCasualty.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming ); // On NULL, default is INCOMING
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing ) );
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Outgoing );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		
		javax.mail.Message storedMessage = null;
		try {
			storedMessage = MailConnector.getStoredMessage();
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 310 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Casualty,
				lobjCasualty.getKey(), lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 321 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public SubCasualty createSubCasualty(SubCasualty subCasualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		boolean lbPolicy;
		CreateSubCasualty lopCSC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subCasualty.casualtyId));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbPolicy = (Constants.ObjID_Policy.equals(UUID.fromString(subCasualty.referenceTypeId)));

		lopCSC = new CreateSubCasualty(lobjCasualty.GetProcessID());
		lopCSC.mobjData = new SubCasualtyData();
		lopCSC.mobjData.mstrNumber = null;
		lopCSC.mobjData.midPolicy = ( lbPolicy ? UUID.fromString(subCasualty.referenceId) : null );
		lopCSC.mobjData.midSubPolicy = ( lbPolicy ? null : UUID.fromString(subCasualty.referenceId) );
		lopCSC.mobjData.mstrInsurerProc = subCasualty.insurerProcessNumber;
		lopCSC.mobjData.mstrDescription = subCasualty.text;
		lopCSC.mobjData.mstrNotes = subCasualty.internalNotes;
		lopCSC.mobjData.mbHasJudicial = subCasualty.hasJudicial;
		lopCSC.mobjData.midPolicyObject = ( subCasualty.insuredObjectId == null ? null :
				(lbPolicy ? UUID.fromString(subCasualty.insuredObjectId) : null) );
		lopCSC.mobjData.midSubPolicyObject = ( subCasualty.insuredObjectId == null ? null :
				(lbPolicy ? null : UUID.fromString(subCasualty.insuredObjectId)) );
		lopCSC.mobjData.mstrGenericObject = subCasualty.insuredObjectName;
		lopCSC.mobjData.midCasualty = lobjCasualty.getKey();
		lopCSC.mobjData.midServiceCenter = ( subCasualty.serviceCenterId == null ? null :
				UUID.fromString(subCasualty.serviceCenterId) );

		if ( subCasualty.items != null )
		{
			lopCSC.mobjData.marrItems = new SubCasualtyItemData[subCasualty.items.length];
			for ( i = 0; i < lopCSC.mobjData.marrItems.length; i++ )
			{
				lopCSC.mobjData.marrItems[i] = new SubCasualtyItemData();
				lopCSC.mobjData.marrItems[i].midPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? UUID.fromString(subCasualty.items[i].coverageId) : null) );
				lopCSC.mobjData.marrItems[i].midSubPolicyCoverage = ( subCasualty.items[i].coverageId == null ? null :
						(lbPolicy ? null : UUID.fromString(subCasualty.items[i].coverageId)) );
				lopCSC.mobjData.marrItems[i].midType = ( subCasualty.items[i].damageTypeId == null ? null :
						UUID.fromString(subCasualty.items[i].damageTypeId) );
				lopCSC.mobjData.marrItems[i].mdblDamages = ( subCasualty.items[i].damages == null ? null :
						new BigDecimal(subCasualty.items[i].damages + "") );
				lopCSC.mobjData.marrItems[i].mdblSettlement = ( subCasualty.items[i].settlement == null ? null :
						new BigDecimal(subCasualty.items[i].settlement + "") );
				lopCSC.mobjData.marrItems[i].mbIsManual = subCasualty.items[i].isManual;
				lopCSC.mobjData.marrItems[i].mdblCapital = ( subCasualty.items[i].value == null ? null :
						new BigDecimal(subCasualty.items[i].value + "") );
				lopCSC.mobjData.marrItems[i].mdblDeductible = ( subCasualty.items[i].deductible == null ? null :
						new BigDecimal(subCasualty.items[i].deductible + "") );
				lopCSC.mobjData.marrItems[i].midInjuryCause = ( subCasualty.items[i].injuryCauseId == null ? null :
						UUID.fromString(subCasualty.items[i].injuryCauseId) );
				lopCSC.mobjData.marrItems[i].midInjuryType = ( subCasualty.items[i].injuryTypeId == null ? null :
						UUID.fromString(subCasualty.items[i].injuryTypeId) );
				lopCSC.mobjData.marrItems[i].midInjuredPart = ( subCasualty.items[i].injuredPartId == null ? null :
						UUID.fromString(subCasualty.items[i].injuredPartId) );
				lopCSC.mobjData.marrItems[i].mbThirdParty = subCasualty.items[i].isThirdParty;
				lopCSC.mobjData.marrItems[i].mstrNotes = subCasualty.items[i].notes;

				lopCSC.mobjData.marrItems[i].mbNew = !subCasualty.items[i].deleted;
				lopCSC.mobjData.marrItems[i].mbDeleted = subCasualty.items[i].deleted;
			}
		}
		else
			lopCSC.mobjData.marrItems = null;
		
		if ( subCasualty.insurerRequests != null ) {
			lopCSC.mobjData.requests = new SubCasualtyInsurerRequestData[subCasualty.insurerRequests.length];
			for ( i = 0; i < lopCSC.mobjData.requests.length; i++ ) {
				lopCSC.mobjData.requests[i] = new SubCasualtyInsurerRequestData();
				lopCSC.mobjData.requests[i].typeId = (subCasualty.insurerRequests[i].insurerRequestTypeId == null ? null :
					UUID.fromString(subCasualty.insurerRequests[i].insurerRequestTypeId));
				lopCSC.mobjData.requests[i].clarificationTypeId = (subCasualty.insurerRequests[i].clarificationTypeId == null ? null :
					UUID.fromString(subCasualty.insurerRequests[i].clarificationTypeId));
				lopCSC.mobjData.requests[i].requestDate = (subCasualty.insurerRequests[i].requestDate == null ? null :
					Timestamp.valueOf(subCasualty.insurerRequests[i].requestDate + " 00:00:00.0"));
				lopCSC.mobjData.requests[i].acceptanceDate = (subCasualty.insurerRequests[i].acceptanceDate == null ? null :
					Timestamp.valueOf(subCasualty.insurerRequests[i].acceptanceDate + " 00:00:00.0"));
				lopCSC.mobjData.requests[i].conforms = subCasualty.insurerRequests[i].conforms;
				lopCSC.mobjData.requests[i].resendDate = (subCasualty.insurerRequests[i].resendDate == null ? null :
					Timestamp.valueOf(subCasualty.insurerRequests[i].resendDate + " 00:00:00.0"));
				lopCSC.mobjData.requests[i].clarificationDate = (subCasualty.insurerRequests[i].clarificationDate == null ? null :
					Timestamp.valueOf(subCasualty.insurerRequests[i].clarificationDate + " 00:00:00.0"));
				
				lopCSC.mobjData.requests[i].isNew = !subCasualty.insurerRequests[i].deleted;
				lopCSC.mobjData.requests[i].isDeleted = subCasualty.insurerRequests[i].deleted;
			}
		} else {
			lopCSC.mobjData.requests = null;
		}
		
		// Framing
		if (subCasualty.framing != null) {
			lopCSC.mobjData.framing = new SubCasualtyFramingData();
			lopCSC.mobjData.framing.analysisDate = (subCasualty.framing.analysisDate == null ? null :
				Timestamp.valueOf(subCasualty.framing.analysisDate + " 00:00:00.0"));
			lopCSC.mobjData.framing.wasDifficult = subCasualty.framing.framingDifficulty;
			lopCSC.mobjData.framing.policyValid = subCasualty.framing.validPolicy;
			lopCSC.mobjData.framing.validityNotes = subCasualty.framing.validityNotes;
			lopCSC.mobjData.framing.areGeneralExclusions = subCasualty.framing.generalExclusions;
			lopCSC.mobjData.framing.generalExclusionsNotes = subCasualty.framing.generalExclusionNotes;
			lopCSC.mobjData.framing.isCoverageRelevant = subCasualty.framing.relevantCoverages;
			lopCSC.mobjData.framing.coverageRelevancyNotes = subCasualty.framing.coverageRelevancyNotes;
			lopCSC.mobjData.framing.coverageValue = (subCasualty.framing.coverageValue == null ? null : new BigDecimal(subCasualty.framing.coverageValue + ""));
			lopCSC.mobjData.framing.areCoverageExclusions = subCasualty.framing.coverageExclusions;
			lopCSC.mobjData.framing.coverageExclusionsNotes = subCasualty.framing.coverageExclusionsNotes;
			lopCSC.mobjData.framing.franchise = (subCasualty.framing.franchise == null ? null : new BigDecimal(subCasualty.framing.franchise + ""));
			lopCSC.mobjData.framing.franchiseType = (subCasualty.framing.deductibleTypeId == null ? null :
				UUID.fromString(subCasualty.framing.deductibleTypeId));
			lopCSC.mobjData.framing.insurerEvaluation = (subCasualty.framing.insurerEvaluationId == null ? null :
				UUID.fromString(subCasualty.framing.insurerEvaluationId));
			lopCSC.mobjData.framing.insurerEvaluationNotes = subCasualty.framing.insurerEvaluationNotes;
			lopCSC.mobjData.framing.expertEvaluation = (subCasualty.framing.expertEvaluationId == null ? null :
				UUID.fromString(subCasualty.framing.expertEvaluationId));
			lopCSC.mobjData.framing.expertEvaluationNotes = subCasualty.framing.expertEvaluationNotes;
			lopCSC.mobjData.framing.coverageNotes = subCasualty.framing.coverageNotes;
			
			if (subCasualty.framing.headings != null) {
				BigDecimal sum = new BigDecimal(0);
				lopCSC.mobjData.framing.framingHeadings = new SubCasualtyFramingHeadingsData();
				lopCSC.mobjData.framing.framingHeadings.baseSalary = subCasualty.framing.headings.baseSalary == null ? null : new BigDecimal(subCasualty.framing.headings.baseSalary);
				lopCSC.mobjData.framing.framingHeadings.feedAllowance = subCasualty.framing.headings.feedAllowance == null ? null : new BigDecimal(subCasualty.framing.headings.feedAllowance);
				lopCSC.mobjData.framing.framingHeadings.otherFees12 = subCasualty.framing.headings.otherFees12 == null ? null : new BigDecimal(subCasualty.framing.headings.otherFees12);
				lopCSC.mobjData.framing.framingHeadings.otherFees14 = subCasualty.framing.headings.otherFees14 == null ? null : new BigDecimal(subCasualty.framing.headings.otherFees14);
				
				if (lopCSC.mobjData.framing.framingHeadings.baseSalary!=null) {
					sum = sum.add(lopCSC.mobjData.framing.framingHeadings.baseSalary);
				}
				if (lopCSC.mobjData.framing.framingHeadings.feedAllowance!=null) {
					sum = sum.add(lopCSC.mobjData.framing.framingHeadings.feedAllowance);
				}
				if (lopCSC.mobjData.framing.framingHeadings.otherFees12!=null) {
					sum = sum.add(lopCSC.mobjData.framing.framingHeadings.otherFees12);
				}
				if (lopCSC.mobjData.framing.framingHeadings.otherFees14!=null) {
					sum = sum.add(lopCSC.mobjData.framing.framingHeadings.otherFees14);
				}
				lopCSC.mobjData.framing.coverageValue = sum;

				lopCSC.mobjData.framing.framingHeadings.isNew = !subCasualty.framing.headings.deleted;
				lopCSC.mobjData.framing.framingHeadings.isDeleted = subCasualty.framing.headings.deleted;
			} else {
				lopCSC.mobjData.framing.framingHeadings = null;
			}
			
			lopCSC.mobjData.framing.isNew = !subCasualty.framing.deleted;
			lopCSC.mobjData.framing.isDeleted = subCasualty.framing.deleted;
			
			if (subCasualty.framing.framingEntities != null) {
				lopCSC.mobjData.framing.framingEntities = new SubCasualtyFramingEntitiesData[subCasualty.framing.framingEntities.length];
				for (i=0; i<lopCSC.mobjData.framing.framingEntities.length; i++) {
					lopCSC.mobjData.framing.framingEntities[i] = new SubCasualtyFramingEntitiesData();
					lopCSC.mobjData.framing.framingEntities[i].entityType = (subCasualty.framing.framingEntities[i].entityTypeId == null ? null :
						UUID.fromString(subCasualty.framing.framingEntities[i].entityTypeId));
					lopCSC.mobjData.framing.framingEntities[i].evaluation = (subCasualty.framing.framingEntities[i].evaluationId == null ? null :
						UUID.fromString(subCasualty.framing.framingEntities[i].evaluationId));
					lopCSC.mobjData.framing.framingEntities[i].notes = subCasualty.framing.framingEntities[i].evaluationNotes;
					
					lopCSC.mobjData.framing.framingEntities[i].isNew = !subCasualty.framing.framingEntities[i].deleted;
					lopCSC.mobjData.framing.framingEntities[i].isDeleted = subCasualty.framing.framingEntities[i].deleted;
				}
			} else {
				subCasualty.framing.framingEntities =null;
			}
		} else {
			lopCSC.mobjData.framing = null;
		}

		lopCSC.mobjContactOps = null;
		lopCSC.mobjDocOps = null;

		try
		{
			lopCSC.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return SubCasualtyServiceImpl.sGetSubCasualty(lopCSC.mobjData.mid);
	}

	public SubCasualty reopenSubCasualty(String casualtyId, String subCasualtyId, String motiveId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ReopenSubCasualty lopRSC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRSC = new ReopenSubCasualty(lobjCasualty.GetProcessID());
		lopRSC.midSubCasualty = UUID.fromString(subCasualtyId);
		lopRSC.midMotive = UUID.fromString(motiveId);

		try
		{
			lopRSC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return SubCasualtyServiceImpl.sGetSubCasualty(lopRSC.midSubCasualty);
	}

	public Casualty closeProcess(String casualtyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		CloseProcess lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjDC = new CloseProcess(lobjCasualty.GetProcessID());

		try
		{
			lobjDC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetCasualty(lobjCasualty.getKey());
	}

	public Casualty reopenProcess(String casualtyId, String motiveId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ReopenProcess lobjRP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjRP = new ReopenProcess(lobjCasualty.GetProcessID());
		lobjRP.midMotive = UUID.fromString(motiveId);

		try
		{
			lobjRP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetCasualty(lobjCasualty.getKey());
	}

	public void deleteCasualty(String casualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		DeleteCasualty lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjDC = new DeleteCasualty(lobjCasualty.GetProcessID());
		lobjDC.midCasualty = UUID.fromString(casualtyId);
		lobjDC.mstrReason = reason;

		try
		{
			lobjDC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		transfer.objectTypeId = Constants.ObjID_Casualty.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_Casualty);
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Casualty;
	}

	protected String[] getColumns()
	{
		IEntity lrefSubs;
		String lstrMulti;
		String lstrSingle;

		lstrMulti = null;
		lstrSingle = null;
		try
		{
			lrefSubs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lstrMulti = lrefSubs.SQLForSelectMulti();
			lstrSingle = lrefSubs.SQLForSelectSingle();
		}
		catch (Throwable e)
		{
			return new String[] {"[:Number]", "[:Process]", "[:Date]", "[:Case Study]", "[:Client]", "[:Client:Name]", "[:Client:Number]", "[:Process:Running]",
					"NULL", "NULL", "NULL", "NULL", "NULL"};
		}

		return new String[] {"[:Number]", "[:Process]", "[:Date]", "[:Case Study]", "[:Client]", "[:Client:Name]", "[:Client:Number]", "[:Process:Running]",
				"(SELECT [:Policy:SubLine:Description] FROM (" + lstrMulti + ") [SC1] WHERE [:Number] = " +
						"(SELECT MIN([:Number]) FROM (" + lstrSingle + ") [SC0] WHERE [:Casualty]=[Aux].[PK]))",
				"(SELECT [:Sub Policy:Policy:SubLine:Description] FROM (" + lstrMulti + ") [SC1] WHERE [:Number] = " +
						"(SELECT MIN([:Number]) FROM (" + lstrSingle + ") [SC0] WHERE [:Casualty]=[Aux].[PK]))",
				"(SELECT [:Policy Object:Name] FROM (" + lstrMulti + ") [SC1] WHERE [:Number] = " +
						"(SELECT MIN([:Number]) FROM (" + lstrSingle + ") [SC0] WHERE [:Casualty]=[Aux].[PK]))",
				"(SELECT [:Sub Policy Object:Name] FROM (" + lstrMulti + ") [SC1] WHERE [:Number] = " +
						"(SELECT MIN([:Number]) FROM (" + lstrSingle + ") [SC0] WHERE [:Casualty]=[Aux].[PK]))",
				"(SELECT [:Generic Object] FROM (" + lstrMulti + ") [SC1] WHERE [:Number] = " +
						"(SELECT MIN([:Number]) FROM (" + lstrSingle + ") [SC0] WHERE [:Casualty]=[Aux].[PK]))"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
		pstrBuffer.append(" AND ([:Client:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Client:Group:Mediator] = '").append(pidMediator.toString()).append("')");
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		CasualtySearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		IEntity lrefPolObjects;
		IEntity lrefSubPolObjects;
        Calendar ldtAux;

		if ( !(pParam instanceof CasualtySearchParameter) )
			return false;
		lParam = (CasualtySearchParameter)pParam;

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
					.append(" OR (LEFT(CONVERT(NVARCHAR, [:Date], 120), 10) LIKE N'%").append(lstrAux).append("%')")
					.append(" OR ([:Client:Name] LIKE N'%").append(lstrAux).append("%')")
					.append(" OR ([:Client:Number] LIKE N'%").append(lstrAux).append("%')")
					.append(" OR [:Process] IN (SELECT [:Process:Parent] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubCasualties] WHERE ([:Insurer Process] LIKE '%").append(lstrAux).append("%'")
					.append(" OR [:Policy:Number] LIKE '%").append(lstrAux).append("%'")
					.append(" OR [:Sub Policy:Number] LIKE '%").append(lstrAux).append("%')))");
		}

		if ( lParam.dateFrom != null )
		{
			pstrBuffer.append(" AND [:Date] >= '").append(lParam.dateFrom).append("'");
		}

		if ( lParam.dateTo != null )
		{
			pstrBuffer.append(" AND [:Date] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.dateTo + " 00:00:00.0").getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		if ( (lParam.caseStudy != null) && lParam.caseStudy )
		{
			pstrBuffer.append(" AND [:Case Study] = 1");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Client] = '").append(lParam.ownerId).append("'");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([PK] IN (SELECT [:Casualty] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubCasualties] WHERE ([:Generic Object] LIKE '%").append(lstrAux).append("%'")
					.append(" OR [:Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%')")
					.append(" OR [:Sub Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefSubPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%'))))");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		CasualtySortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof CasualtySortParameter) )
			return false;
		lParam = (CasualtySortParameter)pParam;

		if ( lParam.field == CasualtySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == CasualtySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == CasualtySortParameter.SortableField.DATE )
			pstrBuffer.append("[:Date]");

		if ( lParam.field == CasualtySortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Name] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == CasualtySortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("(SELECT [:Number] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == CasualtySortParameter.SortableField.MANAGER )
			pstrBuffer.append("[:Process:Manager]");
		
		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		CasualtyStub lobjResult;
//		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSub;
		String lstrCat;
		String lstrObj;

//		lstrCat = null;
//		lstrObj = null;
//		try
//		{
//			lobjSub = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
//					pid).GetFirstSubCasualty();
//			if ( lobjSub != null )
//			{
//				lstrCat = lobjSub.GetSubLine().getDescription();
//				lstrObj = lobjSub.GetObjectName();
//			}
//		}
//		catch (Throwable e)
//		{
//		}

		lstrCat = (String)parrValues[8];
		if (lstrCat == null)
			lstrCat = (String)parrValues[9];

		lstrObj = (String)parrValues[10];
		if (lstrObj == null)
			lstrObj = (String)parrValues[11];
		if (lstrObj == null)
			lstrObj = (String)parrValues[12];

		lobjResult = new CasualtyStub();

		lobjResult.id = pid.toString();
		lobjResult.processNumber = (String)parrValues[0];
		lobjResult.clientId = ((UUID)parrValues[4]).toString();
		lobjResult.clientNumber = ((Integer)parrValues[6]).toString();
		lobjResult.clientName = (String)parrValues[5];
		lobjResult.casualtyDate = ((Timestamp)parrValues[2]).toString().substring(0, 10);
		lobjResult.caseStudy = (Boolean)parrValues[3];
		lobjResult.isOpen = (Boolean)parrValues[7];
		lobjResult.policyCategory = lstrCat;
		lobjResult.insuredObject = lstrObj;
		lobjResult.processId = (parrValues[1] == null ? null : ((UUID)parrValues[1]).toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		CasualtySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof CasualtySearchParameter) )
				continue;
			lParam = (CasualtySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', CAST((SELECT [:Number] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent]) AS NVARCHAR(20))) ELSE ")
					.append("0 END END END");
		}

		return lbFound;
	}
}
