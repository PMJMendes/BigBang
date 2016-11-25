package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.DeleteQuoteRequest;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData;

public class QuoteRequestServiceImpl
	extends SearchServiceBase
	implements QuoteRequestService
{
	private static final long serialVersionUID = 1L;

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		throw new BigBangException("Erro: Lista inválida para o serviço.");
	}

	public QuoteRequest getEmptyRequest(String clientId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getEmptyRequest(UUID.fromString(clientId));
	}

	public QuoteRequest getRequest(String requestId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getRequest(UUID.fromString(requestId));
	}

	public CompositeFieldContainer.SubLineFieldContainer getEmptySubLine(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getEmptySubLine(UUID.fromString(subLineId));
	}

	@Override
	public QuoteRequest editRequest(QuoteRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		QuoteRequestData lobjData;
		ManageData lopMPD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjData = new ClientToServer().getQRDataForEdit(lobjRequest, request);

		try
		{
			lopMPD = new ManageData(lobjRequest.GetProcessID());
			lopMPD.mobjData = lobjData;

			lopMPD.mobjContactOps = null;
			lopMPD.mobjDocOps = null;

			lopMPD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ServerToClient().getRequest(lobjRequest.getKey());
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		ExecMgrXFer lobjEMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(transfer.dataObjectIds[0]));

			lobjEMX = new ExecMgrXFer(lobjRequest.GetProcessID());
			lobjEMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjEMX.midMassProcess = null;

			lobjEMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public Negotiation createNegotiation(Negotiation negotiation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		CreateNegotiation lopCN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(negotiation.ownerId));

			lopCN = new CreateNegotiation(lobjRequest.GetProcessID());
			lopCN.mobjData = new NegotiationData();

			lopCN.mobjData.mid = null;
			lopCN.mobjData.midCompany = ( negotiation.companyId == null ? null : UUID.fromString(negotiation.companyId) );
			lopCN.mobjData.mstrNotes = negotiation.notes;
			lopCN.mobjData.mdtLimitDate = (negotiation.limitDate == null ? null :
					Timestamp.valueOf(negotiation.limitDate + " 00:00:00.0"));

			lopCN.mobjData.midManager = null;
			lopCN.mobjData.midProcess = null;

			lopCN.mobjData.mobjPrevValues = null;

			if ( (negotiation.contacts != null) && (negotiation.contacts.length > 0) )
			{
				lopCN.mobjContactOps = new ContactOps();
				lopCN.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(negotiation.contacts);
			}
			else
				lopCN.mobjContactOps = null;
			if ( (negotiation.documents != null) && (negotiation.documents.length > 0) )
			{
				lopCN.mobjDocOps = new DocOps();
				lopCN.mobjDocOps.marrCreate2 = DocumentServiceImpl.buildTreeLight(negotiation.documents);
			}
			else
				lopCN.mobjDocOps = null;

			lopCN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return NegotiationServiceImpl.sGetNegotiation(lopCN.mobjData.mid);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjQReq;
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
			lobjQReq = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjQReq.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_QuoteRequest,
				lobjQReq.getKey(), Constants.MsgDir_Outgoing, null);

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
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjQReq;
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
			lobjQReq = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjQReq.GetProcessID());
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
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_QuoteRequest,
				lobjQReq.getKey(), lopCC.mobjData.midStartDir, null);

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

	public QuoteRequest closeProcess(String requestId, String notes)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		CloseProcess lopCP;
		
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(requestId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjRequest.GetProcessID());
		lopCP.mstrNotes = notes;

		try
		{
			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ServerToClient().getRequest(lobjRequest.getKey());
	}

	public void deleteRequest(String requestId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		DeleteQuoteRequest lobjDQR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(requestId));

			lobjDQR = new DeleteQuoteRequest(lobjRequest.GetProcessID());
			lobjDQR.midRequest = UUID.fromString(requestId);
			lobjDQR.mstrReason = reason;
			lobjDQR.Execute();
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

		transfer.objectTypeId = Constants.ObjID_QuoteRequest.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_QuoteRequest);
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_QuoteRequest;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Case Study]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
		pstrBuffer.append(" AND ([:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR ([:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Client:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Client:Group:Mediator] = '").append(pidMediator.toString()).append("')))");
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		QuoteRequestSearchParameter lParam;
		String lstrAux;
		IEntity lrefQRSubLines;
		IEntity lrefNegotiations;

		if ( !(pParam instanceof QuoteRequestSearchParameter) )
			return false;
		lParam = (QuoteRequestSearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND [:Number] LIKE N'%").append(lstrAux).append("%'");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Client] = '").append(lParam.ownerId).append("'");
		}

		if ( lParam.subLineId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line] = '").append(lParam.subLineId).append("')");
		}
		else if ( lParam.lineId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line:Line] = '").append(lParam.lineId).append("'");
		}
		else if ( lParam.categoryId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line:Line:Category] = '").append(lParam.categoryId).append("'");
		}

		if ( lParam.insuranceAgencyId != null )
		{
			pstrBuffer.append(" AND [:Process] IN (SELECT [:Process:Parent] FROM (");
			try
			{
				lrefNegotiations = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Negotiation));
				pstrBuffer.append(lrefNegotiations.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxNegotiations] WHERE [:Company] = '").append(lParam.insuranceAgencyId).append("')");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND [:Mediator] = '").append(lParam.mediatorId).append("'");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( (lParam.caseStudy != null) && lParam.caseStudy )
		{
			pstrBuffer.append(" AND [:Case Study] = 1");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestSortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof QuoteRequestSortParameter) )
			return false;
		lParam = (QuoteRequestSortParameter)pParam;

		if ( lParam.field == QuoteRequestSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == QuoteRequestSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == QuoteRequestSortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Name] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == QuoteRequestSortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("(SELECT [:Number] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
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
		QuoteRequestStub lobjResult;
		Client lobjClient;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjClient = null;
		}

		lobjResult = new QuoteRequestStub();

		lobjResult.id = pid.toString();
		lobjResult.processNumber = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.caseStudy = (Boolean)parrValues[2];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.isOpen = (lobjProcess == null ? false : lobjProcess.IsRunning());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof QuoteRequestSearchParameter) )
				continue;
			lParam = (QuoteRequestSearchParameter)parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("0 END");
		}

		return lbFound;
	}
}
