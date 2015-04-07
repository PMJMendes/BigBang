package bigBang.library.interfaces;

import bigBang.library.shared.ScanItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocuShareServiceAsync
	extends ScanItemServiceAsync
{
	void isDocuSharePresent(AsyncCallback<Boolean> callback);
	void getContext(String ownerId, String ownerTypeId, AsyncCallback<ScanItem[]> callback);
}
