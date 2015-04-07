package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ScanItem;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DocuShareService")
public interface DocuShareService
	extends ScanItemService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static DocuShareServiceAsync instance;
		public static DocuShareServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(DocuShareService.class);
			}
			return instance;
		}
	}

	boolean isDocuSharePresent() throws SessionExpiredException, BigBangException;
	ScanItem[] getContext(String ownerId, String ownerTypeId) throws SessionExpiredException, BigBangException;
}
