package bigBang.library.interfaces;

import bigBang.definitions.shared.ImageItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImageSubServiceAsync
	extends Service
{
	void getItemAsImage(String pstrItem, int pageNumber, AsyncCallback<ImageItem> callback);
}
