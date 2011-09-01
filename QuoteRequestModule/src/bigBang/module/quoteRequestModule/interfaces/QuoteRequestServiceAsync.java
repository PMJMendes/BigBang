package bigBang.module.quoteRequestModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.ClientInfoOrDocumentRequest;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Response;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestToManagerTransfer;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.SecuredObject;
import bigBang.library.interfaces.SearchServiceAsync;

public interface QuoteRequestServiceAsync extends SearchServiceAsync {

	void getRequest(String requestId, AsyncCallback<QuoteRequest> callback);

	void createRequest(QuoteRequest request,
			AsyncCallback<QuoteRequest> callback);

	void editRequest(QuoteRequest request, AsyncCallback<QuoteRequest> callback);

	void deleteRequest(String requestId, AsyncCallback<Void> callback);

	void closeRequest(String requestId, AsyncCallback<QuoteRequest> callback);

	void createRiskAnalisys(RiskAnalisys riskAnalisys,
			AsyncCallback<RiskAnalisys> callback);

	void insertSecuredObject(SecuredObject object,
			AsyncCallback<SecuredObject> callback);

	void transferToManager(String[] quoteRequestIds, String managerId,
			AsyncCallback<QuoteRequestToManagerTransfer[]> callback);

	void cancelTransfer(String transferId,
			AsyncCallback<QuoteRequestToManagerTransfer> callback);

	void acceptTransfer(String transferId,
			AsyncCallback<QuoteRequestToManagerTransfer> callback);

	void createClientInfoOrDocumentRequest(ClientInfoOrDocumentRequest request,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);

	void repeatClientInfoOrDocumentRequest(ClientInfoOrDocumentRequest request,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);

	void receiveClientInfoOrDocumentRequestResponse(Response response,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);

	void cancelClientInfoOrDocumentRequest(Cancellation cancellation,
			AsyncCallback<Void> callback);

}
