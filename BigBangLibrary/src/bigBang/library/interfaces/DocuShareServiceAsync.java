package bigBang.library.interfaces;

import bigBang.library.shared.DocuShareItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocuShareServiceAsync
	extends Service
{
	void getSubFolders(String pstrFolder, AsyncCallback<DocuShareItem[]> callback);
	void getItems(String pstrFolder, AsyncCallback<DocuShareItem[]> callback);
	void getItem(String pstrItem, AsyncCallback<String> callback);
}
