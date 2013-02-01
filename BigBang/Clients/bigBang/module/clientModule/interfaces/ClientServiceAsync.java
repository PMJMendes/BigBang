package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
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
	void setInternational(String clientId, AsyncCallback<Client> callback);
	void mergeWithClient(String clientId, String receptorId, AsyncCallback<Client> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void createPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void createRiskAnalisys(RiskAnalysis riskAnalisys, AsyncCallback<RiskAnalysis> callback);
	void createCasualty(Casualty casualty, AsyncCallback<Casualty> callback);
	void deleteClient(String clientId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
