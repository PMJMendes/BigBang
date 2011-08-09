package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TipifiedListService")
public interface TipifiedListService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static TipifiedListServiceAsync instance;
		public static TipifiedListServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(TipifiedListService.class);
			}
			return instance;
		}
	}
	
	public TipifiedListItem[] getListItems(String listId) throws SessionExpiredException, BigBangException;
	public TipifiedListItem createListItem(String listId, TipifiedListItem item) throws SessionExpiredException, BigBangException;
	public TipifiedListItem saveListItem(String listId, TipifiedListItem item) throws SessionExpiredException, BigBangException;
	public void deleteListItem(String listId, String itemId) throws SessionExpiredException, BigBangException;
}
