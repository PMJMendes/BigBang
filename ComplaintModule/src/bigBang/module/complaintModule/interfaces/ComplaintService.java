package bigBang.module.complaintModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ComplaintService")
public interface ComplaintService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ComplaintServiceAsync instance;
		public static ComplaintServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ComplaintService.class);
			}
			return instance;
		}
	}
}
