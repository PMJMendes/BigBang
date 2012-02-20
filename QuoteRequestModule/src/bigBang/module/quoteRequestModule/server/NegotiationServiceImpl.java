package bigBang.module.quoteRequestModule.server;

import java.util.UUID;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;

public class NegotiationServiceImpl
	extends SearchServiceBase
	implements NegotiationService
{
	private static final long serialVersionUID = 1L;

	@Override
	public Negotiation getNegotiation(String negotiationId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation editNegotiation(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Negotiation cancelNegotiation(Negotiation.Cancellation cancellation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation receiveResponse(Negotiation.Response response)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation.Grant grantNegotiation(Negotiation.Grant adjudication)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsurancePolicy createPolicy(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation closeNegotiation(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteNegotiation(Negotiation.Deletion deletion)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected UUID getObjectID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean buildFilter(StringBuilder pstrBuffer,
			SearchParameter pParam) throws BigBangException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam,
			SearchParameter[] parrParams) throws BigBangException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected SearchResult buildResult(UUID pid, Object[] parrValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
