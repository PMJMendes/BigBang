package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DocuShareService")
public interface DocuShareService
	extends ImageSubService
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

	DocuShareItem[] getItems(String pstrFolder, boolean pbWithFolders) throws SessionExpiredException, BigBangException;
	DocuShareItem[] getContext(String ownerId, String ownerTypeId) throws SessionExpiredException, BigBangException;
	String getItem(String pstrItem) throws SessionExpiredException, BigBangException;
}
