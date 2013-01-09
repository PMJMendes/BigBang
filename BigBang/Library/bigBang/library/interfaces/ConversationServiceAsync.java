package bigBang.library.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversationServiceAsync
	extends SearchServiceAsync, DependentItemSubServiceAsync
{
	void getConversation(String id, AsyncCallback<Conversation> callback);
	void createFromEmail(Conversation conversation, AsyncCallback<Conversation> callback);
	void saveConversation(Conversation conversation, AsyncCallback<Conversation> callback);
	void sendMessage(Message message, Integer replylimit, AsyncCallback<Conversation> callback);
	void repeatMessage(Message message, Integer replylimit, AsyncCallback<Conversation> callback);
	void receiveMessage(Message message, Integer replylimit, AsyncCallback<Conversation> callback);
	void closeConversation(String conversationId, String motiveId, AsyncCallback<Void> callback);
}
