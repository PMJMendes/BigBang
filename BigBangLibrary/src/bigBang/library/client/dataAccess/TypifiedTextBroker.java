package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TypifiedText;

public interface TypifiedTextBroker {

	
	public void registerClient(String listId, TypifiedTextClient client);
	
	public void unregisterClient(String listId, TypifiedTextClient client);
	
	public void isClientRegistered(String listId, TypifiedTextClient client);
	
	public void refreshListData(String listId);
	
	public List<TypifiedText> getTextitems(String listId);
	
	public TypifiedText getListItem(String listId, String itemId);
	
	public void createListItem(String listId, TypifiedText item, ResponseHandler<TypifiedText> handler);

	public void removeListItem(String listId, String itemId, ResponseHandler<TypifiedText> handler);
	
	public void saveListItem(String listId, TypifiedText item, ResponseHandler<TypifiedText> handler);

}
