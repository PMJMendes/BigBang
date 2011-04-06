package bigBang.library.interfaces;

import bigBang.library.shared.Permission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangPermissionServiceAsync
	extends Service
{
	void getProcessPermissions(String id, AsyncCallback<Permission[]> callback);
}
