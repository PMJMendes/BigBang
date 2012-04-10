package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientServiceAsync
	extends SearchServiceAsync
{
	void getClient(String clientId, AsyncCallback<Client> callback);
	void createClient(Client client, AsyncCallback<Client> callback);
	void editClient(Client client, AsyncCallback<Client> callback);
	void mergeWithClient(String clientId, String receptorId, AsyncCallback<Client> callback);
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void createRiskAnalisys(String clientId, RiskAnalysis riskAnalisys, AsyncCallback<RiskAnalysis> callback);
	void createCasualty(Casualty casualty, AsyncCallback<Casualty> callback);
	void deleteClient(String clientId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
