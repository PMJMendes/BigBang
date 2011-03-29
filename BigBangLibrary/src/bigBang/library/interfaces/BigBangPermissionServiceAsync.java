package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangPermissionServiceAsync extends Service {

	void getProcessPermissions(String id, AsyncCallback<String[]> callback);

}
