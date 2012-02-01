package bigBang.module.quoteRequestModule.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequestInfoRequest;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

@RemoteServiceRelativePath("QuoteRequestNegotiationService")
public interface NegotiationService extends SearchService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static NegotiationServiceAsync instance;
		public static NegotiationServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(NegotiationService.class);
			}
			return instance;
		}
	}

	public Negotiation getNegotiation(String negotiationId);
	public Negotiation cancelNegotiation(Negotiation.Cancellation cancellation) throws SessionExpiredException, BigBangException;
	public Negotiation.Adjudication adjudicateNegotiation(Negotiation.Adjudication adjudication);

	public InsurancePolicy createPolicy(InsurancePolicy policy) throws SessionExpiredException, BigBangException;
	public QuoteRequestInfoRequest sendQuoteRequest(QuoteRequestInfoRequest request) throws SessionExpiredException, BigBangException;
	public QuoteRequestInfoRequest.Response receiveQuoteRequestResponse(QuoteRequestInfoRequest.Response response) throws SessionExpiredException, BigBangException;
	public QuoteRequestInfoRequest repeatSendQuoteRequest(QuoteRequestInfoRequest request) throws SessionExpiredException, BigBangException;
	
	//Request TO insurance agency
	public InfoOrDocumentRequest createInfoRequestToInsuranceAgency(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public InfoOrDocumentRequest repeatInfoRequestToInsuranceAgency(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public void cancelInfoRequestToInsuranceAgency(InfoOrDocumentRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;
	public InfoOrDocumentRequest receiveInfoRequestToInsuranceAgencyResponse(InfoOrDocumentRequest.Response response) throws SessionExpiredException, BigBangException;
	
	//Request FROM insurance agency
	public ExternalInfoRequest createInfoRequestFromInsuranceAgency(ExternalInfoRequest request) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest repeatInfoRequestFromInsuranceAgency(ExternalInfoRequest request) throws SessionExpiredException, BigBangException;
	public void closeInfoRequestFromInsuranceAgency(ExternalInfoRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest receiveInfoRequestFromInsuranceAgencyResponse(ExternalInfoRequest.Response response) throws SessionExpiredException, BigBangException;

	public Negotiation deleteNegotiation(Negotiation.Deletion deletion) throws SessionExpiredException, BigBangException;
}
