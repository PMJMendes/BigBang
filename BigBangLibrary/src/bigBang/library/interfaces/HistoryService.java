package bigBang.library.interfaces;

import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("HistoryService")
public interface HistoryService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static HistoryServiceAsync instance;
		public static HistoryServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(HistoryService.class);
			}
			return instance;
		}
	}

	public HistoryItem getItem(String undoItemId) throws SessionExpiredException, BigBangException;
	public HistoryItemStub undo(String undoItemId) throws SessionExpiredException, BigBangException;
}
