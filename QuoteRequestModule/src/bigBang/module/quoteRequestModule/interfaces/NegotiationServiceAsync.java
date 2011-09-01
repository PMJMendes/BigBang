package bigBang.module.quoteRequestModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InsuranceAgencyInfoRequest;
import bigBang.definitions.shared.InsuranceAgencyInfoRequest.Response;
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

	void createInfoRequestToInsuranceAgency(InsuranceAgencyInfoRequest request,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

	void repeatInfoRequestToInsuranceAgency(InsuranceAgencyInfoRequest request,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

	void cancelInfoRequestToInsuranceAgency(
			bigBang.definitions.shared.InsuranceAgencyInfoRequest.Cancellation cancellation,
			AsyncCallback<Void> callback);

	void receiveInfoRequestToInsuranceAgencyResponse(Response response,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

	void createInfoRequestFromInsuranceAgency(
			InsuranceAgencyInfoRequest request,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

	void repeatInfoRequestFromInsuranceAgency(
			InsuranceAgencyInfoRequest request,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

	void closeInfoRequestFromInsuranceAgency(
			bigBang.definitions.shared.InsuranceAgencyInfoRequest.Cancellation cancellation,
			AsyncCallback<Void> callback);

	void receiveInfoRequestFromInsuranceAgencyResponse(Response response,
			AsyncCallback<InsuranceAgencyInfoRequest> callback);

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
