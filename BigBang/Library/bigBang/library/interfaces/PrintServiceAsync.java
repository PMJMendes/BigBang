package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PrintServiceAsync
	extends Service
{
	void getAvailablePrinterNames(AsyncCallback<String[]> callback);
}
