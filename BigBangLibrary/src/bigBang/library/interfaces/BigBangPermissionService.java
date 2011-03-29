package bigBang.library.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("BigBangPermissionService")
public interface BigBangPermissionService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static BigBangPermissionServiceAsync instance;
		public static BigBangPermissionServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(BigBangPermissionService.class);
			}
			return instance;
		}
	}
	
	String[] getProcessPermissions(String id);
}
