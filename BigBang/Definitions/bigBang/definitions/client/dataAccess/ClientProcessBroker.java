package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.RiskAnalysis;

/**
 * The interface for a Client processes DataBroker
 */
public interface ClientProcessBroker extends DataBrokerInterface<Client>{

	/**
	 * Fetches the client for a given id
	 * @param clientId The client id
	 * @param handler The handler to be notified on response
	 */
	public void getClient(String clientId, ResponseHandler<Client> handler);
	
	/**
	 * Creates a new Client
	 * @param client The client to be created
	 * @param handler The handler to be notified on response
	 */
	public void addClient(Client client, ResponseHandler<Client> handler);
	
	/**
	 * Updates the client for a given id
	 * @param clientId The client id
	 * @param handler The handler to be notified on response
	 */
	public void updateClient(Client client, ResponseHandler<Client> handler);
	
	/**
	 * Removes the client for a given id
	 * @param clientId The client id
	 * @param reasonId The id of the reason for deletion
	 * @param handler The handler to be notified on response
	 */
	public void removeClient(String clientId, String reasonId, ResponseHandler<String> handler);
	
	/**
	 * Gets the search service associated with this broker
	 * @return
	 */
	public SearchDataBroker<ClientStub> getSearchBroker();
	
	/**
	 * Creates a risk analysis for a given client
	 * @param clientId The id of the client
	 * @param riskAnalysis The risk analysis to be created
	 * @param handler The handler to be notified on response
	 */
	public void createRiskAnalisys(RiskAnalysis riskAnalisys, ResponseHandler<RiskAnalysis> handler);

	public void createPolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);

	public void createCasualty(Casualty casualty, ResponseHandler<Casualty> handler);
	
	public void mergeWithClient(String originalId, String receptorId, ResponseHandler<Client> handler);
		
	public void createManagerTransfer(String[] dataObjectIds, String managerId, ResponseHandler<ManagerTransfer> handler);

	void getClientSubProcesses(String clientId,
			ResponseHandler<Collection<BigBangProcess>> handler);
	
	void getClientSubProcess(String clientId, String subProcessId, ResponseHandler<BigBangProcess> handler);
	
	void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);

	public void markAsInternational(String id,
			ResponseHandler<Client> responseHandler);


}
