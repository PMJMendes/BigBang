package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
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

	public void removeQuoteRequest(String requestId, String reason, ResponseHandler<String> handler);

	public void createSubLine(String requestId, String subLineId,
			ResponseHandler<CompositeFieldContainer.SubLineFieldContainer> handler);

	public void updateSubLineCoverages(String requestId, String subLineId, StructuredFieldContainer.Coverage[] coverages);

	public CompositeFieldContainer.SubLineFieldContainer removeSubLine(String requestId, String subLineId);

	public QuoteRequestObjectStub[] getAlteredObjects(String requestId);

	public void getRequestObject(String requestId, String objectId, ResponseHandler<QuoteRequestObject> handler);

	public QuoteRequestObject createRequestObject(String requestId, String typeId);

	public QuoteRequestObject updateCompositeObject(String requestId, QuoteRequestObject object);

	public QuoteRequestObjectStub removeCompositeObject(String requestId, String objectId);

	public FieldContainer getContextForRequest(String requestId, String subLineId);

	public void saveContextForRequest(String requestId, String subLineId, FieldContainer contents);

	public FieldContainer getContextForCompositeObject(String requestId, String subLineId, String objectId);

	public void saveContextForCompositeObject(String requestId, String subLineId, String objectId, FieldContainer contents);


	// OTHER OPS

	public SearchDataBroker<QuoteRequestStub> getSearchBroker();

	public void getQuoteRequestsForClient(String clientId, ResponseHandler<Collection<QuoteRequestStub>> handler);

	public void createQuoteRequestManagerTransfer(String[] requestIds, String managerId, ResponseHandler<QuoteRequest> handler);

	public void createRiskAnalysis(RiskAnalysis riskAnalysis, ResponseHandler<RiskAnalysis> handler);

	public void createManagerTransfer(String[] processIds, String newManagerId,
			ResponseHandler<ManagerTransfer> responseHandler);
	
}

