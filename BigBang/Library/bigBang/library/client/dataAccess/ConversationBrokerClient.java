package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.Conversation;

public interface ConversationBrokerClient extends DataBrokerClient<Conversation>{

	void updateConversation(Conversation response);

}
