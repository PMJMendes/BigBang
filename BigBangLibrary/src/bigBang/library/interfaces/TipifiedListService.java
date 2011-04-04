package bigBang.library.interfaces;

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
	
	//List item is a 2 position vector
	//v[0] = item id
	//v[1] = item description
	
	public String[] getListItems(String listId);
	
	public String[] createListItem(String[] item);
	
	public String[] saveListItem(String[] item);
	
	public void deleteListItem(String itemId);

}
