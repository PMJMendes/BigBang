package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.DependentItemSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuoteRequestServiceAsync
	extends SearchServiceAsync, DependentItemSubServiceAsync
{
	void getRequest(String requestId, AsyncCallback<QuoteRequest> callback);
	void getPage(String requestId, String subLineId, String objectId, AsyncCallback<QuoteRequest.TableSection> callback);
	void openRequestScratchPad(String requestId, AsyncCallback<Remap[]> callback);
	void initRequestInPad(QuoteRequest request, AsyncCallback<QuoteRequest> callback);
	void getRequestInPad(String requestId, AsyncCallback<QuoteRequest> callback);
	void updateHeader(QuoteRequest request, AsyncCallback<QuoteRequest> callback);
	void addSubLineToPad(String requestId, String subLineId, AsyncCallback<QuoteRequest.RequestSubLine> callback);
	void deleteSubLineFromPad(String subLineId, AsyncCallback<Void> callback);
	void getPageForEdit(String requestId, String subLineId, String objectId, AsyncCallback<QuoteRequest.TableSection> callback);
	void savePage(QuoteRequest.TableSection data, AsyncCallback<QuoteRequest.TableSection> callback);
	void getObjectInPad(String objectId, AsyncCallback<QuoteRequestObject> callback);
	void createObjectInPad(String requestId, String objectTypeId, AsyncCallback<QuoteRequestObject> callback);
	void createObjectFromClientInPad(String requestId, AsyncCallback<QuoteRequestObject> callback);
	void updateObjectInPad(QuoteRequestObject data, AsyncCallback<QuoteRequestObject> callback);
	void deleteObjectInPad(String objectId, AsyncCallback<Void> callback);
	void commitPad(String requestId, AsyncCallback<Remap[]> callback);
	void discardPad(String requestId, AsyncCallback<Remap[]> callback);
	void closeProcess(String requestId, String notes, AsyncCallback<QuoteRequest> callback);
	void deleteRequest(String requestId, String reason, AsyncCallback<Void> callback);
}
