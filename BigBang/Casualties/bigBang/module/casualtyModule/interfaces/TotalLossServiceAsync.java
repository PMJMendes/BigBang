package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TotalLossServiceAsync
	extends SearchServiceAsync
{
	void getTotalLossFile(String id, AsyncCallback<TotalLossFile> callback);
	void editTotalLossFile(TotalLossFile file, AsyncCallback<TotalLossFile> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void closeProcess(String id, AsyncCallback<TotalLossFile> callback);
}
