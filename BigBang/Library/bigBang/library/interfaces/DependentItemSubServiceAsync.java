package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DependentItemSubServiceAsync
	extends Service
{
	void getListItemsFilter(String listId, String filterId, AsyncCallback<TipifiedListItem[]> callback);
}
