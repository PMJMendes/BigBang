package bigBang.library.interfaces;

import bigBang.library.shared.ScanItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ScanItemServiceAsync
	extends ImageSubServiceAsync
{
	void getItems(String pstrFolder, boolean pbWithFolders, AsyncCallback<ScanItem[]> callback);
	void getItem(String pstrItem, AsyncCallback<String> callback);
}
