package bigBang.library.client.dataAccess;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.TipifiedListItem;

public interface ConversationBroker extends DataBrokerInterface<Conversation>{

	void getConversation(String conversationId,
			ResponseHandler<Conversation> handler);

	void saveConversation(Conversation conversation,
			ResponseHandler<Conversation> handler);

	void getForReply(String messageId,
			ResponseHandler<Message> handler);

	void getForReplyAll(String messageId,
			ResponseHandler<Message> handler);

	void getForForward(String messageId,
			ResponseHandler<Message> handler);

	void sendMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void repeatMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void receiveMessage(Message message, Integer replyLimit, 
			ResponseHandler<Conversation> handler);
	
	void closeConversation(String conversationId, String motiveId,
			ResponseHandler<Void> handler);

	void getConversations(String listId, String filterId,
			ResponseHandler<List<TipifiedListItem>> handler);

	void createConversationFromEmail(Conversation conv,
			ResponseHandler<Conversation> handler);

	void getConversationsForOwner(String ownerId,
			ResponseHandler<Collection<ConversationStub>> responseHandler);

	SearchDataBroker<ConversationStub> getSearchBroker();

	void getForPrinting(String conversationId,
			ResponseHandler<String> responseHandler);

	void reopenConversation(String id, String directionId, int replyLimit,
			ResponseHandler<Conversation> responseHandler);
}
