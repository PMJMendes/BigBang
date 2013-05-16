package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DependentItemSubService
	extends RemoteService
{
	public TipifiedListItem[] getListItemsFilter(String listId, String filterId) throws SessionExpiredException, BigBangException;
}
