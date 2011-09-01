package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuoteRequestServiceAsync
	extends SearchServiceAsync
{
	void getRequest(String requestId, AsyncCallback<QuoteRequest> callback);
	void editRequest(QuoteRequest request, AsyncCallback<QuoteRequest> callback);
	void insertInsuredObject(InsuredObject object, AsyncCallback<InsuredObject> callback);
	void closeRequest(String requestId, AsyncCallback<QuoteRequest> callback);
	void createNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createManagerTransfer(String[] quoteRequestIds, String managerId, AsyncCallback<ManagerTransfer[]> callback);
	void createRiskAnalisys(RiskAnalisys riskAnalisys, AsyncCallback<RiskAnalisys> callback);
	void deleteRequest(String requestId, AsyncCallback<Void> callback);
}
