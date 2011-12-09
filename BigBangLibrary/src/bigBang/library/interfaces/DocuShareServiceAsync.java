package bigBang.library.interfaces;

import bigBang.library.shared.DocuShareItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocuShareServiceAsync
	extends Service
{
	void getItems(String pstrFolder, boolean pbWithFolders, AsyncCallback<DocuShareItem[]> callback);
	void getItem(String pstrItem, AsyncCallback<String> callback);
	void getItemAsImage(String pstrItem, AsyncCallback<String> callback);
}
