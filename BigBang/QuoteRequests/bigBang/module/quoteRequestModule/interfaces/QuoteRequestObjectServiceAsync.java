package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuoteRequestObjectServiceAsync
	extends SearchServiceAsync
{
	void getObject(String objectId, AsyncCallback<QuoteRequestObject> callback);
}
