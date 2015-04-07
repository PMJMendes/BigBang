package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ScanItem;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FileShareScanService")
public interface ScanItemService
	extends ImageSubService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ScanItemServiceAsync instance;
		public static ScanItemServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ScanItemService.class);
			}
			return instance;
		}
	}

	ScanItem[] getItems(String pstrFolder, boolean pbWithFolders) throws SessionExpiredException, BigBangException;
	String getItem(String pstrItem) throws SessionExpiredException, BigBangException;
}
