package bigBang.library.interfaces;

import bigBang.definitions.shared.ManagerTransfer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransferManagerServiceAsync
	extends Service
{
	void acceptTransfer(String transferId, AsyncCallback<ManagerTransfer> callback);
	void cancelTransfer(String transferId, AsyncCallback<ManagerTransfer> callback);
	void massAcceptTransfer(String transferId, AsyncCallback<ManagerTransfer> callback);
	void massCancelTransfer(String transferId, AsyncCallback<ManagerTransfer> callback);
}
