package bigBang.library.interfaces;

import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExchangeServiceAsync
	extends Service
{
	void getItems(AsyncCallback<ExchangeItemStub[]> callback);
	void getItem(String id, AsyncCallback<ExchangeItem> callback);
//	void getAttachment(String emailId, String attachmentId, AsyncCallback<Attachment> callback);
}
