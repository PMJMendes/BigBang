package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.CompositeObject;
import bigBang.definitions.shared.CompositeObjectStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.StructuredFieldContainer;

public interface QuoteRequestBroker extends DataBrokerInterface<QuoteRequest> {

	//GET & SET

	public void getQuoteRequest(String requestId, ResponseHandler<QuoteRequest> handler);

	public void getEmptyQuoteRequest(String clientId, ResponseHandler<QuoteRequest> handler);

	public QuoteRequest getRequestHeader(String requestId);

	public QuoteRequest updateRequestHeader(QuoteRequest request);

	public void persistQuoteRequest(String requestId, ResponseHandler<QuoteRequest> handler);

	public QuoteRequest discardEditData(String requestId);

	public void removeQuoteRequest(String requestId, ResponseHandler<QuoteRequest> handler);

	public CompositeFieldContainer.SubLineFieldContainer createSubLine(String requestId);

	public CompositeFieldContainer.SubLineFieldContainer updateSubLineCoverages(String requestId, String subLineId,
			StructuredFieldContainer.Coverage[] coverages);

	public CompositeObjectStub[] getAlteredObjects(String requestId);

	public void getCompositeObject(String requestId, String objectId, ResponseHandler<CompositeObject> handler);

	public CompositeObject createCompositeObject(String requestId);

	public CompositeObject updateCompositeObject(String requestId, CompositeObject object);

	public CompositeObjectStub removeCompositeObject(String requestId, String objectId);

	public FieldContainer getContextForRequest(String requestId, String subLineId);

	public void saveContextForRequest(String requestId, String subLineId, FieldContainer contents);

	public FieldContainer getContextForCompositeObject(String requestId, String subLineId, String objectId);

	public void saveContextForCompositeObject(String requestId, String subLineId, String objectId, FieldContainer contents);


	// OTHER OPS

	public SearchDataBroker<QuoteRequestStub> getSearchBroker();

	public void getQuoteRequestsForClient(String clientId, ResponseHandler<Collection<QuoteRequestStub>> handler);

	public void createQuoteRequestManagerTransfer(String[] requestIds, String managerId, ResponseHandler<QuoteRequest> handler);

	public void createRiskAnalysis(RiskAnalysis riskAnalysis, ResponseHandler<RiskAnalysis> handler);

	public boolean isTemp(String policyId);
	public void discardTemp(String policyId);
	public String getEffectiveId(String ownerId);
	public String getFinalMapping(String ownerId);
	public void remapItemId(String oldId, String newId, boolean newInScratchPad);
	public void createManagerTransfer(String[] processIds, String newManagerId,
			ResponseHandler<ManagerTransfer> responseHandler);
	
}

