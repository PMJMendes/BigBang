package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NegotiationServiceAsync
	extends SearchServiceAsync
{
	void getNegotiation(String negotiationId, AsyncCallback<Negotiation> callback);
	void editNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void sendCallForQuote(Negotiation.CallForQuote request, AsyncCallback<Negotiation.CallForQuote> callback);
	void repeatSendCallForQuote(Negotiation.CallForQuote request, AsyncCallback<Negotiation.CallForQuote> callback);
	void cancelNegotiation(Negotiation.Cancellation cancellation, AsyncCallback<Negotiation> callback);
	void receiveResponse(Negotiation.Response response, AsyncCallback<Negotiation> callback);
	void grantNegotiation(Negotiation.Grant grant, AsyncCallback<Negotiation> callback);
	void createPolicy(Negotiation negotiation, AsyncCallback<InsurancePolicy> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void closeNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void deleteNegotiation(Negotiation.Deletion deletion, AsyncCallback<Void> callback);
}
