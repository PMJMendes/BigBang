package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("NegotiationService")
public interface NegotiationService
	extends SearchService
{
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

	public Negotiation getNegotiation(String negotiationId) throws SessionExpiredException, BigBangException;

	public Negotiation editNegotiation(Negotiation negotiation) throws SessionExpiredException, BigBangException;

	public Negotiation.QuoteRequestInfo sendQuoteRequest(Negotiation.QuoteRequestInfo request)
			throws SessionExpiredException, BigBangException;
	public Negotiation.QuoteRequestInfo repeatSendQuoteRequest(Negotiation.QuoteRequestInfo request)
			throws SessionExpiredException, BigBangException;

	public Negotiation cancelNegotiation(Negotiation.Cancellation cancellation) throws SessionExpiredException, BigBangException;

	public Negotiation receiveResponse(Negotiation.Response response) throws SessionExpiredException, BigBangException;

	public Negotiation.Grant grantNegotiation(Negotiation.Grant adjudication) throws SessionExpiredException, BigBangException;

	public InsurancePolicy createPolicy(Negotiation negotiation) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request) throws SessionExpiredException, BigBangException;

	public Negotiation closeNegotiation(Negotiation negotiation) throws SessionExpiredException, BigBangException;

	public void deleteNegotiation(Negotiation.Deletion deletion) throws SessionExpiredException, BigBangException;
}
