package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
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
	 * @param handler The handler to be notified on response
	 */
	public void removeClient(String clientId, ResponseHandler<String> handler);
	
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
	public void createRiskAnalisys(String clientId, RiskAnalysis riskAnalisys, ResponseHandler<RiskAnalysis> handler);

	public void createInsurancePolicy(String clientId, InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void createQuoteRequest(String clientId, QuoteRequest quoteRequest, ResponseHandler<QuoteRequest> handler);
	
	public void createCasualty(String clientId, Casualty casualty, ResponseHandler<Casualty> handler);
	
	public void mergeWithClient(String originalId, String receptorId, ResponseHandler<Client> handler);
	
	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request, ResponseHandler<InfoOrDocumentRequest> handler);
	
	public void repeatRequest(InfoOrDocumentRequest request, ResponseHandler<InfoOrDocumentRequest> handler);
	
	public void receiveInfoOrDocumentRequestResponse(InfoOrDocumentRequest.Response response, ResponseHandler<InfoOrDocumentRequest> handler);
	
	public void cancelInfoOrDocumentRequest(InfoOrDocumentRequest.Cancellation cancellation, ResponseHandler<Void> handler);
	
	public void createManagerTransfer(String[] clientIds, String managerId, ResponseHandler<ManagerTransfer[]> handler);
}
