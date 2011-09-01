package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientToManagerTransfer;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Response;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.library.interfaces.SearchServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientServiceAsync
	extends SearchServiceAsync
{
	void getClient(String clientId, AsyncCallback<Client> callback);
	void createClient(Client client, AsyncCallback<Client> callback);
	void editClient(Client client, AsyncCallback<Client> callback);
	void deleteClient(String clientId, AsyncCallback<Void> callback);
	void createRiskAnalisys(String clientId, RiskAnalisys riskAnalisys,
			AsyncCallback<RiskAnalisys> callback);
	void createPolicy(String clientId, InsurancePolicy policy,
			AsyncCallback<InsurancePolicy> callback);
	void createQuoteRequest(String clientId, QuoteRequest request,
			AsyncCallback<QuoteRequest> callback);
	void createCasualty(String clientId, Casualty casualty,
			AsyncCallback<Casualty> callback);
	void mergeWithClient(String originalId, String receptorId,
			AsyncCallback<Client> callback);
	void transferToManager(String[] clientIds, String managerId,
			AsyncCallback<ClientToManagerTransfer[]> callback);
	void acceptTransfer(String transferId,
			AsyncCallback<ClientToManagerTransfer> callback);
	void cancelTransfer(String transferId,
			AsyncCallback<ClientToManagerTransfer> callback);
	void createInfoOrDocumentRequest(ClientInfoOrDocumentRequest request,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);
	void repeatRequest(ClientInfoOrDocumentRequest request,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);
	void receiveInfoOrDocumentRequestResponse(Response response,
			AsyncCallback<ClientInfoOrDocumentRequest> callback);
	void cancelInfoOrDocumentRequest(Cancellation cancellation,
			AsyncCallback<Void> callback);
}
