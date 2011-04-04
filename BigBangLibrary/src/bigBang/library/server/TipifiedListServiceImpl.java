package bigBang.library.server;

import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.shared.TipifiedListItem;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TipifiedListServiceImpl extends RemoteServiceServlet implements TipifiedListService {

	private static final long serialVersionUID = 1L;

	@Override
	public TipifiedListItem[] getListItems(String listId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipifiedListItem createListItem(String listId, TipifiedListItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipifiedListItem saveListItem(String listId, TipifiedListItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteListItem(String listId, String itemId) {
		// TODO Auto-generated method stub
		
	}
}
