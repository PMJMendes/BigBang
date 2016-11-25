package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.NegotiationStub;
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
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;
import bigBang.module.quoteRequestModule.shared.NegotiationSearchParameter;
import bigBang.module.quoteRequestModule.shared.NegotiationSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.CancelNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.DeleteNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.ReceiveQuote;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.SendGrant;

public class NegotiationServiceImpl
	extends SearchServiceBase
	implements NegotiationService
{
	private static final long serialVersionUID = 1L;

	public static Negotiation sGetNegotiation(UUID pidNegotiation)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		IProcess lobjProc;
		Company lobjComp;
		Negotiation lobjResult;
		Mediator lobjMed;

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(), pidNegotiation);
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjNeg.GetProcessID());
			lobjComp = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjNeg.getAt(0));
			if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScript().GetDataType()) )
				lobjMed = ((Policy)lobjProc.GetParent().GetData()).getMediator();
			else
				lobjMed = ((QuoteRequest)lobjProc.GetParent().GetData()).GetClient().getMediator();

			lobjResult = new Negotiation();
			lobjResult.id = lobjNeg.getKey().toString();
			lobjResult.processId = lobjProc.getKey().toString();
			lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());
			lobjResult.ownerId = lobjProc.GetParent().GetDataKey().toString();
			lobjResult.ownerTypeId = lobjProc.GetParent().GetScript().GetDataType().toString();
			lobjResult.ownerLabel = lobjProc.GetParent().GetData().getLabel();
			lobjResult.inheritClientId = lobjProc.GetParent().GetParent().GetDataKey().toString();
			lobjResult.inheritClientName = lobjProc.GetParent().GetParent().GetData().getLabel();
			lobjResult.inheritMediatorId = lobjMed.getKey().toString();
			lobjResult.inheritMediatorName = lobjMed.getLabel();
			lobjResult.companyId = lobjComp.getKey().toString();
			lobjResult.companyName = lobjComp.getLabel();
			lobjResult.limitDate = (lobjNeg.getAt(2) == null ? null : ((Timestamp)lobjNeg.getAt(2)).toString().substring(0, 10));
			lobjResult.managerId = lobjProc.GetManagerID().toString();
			lobjResult.notes = (String)lobjNeg.getAt(1);
			lobjResult.contacts = new Contact[0];
			lobjResult.documents = new Document[0];
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

		return lobjResult;
	}

	public Negotiation getNegotiation(String negotiationId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetNegotiation(UUID.fromString(negotiationId));
	}

	public Negotiation editNegotiation(Negotiation negotiation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(negotiation.id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjNeg.GetProcessID());
		lopMD.mobjData = new NegotiationData();
		lopMD.mobjData.FromObject(lobjNeg);

		lopMD.mobjData.midCompany = UUID.fromString(negotiation.companyId);
		lopMD.mobjData.mstrNotes = negotiation.notes;
		lopMD.mobjData.mdtLimitDate = (negotiation.limitDate == null ? null :
				Timestamp.valueOf(negotiation.limitDate + " 00:00:00.0"));

		lopMD.mobjContactOps = null;
		lopMD.mobjDocOps = null;

		try
		{
			lopMD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getNegotiation(negotiation.id);
	}

	@Override
	public Negotiation.CallForQuote sendCallForQuote(Negotiation.CallForQuote request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation.CallForQuote repeatSendCallForQuote(Negotiation.CallForQuote request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public Negotiation cancelNegotiation(Negotiation.Cancellation cancellation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		ObjectBase lobjMotive;
		CancelNegotiation lopCN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(cancellation.negotiationId));
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_RequestCancelMotives),
					UUID.fromString(cancellation.internalMotiveId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCN = new CancelNegotiation(lobjNeg.GetProcessID());
		lopCN.mstrMotive = lobjMotive.getLabel();
		lopCN.mbSendNotification = cancellation.sendResponseToInsuranceAgency;
		if ( lopCN.mbSendNotification )
			lopCN.mobjMessage = MessageBridge.outgoingToServer(cancellation.message);

		try
		{
			lopCN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetNegotiation(lobjNeg.getKey());
	}

	public Negotiation receiveResponse(Negotiation.Response response)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		ReceiveQuote lopRQ;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(response.negotiationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRQ = new ReceiveQuote(lobjNeg.GetProcessID());
//		lopRQ.mobjMessage = MessageBridge.incomingToServer(response.message, Constants.ObjID_Negotiation);

		try
		{
			lopRQ.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetNegotiation(lobjNeg.getKey());
	}

	public Negotiation grantNegotiation(Negotiation.Grant grant)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNegotiation;
		SendGrant lopSG;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNegotiation = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(grant.negotiationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopSG = new SendGrant(lobjNegotiation.GetProcessID());
		lopSG.mobjMessage = MessageBridge.outgoingToServer(grant.message);
		lopSG.mdtEffectDate = ( grant.effectDate == null ? null : Timestamp.valueOf(grant.effectDate + " 00:00:00.0") );

		try
		{
			lopSG.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetNegotiation(lobjNegotiation.getKey());
	}

	@Override
	public InsurancePolicy createPolicy(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
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
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjNeg.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Negotiation,
				lobjNeg.getKey(), Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
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
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjNeg.GetProcessID());
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
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Negotiation,
				lobjNeg.getKey(), lopCC.mobjData.midStartDir, null);

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

	@Override
	public Negotiation closeNegotiation(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteNegotiation(Negotiation.Deletion deletion)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNegotiation;
		DeleteNegotiation lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNegotiation = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(deletion.negotiationId));

			lobjDC = new DeleteNegotiation(lobjNegotiation.GetProcessID());
			lobjDC.midNegotiation = UUID.fromString(deletion.negotiationId);
			lobjDC.mstrReason = deletion.motive;

			lobjDC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Negotiation;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Process]", "[:Company]", "[:Company:Name]", "[:Limit Date]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		NegotiationSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;
//		IEntity lrefQuoteRequests;

		if ( !(pParam instanceof NegotiationSearchParameter) )
			return false;
		lParam = (NegotiationSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Notes] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Company:Name] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
//			pstrBuffer.append(" OR ([:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				lrefQuoteRequests = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequest));
//				pstrBuffer.append(lrefQuoteRequests.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//        		throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
			pstrBuffer.append(")");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		NegotiationSortParameter lParam;

		if ( !(pParam instanceof NegotiationSortParameter) )
			return false;
		lParam = (NegotiationSortParameter)pParam;

		if ( lParam.field == NegotiationSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess, lobjParent;
		IScript lobjScript;
		NegotiationStub lobjResult;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[0]);
			try
			{
				lobjParent = lobjProcess.GetParent();
				try
				{
					lobjScript = lobjProcess.GetScript();
				}
				catch (Throwable e)
				{
					lobjScript = null;
				}
			}
			catch (Throwable e)
			{
				lobjParent = null;
				lobjScript = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjParent = null;
			lobjScript = null;
		}

		lobjResult = new NegotiationStub();
		lobjResult.id = pid.toString();
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.ownerId = (lobjParent == null ? null : lobjParent.GetDataKey().toString());
		lobjResult.ownerTypeId = (lobjScript == null ? null : lobjScript.GetDataType().toString());
		lobjResult.companyId = ((UUID)parrValues[1]).toString();
		lobjResult.companyName = (String)parrValues[2];
		lobjResult.limitDate = (parrValues[3] == null ? null : ((Timestamp)parrValues[3]).toString().substring(0, 10));
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		NegotiationSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof NegotiationSearchParameter) )
				continue;
			lParam = (NegotiationSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Notes] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Notes]) ELSE ")
					.append("CASE WHEN [:Company:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Company:Name]) ELSE ")
					.append("0 END END");
		}

		return lbFound;
	}
}
