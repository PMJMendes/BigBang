package bigBang.library.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ConversationService")
public interface ConversationService
	extends RemoteService, DependentItemSubService
{
	public static class Util
	{
		private static ConversationServiceAsync instance;

		public static ConversationServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(ConversationService.class);

			return instance;
		}
	}

	public Conversation getConversation(String id) throws SessionExpiredException, BigBangException;

	public Conversation createFromEmail(Conversation conversation) throws SessionExpiredException, BigBangException;

	public Conversation saveConversation(Conversation conversation) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Message message, Integer replylimit) throws SessionExpiredException, BigBangException;
	public Conversation repeatMessage(Message message, Integer replylimit) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Message message, Integer replylimit) throws SessionExpiredException, BigBangException;

	public void closeConversation(String conversationId, String motiveId) throws SessionExpiredException, BigBangException;
}
