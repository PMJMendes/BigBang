package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DependentItemSubServiceAsync
	extends Service
{
	void getPadItemsFilter(String listId, String policyId, AsyncCallback<TipifiedListItem[]> callback);
}
