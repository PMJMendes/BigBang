package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

public interface ConversationBroker extends DataBrokerInterface<Conversation>{

	void getConversation(String conversationId,
			ResponseHandler<Conversation> handler);

	void saveConversation(Conversation conversation,
			ResponseHandler<Conversation> handler);
	
	void sendMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void repeatMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void receiveMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void closeConversation(String conversationId, String motiveId,
			ResponseHandler<Void> handler);
}
