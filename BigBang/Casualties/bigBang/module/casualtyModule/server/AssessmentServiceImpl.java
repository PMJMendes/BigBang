package bigBang.module.casualtyModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.AssessmentStub;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.AssessmentService;
import bigBang.module.casualtyModule.shared.AssessmentSearchParameter;
import bigBang.module.casualtyModule.shared.AssessmentSortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.AssessmentData;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.ManageData;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class AssessmentServiceImpl
	extends SearchServiceBase
	implements AssessmentService
{
	private static final long serialVersionUID = 1L;

	public static Assessment sGetAssessment(UUID pidAssessment)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
		SubCasualty lobjSubC;
		Client lobjCli;
		String lstrObj;
		IProcess lobjProcess;
		Assessment lobjResult;

		try
		{
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(), pidAssessment);
			lobjSubC = SubCasualty.GetInstance(Engine.getCurrentNameSpace(),
					(UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY));
			lobjCli = lobjSubC.GetCasualty().GetClient();
			lstrObj = lobjSubC.GetObjectName();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjAssessment.GetProcessID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Assessment();
		lobjResult.id = lobjAssessment.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.reference = lobjAssessment.getLabel();
		lobjResult.scheduledDate = (lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SCHEDULEDDATE) == null ? null :
			((Timestamp)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SCHEDULEDDATE)).toString().substring(0, 10) );
		lobjResult.effectiveDate = (lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.EFFECTIVEDATE) == null ? null :
			((Timestamp)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.EFFECTIVEDATE)).toString().substring(0, 10) );
		lobjResult.inheritClientName = lobjCli.getLabel();
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = lobjProcess.IsRunning();
		lobjResult.subCasualtyId = lobjSubC.getKey().toString();
		lobjResult.subCasualtyNumber = lobjSubC.getLabel();
		lobjResult.isConditional = (Boolean)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.ISCONDITIONAL);
		lobjResult.isTotalLoss = (Boolean)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.ISTOTALLOSS);
		lobjResult.notes = (String)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.NOTES);

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public static AssessmentData sParseClient(Assessment pobjSource)
	{
		AssessmentData lobjResult;

		lobjResult = new AssessmentData();

		lobjResult.mid = (pobjSource.id == null ? null : UUID.fromString(pobjSource.id));
		lobjResult.mstrReference = pobjSource.reference;
		lobjResult.midSubCasualty = UUID.fromString(pobjSource.subCasualtyId);
		lobjResult.midProcess = (pobjSource.processId == null ? null : UUID.fromString(pobjSource.processId));
		lobjResult.mdtScheduledDate = (pobjSource.scheduledDate == null ? null :
				Timestamp.valueOf(pobjSource.scheduledDate + " 00:00:00.0"));
		lobjResult.mdtEffectiveDate = (pobjSource.effectiveDate == null ? null :
				Timestamp.valueOf(pobjSource.effectiveDate + " 00:00:00.0"));
		lobjResult.mbConditional = pobjSource.isConditional;
		lobjResult.mbTotalLoss = pobjSource.isTotalLoss;
		lobjResult.mstrNotes = pobjSource.notes;

		return lobjResult;
	}

	public Assessment getAssessment(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetAssessment(UUID.fromString(id));
	}

	public Assessment editAssessment(Assessment assessment)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(assessment.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjAssessment.GetProcessID());
		lopMD.mobjData = sParseClient(assessment);

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetAssessment(lopMD.mobjData.mid);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
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
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjAssessment.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;
		
		lopCC.isSend = true;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY),
				Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
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
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 246 ", e);
		}

		lopCC = new CreateConversation(lobjAssessment.GetProcessID());
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
			storedMessage = MailConnector.getStoredMessage(null, conversation.messages[0].emailId);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 267 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY),
				lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 278 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public Assessment closeProcess(String id)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjAssessment.GetProcessID());

		try
		{
			lopCP.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetAssessment(lobjAssessment.getKey());
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Assessment;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Reference]", "[:Process]", "[:Scheduled Date]", "[:Effective Date]", "[:Process:Running]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		AssessmentSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		IEntity lrefPolObjects;
		IEntity lrefSubPolObjects;

		if ( !(pParam instanceof AssessmentSearchParameter) )
			return false;
		lParam = (AssessmentSearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Reference] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR (LEFT(CONVERT(NVARCHAR, [:Scheduled Date], 120), 10) LIKE N'%").append(lstrAux).append("%')")
					.append(" OR (LEFT(CONVERT(NVARCHAR, [:Effective Date], 120), 10) LIKE N'%").append(lstrAux).append("%')")
					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE ([:Casualty:Client:Name] LIKE '%").append(lstrAux).append("%')))");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
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
		AssessmentSortParameter lParam;
		IEntity lrefSubCasualties;

		if ( !(pParam instanceof AssessmentSortParameter) )
			return false;
		lParam = (AssessmentSortParameter)pParam;

		if ( lParam.field == AssessmentSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == AssessmentSortParameter.SortableField.REFERENCE )
			pstrBuffer.append("[:Reference]");

		if ( lParam.field == AssessmentSortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Casualty:Client:Name] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE [:Process] = [Aux].[:Process:Parent])");
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
		AssessmentStub lobjResult;
		SubCasualty lobjSub;
		Client lobjCli;
		String lstrObj;
		
		lobjProcess = null;
		lobjCli = null;
		lstrObj = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjSub = (SubCasualty)lobjProcess.GetParent().GetData();
			lobjCli = lobjSub.GetCasualty().GetClient();
			lstrObj = lobjSub.GetObjectName();
		}
		catch (Throwable e)
		{
		}

		lobjResult = new AssessmentStub();

		lobjResult.id = pid.toString();
		lobjResult.reference = (String)parrValues[0];
		lobjResult.scheduledDate = ( parrValues[2] == null ? null : ((Timestamp)parrValues[2]).toString().substring(0, 10) );
		lobjResult.effectiveDate = ( parrValues[3] == null ? null : ((Timestamp)parrValues[3]).toString().substring(0, 10) );
		lobjResult.inheritClientName = (lobjCli == null ? "(Erro a obter o nome do cliente.)" : lobjCli.getLabel());
		lobjResult.inheritObjectName = lstrObj;
		lobjResult.isRunning = ((Boolean)parrValues[4]);

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		AssessmentSearchParameter lParam;
		String lstrAux;
		IEntity lrefSubCasualties;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof AssessmentSearchParameter) )
				continue;
			lParam = (AssessmentSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Reference] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Reference]) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefSubCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC] WHERE ([:Casualty:Client:Name] LIKE '%").append(lstrAux).append("%')) THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Casualty:Client:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefSubCasualties.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubC2] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ");
			
			pstrBuffer.append("CASE WHEN (LEFT(CONVERT(NVARCHAR, [:Scheduled Date], 120), 10) LIKE N'%")
					.append(lstrAux).append("%') THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux)
					.append("%', LEFT(CONVERT(NVARCHAR, [:Scheduled Date], 120), 10)) ELSE ");
			
			pstrBuffer.append("CASE WHEN (LEFT(CONVERT(NVARCHAR, [:Effective Date], 120), 10) LIKE N'%")
					.append(lstrAux).append("%')")
					.append("-1000000000*PATINDEX(N'%").append(lstrAux)
					.append("%', LEFT(CONVERT(NVARCHAR, [:Effective Date], 120), 10)) ELSE ");
			
			pstrBuffer.append("0 END END END END");
		}

		return lbFound;
	}
}
