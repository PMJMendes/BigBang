package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;

public interface QuoteRequestBroker extends DataBrokerInterface<QuoteRequest> {

	public void getQuoteRequest(String id, ResponseHandler<QuoteRequest> handler);
	public void getQuoteRequestsForClient(String clientId, ResponseHandler<QuoteRequest[]> handler);

	public void updateQuoteRequest(QuoteRequest request, ResponseHandler<QuoteRequest> handler);
	public void closeQuoteRequest(String id, ResponseHandler<QuoteRequest> handler);
	public void removeQuoteRequest(String id, ResponseHandler<String> handler);
	
	public void insertInsuredObject(InsuredObject object, ResponseHandler<InsuredObject> handler);
	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request, ResponseHandler<InfoOrDocumentRequest> handler);
	public void createQuoteRequestManagerTransfer(String[] requestIds, String managerId, ResponseHandler<QuoteRequest> handler);
	public void createRiskAnalysis(RiskAnalysis riskAnalysis, ResponseHandler<RiskAnalysis> handler);
	
}

