package bigBang.library.interfaces;

import bigBang.definitions.shared.Permission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangPermissionServiceAsync
	extends Service
{
	void getProcessPermissions(String dataObjectId, AsyncCallback<Permission[]> callback);
	void getGeneralOpPermissions(String dataTypeId, AsyncCallback<Permission[]> callback);
}
