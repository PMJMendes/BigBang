package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.CompositeFieldContainer.SubLineFieldContainer;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.interfaces.DependentItemSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuoteRequestServiceAsync
	extends SearchServiceAsync, DependentItemSubServiceAsync
{
	void getEmptyRequest(String clientId, AsyncCallback<QuoteRequest> callback);
	void getRequest(String requestId, AsyncCallback<QuoteRequest> callback);
	void getEmptySubLine(String subLineId, AsyncCallback<SubLineFieldContainer> callback);
	void editRequest(QuoteRequest request, AsyncCallback<QuoteRequest> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void createNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void closeProcess(String requestId, String notes, AsyncCallback<QuoteRequest> callback);
	void deleteRequest(String requestId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
