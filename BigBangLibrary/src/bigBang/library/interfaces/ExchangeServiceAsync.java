package bigBang.library.interfaces;

import bigBang.library.shared.ExchangeItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExchangeServiceAsync
	extends Service
{
	void getItems(AsyncCallback<ExchangeItem[]> callback);
}
