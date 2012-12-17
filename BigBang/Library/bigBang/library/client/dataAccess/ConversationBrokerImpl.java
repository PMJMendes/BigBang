package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.interfaces.ConversationService;
import bigBang.library.interfaces.ConversationServiceAsync;

public class ConversationBrokerImpl extends DataBroker<Conversation> implements ConversationBroker{

	protected ConversationServiceAsync service;

	public ConversationBrokerImpl(){
		this(ConversationService.Util.getInstance());
	}

	public ConversationBrokerImpl(ConversationServiceAsync instance) {
		this.service = instance;
		this.dataElementId = BigBangConstants.EntityIds.CONVERSATION;
	}

	@Override
	public void getConversation(final String conversationId, final ResponseHandler<Conversation> handler){
		service.getConversation(conversationId, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				incrementDataVersion();
				for(DataBrokerClient<Conversation> client : clients){
					((ConversationBrokerClient) client).updateConversation(result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the conversation")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void notifyItemCreation(String itemId) {
		return; //NAO FAZ SENTIDO EM CONVERSAÇÃO, visto que só se vÊ uma de cada vez.
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		return; //NAO ACONTECE

	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getConversation(itemId, new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				incrementDataVersion();
				for(DataBrokerClient<Conversation> client : clients){
					((ConversationBrokerClient) client).updateConversation(response);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}

		});
	}

	@Override
	public void requireDataRefresh() {
		return;		
	}


	@Override
	public void sendMessage(Message message, Integer replyLimit,
			final ResponseHandler<Conversation> handler) {
		service.sendMessage(message, replyLimit, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ConversationProcess.SEND, result.id));
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);				
			}


			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the message")
				});			
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void repeatMessage(Message message, Integer replyLimit,
			final ResponseHandler<Conversation> handler) {
		service.repeatMessage(message, replyLimit, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ConversationProcess.REPEAT, result.id));
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);						
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not repeat the message")
				});			
				super.onResponseFailure(caught);			}
		})
		;
	}

	@Override
	public void receiveMessage(Message message, Integer replyLimit,
			final ResponseHandler<Conversation> handler) {
		service.receiveMessage(message, replyLimit, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ConversationProcess.RECEIVE, result.id));
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);							
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not receive the message")
				});			
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void closeConversation(final String conversationId, String motiveId,
			final ResponseHandler<Void> handler) {
		service.closeConversation(conversationId, motiveId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ConversationProcess.CLOSE, conversationId));
				incrementDataVersion();
				notifyItemUpdate(conversationId);
				handler.onResponse(result);			
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not close the conversation")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void saveConversation(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.saveConversation(conversation, new BigBangAsyncCallback<Conversation>() {


			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save the conversation")
				});
				super.onResponseFailure(caught);
			}


			@Override
			public void onResponseSuccess(Conversation result) {
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);			

			}
		});
	}

	@Override
	public void getConversations(String listId,String filterId, final ResponseHandler<List<TipifiedListItem>> handler){
		service.getListItemsFilter(listId, filterId, new BigBangAsyncCallback<TipifiedListItem[]>() {

			@Override
			public void onResponseSuccess(TipifiedListItem[] result) {
				List<TipifiedListItem> toReturn = new ArrayList<TipifiedListItem>();
				for(TipifiedListItem item : result){
					toReturn.add(0, item);
				}
				handler.onResponse(toReturn);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the conversations")
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void createConversationFromEmail(Conversation conv, final ResponseHandler<Conversation> handler){
		service.createFromEmail(conv, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);
				
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the conversation")
				});
				super.onResponseFailure(caught);
			}
		
		});
	}
}

