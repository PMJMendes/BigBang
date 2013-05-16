package bigBang.library.client;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.history.NavigationHistoryItem;

public interface ProcessNavigationMapper {
	
	void getProcessNavigationItem(String typeId, String instanceId, ResponseHandler<NavigationHistoryItem> handler);

}
