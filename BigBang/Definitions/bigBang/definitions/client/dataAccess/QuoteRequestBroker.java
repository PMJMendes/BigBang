package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.RiskAnalysis;

public interface QuoteRequestBroker extends DataBrokerInterface<QuoteRequest> {

	public void getQuoteRequest(String id, ResponseHandler<QuoteRequest> handler);
	public void getQuoteRequestsForClient(String clientId, ResponseHandler<Collection<QuoteRequestStub>> handler);

	public void openRequestResource(String requestId, ResponseHandler<QuoteRequest> handler);
	public void commitRequest(QuoteRequest request, ResponseHandler<QuoteRequest> handler);
	public void closeRequestResource(String requestId, ResponseHandler<Void> handler);
	public void openCoverageDetailsPage(String requestId, String subLineId, String insuredObjectId, ResponseHandler<QuoteRequest.TableSection> handler);
	public void saveCoverageDetailsPage(String requestId, String subLineId, String insuredObjectId, QuoteRequest.TableSection data, ResponseHandler<QuoteRequest.TableSection> handler);
	public void addSubLine(String quoteRequestId, String subLineId, ResponseHandler<RequestSubLine> handler);
	public void deleteSubLine(String subLineId, ResponseHandler<Void> handler);
	public void updateQuoteRequest(QuoteRequest request, ResponseHandler<QuoteRequest> handler);

	public void closeQuoteRequest(String id, String notes, ResponseHandler<QuoteRequest> handler);
	public void deleteQuoteRequest(String id, String reason,  ResponseHandler<String> handler);
	
	public void insertInsuredObject(InsuredObject object, ResponseHandler<InsuredObject> handler);
//	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request, ResponseHandler<InfoOrDocumentRequest> handler);
	public void createQuoteRequestManagerTransfer(String[] requestIds, String managerId, ResponseHandler<QuoteRequest> handler);
	public void createRiskAnalysis(RiskAnalysis riskAnalysis, ResponseHandler<RiskAnalysis> handler);
	
	public SearchDataBroker<QuoteRequestStub> getSearchBroker();
	
	public boolean isTemp(String policyId);
	public void discardTemp(String policyId);
	public String getEffectiveId(String ownerId);
	public String getFinalMapping(String ownerId);
	public void remapItemId(String oldId, String newId, boolean newInScratchPad);
	public void createManagerTransfer(String[] processIds, String newManagerId,
			ResponseHandler<ManagerTransfer> responseHandler);
	
}

