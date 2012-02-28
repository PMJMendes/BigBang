package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuoteRequestServiceAsync
	extends SearchServiceAsync
{
	void getRequest(String requestId, AsyncCallback<QuoteRequest> callback);
	void getPage(String requestId, String subLineId, String objectId, AsyncCallback<QuoteRequest.TableSection> callback);
	void openRequestScratchPad(String requestId, AsyncCallback<Remap[]> callback);
	void getRequestInPad(String requestId, AsyncCallback<QuoteRequest> callback);
	void updateHeader(QuoteRequest request, AsyncCallback<QuoteRequest> callback);
	void addSubLineToPad(String requestId, String subLineId, AsyncCallback<QuoteRequest.RequestSubLine> callback);
	void deleteSubLineFromPad(String subLineId, AsyncCallback<Void> callback);
	void getPageForEdit(String requestId, String subLineId, String objectId, AsyncCallback<QuoteRequest.TableSection> callback);
	void savePage(QuoteRequest.TableSection data, AsyncCallback<QuoteRequest.TableSection> callback);
	void getObjectInPad(String objectId, AsyncCallback<InsuredObject> callback);
	void createObjectInPad(String requestId, AsyncCallback<InsuredObject> callback);
	void createObjectFromClientInPad(String requestId, AsyncCallback<InsuredObject> callback);
	void updateObjectInPad(InsuredObject data, AsyncCallback<InsuredObject> callback);
	void deleteObjectInPad(String objectId, AsyncCallback<Void> callback);
	void commitPad(String requestId, AsyncCallback<Remap[]> callback);
	void discardPad(String requestId, AsyncCallback<Remap[]> callback);
	void deleteRequest(String requestId, AsyncCallback<Void> callback);
}
