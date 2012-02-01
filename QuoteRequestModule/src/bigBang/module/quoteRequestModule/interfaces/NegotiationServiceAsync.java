package bigBang.module.quoteRequestModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Response;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Adjudication;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.QuoteRequestInfoRequest;
import bigBang.library.interfaces.SearchServiceAsync;

public interface NegotiationServiceAsync extends SearchServiceAsync {

	void getNegotiation(String negotiationId,
			AsyncCallback<Negotiation> callback);

	void cancelNegotiation(Cancellation cancellation,
			AsyncCallback<Negotiation> callback);

	void deleteNegotiation(Deletion deletion,
			AsyncCallback<Negotiation> callback);

	void adjudicateNegotiation(Adjudication adjudication,
			AsyncCallback<Adjudication> callback);

	void createInfoRequestToInsuranceAgency(InfoOrDocumentRequest request,
			AsyncCallback<InfoOrDocumentRequest> callback);

	void repeatInfoRequestToInsuranceAgency(InfoOrDocumentRequest request,
			AsyncCallback<InfoOrDocumentRequest> callback);

	void cancelInfoRequestToInsuranceAgency(
			InfoOrDocumentRequest.Cancellation cancellation,
			AsyncCallback<Void> callback);

	void receiveInfoRequestToInsuranceAgencyResponse(InfoOrDocumentRequest.Response response,
			AsyncCallback<InfoOrDocumentRequest> callback);

	void createInfoRequestFromInsuranceAgency(
			ExternalInfoRequest request,
			AsyncCallback<ExternalInfoRequest> callback);

	void repeatInfoRequestFromInsuranceAgency(
			ExternalInfoRequest request,
			AsyncCallback<ExternalInfoRequest> callback);

	void closeInfoRequestFromInsuranceAgency(
			bigBang.definitions.shared.ExternalInfoRequest.Cancellation cancellation,
			AsyncCallback<Void> callback);

	void receiveInfoRequestFromInsuranceAgencyResponse(Response response,
			AsyncCallback<ExternalInfoRequest> callback);

	void createPolicy(InsurancePolicy policy,
			AsyncCallback<InsurancePolicy> callback);

	void sendQuoteRequest(QuoteRequestInfoRequest request,
			AsyncCallback<QuoteRequestInfoRequest> callback);

	void receiveQuoteRequestResponse(
			bigBang.definitions.shared.QuoteRequestInfoRequest.Response response,
			AsyncCallback<bigBang.definitions.shared.QuoteRequestInfoRequest.Response> callback);

	void repeatSendQuoteRequest(QuoteRequestInfoRequest request,
			AsyncCallback<QuoteRequestInfoRequest> callback);

}
