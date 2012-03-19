package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NegotiationServiceAsync
	extends SearchServiceAsync
{
	void getNegotiation(String negotiationId, AsyncCallback<Negotiation> callback);
	void editNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void sendQuoteRequest(Negotiation.QuoteRequestInfo request, AsyncCallback<Negotiation.QuoteRequestInfo> callback);
	void repeatSendQuoteRequest(Negotiation.QuoteRequestInfo request, AsyncCallback<Negotiation.QuoteRequestInfo> callback);
	void cancelNegotiation(Negotiation.Cancellation cancellation, AsyncCallback<Negotiation> callback);
	void receiveResponse(Negotiation.Response response, AsyncCallback<Negotiation> callback);
	void grantNegotiation(Negotiation.Grant grant, AsyncCallback<Negotiation> callback);
	void createPolicy(Negotiation negotiation, AsyncCallback<InsurancePolicy> callback);
	void createInfoRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createExternalRequest(ExternalInfoRequest request, AsyncCallback<ExternalInfoRequest> callback);
	void closeNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void deleteNegotiation(Negotiation.Deletion deletion, AsyncCallback<Void> callback);
}
