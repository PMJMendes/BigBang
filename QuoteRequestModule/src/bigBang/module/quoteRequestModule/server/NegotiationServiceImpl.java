package bigBang.module.quoteRequestModule.server;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.Negotiation.Adjudication;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.QuoteRequestInfoRequest;
import bigBang.definitions.shared.QuoteRequestInfoRequest.Response;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NegotiationServiceImpl  extends RemoteServiceServlet implements NegotiationService {

	private static final long serialVersionUID = 1L;

	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters,
			SortParameter[] sorts, int size) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult openForOperation(String opId,
			SearchParameter[] parameters, SortParameter[] sorts, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult search(String workspaceId,
			SearchParameter[] parameters, SortParameter[] sorts, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] getResults(String workspaceId, int from, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeSearch(String workspaceId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Negotiation getNegotiation(String negotiationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation deleteNegotiation(Deletion deletion)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation cancelNegotiation(Cancellation cancellation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Adjudication adjudicateNegotiation(Adjudication adjudication) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsurancePolicy createPolicy(InsurancePolicy policy)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestInfoRequest sendQuoteRequest(
			QuoteRequestInfoRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response receiveQuoteRequestResponse(Response response)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestInfoRequest repeatSendQuoteRequest(
			QuoteRequestInfoRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest createInfoRequestToInsuranceAgency(
			InfoOrDocumentRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest repeatInfoRequestToInsuranceAgency(
			InfoOrDocumentRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelInfoRequestToInsuranceAgency(
			InfoOrDocumentRequest.Cancellation cancellation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InfoOrDocumentRequest receiveInfoRequestToInsuranceAgencyResponse(
			InfoOrDocumentRequest.Response response)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalInfoRequest createInfoRequestFromInsuranceAgency(
			ExternalInfoRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalInfoRequest repeatInfoRequestFromInsuranceAgency(
			ExternalInfoRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeInfoRequestFromInsuranceAgency(
			bigBang.definitions.shared.ExternalInfoRequest.Cancellation cancellation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExternalInfoRequest receiveInfoRequestFromInsuranceAgencyResponse(
			bigBang.definitions.shared.ExternalInfoRequest.Response response)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

}
