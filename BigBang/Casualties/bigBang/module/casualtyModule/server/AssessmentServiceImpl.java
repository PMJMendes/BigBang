package bigBang.module.casualtyModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.server.MessageBridge;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.AssessmentService;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.AssessmentData;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Assessment.ManageData;

public class AssessmentServiceImpl
	extends EngineImplementor
	implements AssessmentService
{
	private static final long serialVersionUID = 1L;

	public static Assessment sGetAssessment(UUID pidAssessment)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Assessment lobjAssessment;
		IProcess lobjProcess;
		Assessment lobjResult;

		try
		{
			lobjAssessment = com.premiumminds.BigBang.Jewel.Objects.Assessment.GetInstance(Engine.getCurrentNameSpace(), pidAssessment);
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
		lobjResult.subCasualtyId = ((UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY)).toString();
		lobjResult.scheduledDate = (lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SCHEDULEDDATE) == null ? null :
				((Timestamp)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SCHEDULEDDATE)).toString().substring(0, 10) );
		lobjResult.effectiveDate = (lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.EFFECTIVEDATE) == null ? null :
			((Timestamp)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.EFFECTIVEDATE)).toString().substring(0, 10) );
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
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY),
				Constants.MsgDir_Outgoing);

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
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjAssessment.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Incoming;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Outgoing );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_SubCasualty,
				(UUID)lobjAssessment.getAt(com.premiumminds.BigBang.Jewel.Objects.Assessment.I.SUBCASUALTY),
				Constants.MsgDir_Incoming);

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
}
