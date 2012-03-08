package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.NegotiationStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ExternRequestServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;
import bigBang.module.quoteRequestModule.shared.NegotiationSearchParameter;
import bigBang.module.quoteRequestModule.shared.NegotiationSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.CancelNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.CreateExternRequest;
import com.premiumminds.BigBang.Jewel.Operations.Negotiation.CreateInfoRequest;
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

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(), pidNegotiation);
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjNeg.GetProcessID());
			lobjComp = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjNeg.getAt(0));

			lobjResult = new Negotiation();
			lobjResult.id = lobjNeg.getKey().toString();
			lobjResult.processId = lobjProc.getKey().toString();
			lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());
			lobjResult.ownerId = lobjProc.GetParent().GetDataKey().toString();
			lobjResult.ownerTypeId = lobjProc.GetParent().GetScript().GetDataType().toString();
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
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMD = new ManageData(UUID.fromString(negotiation.processId));
			lopMD.mobjData = new NegotiationData();

			lopMD.mobjData.midProcess = UUID.fromString(negotiation.processId);
			lopMD.mobjData.mid = UUID.fromString(negotiation.id);
			lopMD.mobjData.midCompany = UUID.fromString(negotiation.companyId);
			lopMD.mobjData.mstrNotes = negotiation.notes;
			lopMD.mobjData.mdtLimitDate = (negotiation.limitDate == null ? null :
					Timestamp.valueOf(negotiation.limitDate + " 00:00:00.0"));

			lopMD.mobjContactOps = null;
			lopMD.mobjDocOps = null;

			lopMD.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getNegotiation(negotiation.id);
	}

	@Override
	public Negotiation.QuoteRequestInfo sendQuoteRequest(Negotiation.QuoteRequestInfo request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation.QuoteRequestInfo repeatSendQuoteRequest(Negotiation.QuoteRequestInfo request)
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
		lopRQ.mobjMessage = MessageBridge.incomingToServer(response.message, Constants.ObjID_Negotiation);

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

	public Negotiation.Grant grantNegotiation(Negotiation.Grant grant)
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

		return null;
	}

	@Override
	public InsurancePolicy createPolicy(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		lopCIR = new CreateInfoRequest(lobjNeg.GetProcessID());
		lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
		lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
		lopCIR.mlngDays = request.replylimit;

		try
		{
			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Negotiation lobjNeg;
		CreateExternRequest lopCER;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjNeg = com.premiumminds.BigBang.Jewel.Objects.Negotiation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));

			lopCER = new CreateExternRequest(lobjNeg.GetProcessID());
			lopCER.mstrSubject = request.subject;
			lopCER.mobjMessage = MessageBridge.incomingToServer(request.message, Constants.ObjID_Negotiation);
			lopCER.mlngDays = request.replylimit;

			lopCER.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return ExternRequestServiceImpl.sGetRequest(lopCER.midRequestObject);
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
