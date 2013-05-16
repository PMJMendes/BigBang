package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.casualtyModule.interfaces.TotalLossService;
import bigBang.module.casualtyModule.interfaces.TotalLossServiceAsync;

public class TotalLossFileBrokerImpl extends DataBroker<TotalLossFile> implements TotalLossFileBroker{

	protected TotalLossServiceAsync service;
	protected TotalLossFileSearchBroker searchBroker;

	public TotalLossFileBrokerImpl(){
		this.service = TotalLossService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.TOTAL_LOSS_FILE;
		searchBroker = new TotalLossFileSearchBroker();
	}

	@Override
	public void requireDataRefresh() {
		return;		
	}


	@Override
	public void notifyItemCreation(String itemId) {
		return;
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		return;
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getTotalLossFile(itemId, new ResponseHandler<TotalLossFile>(){

			@Override
			public void onResponse(TotalLossFile response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}

		});
	}

	@Override
	public void getTotalLossFile(String id,
			final ResponseHandler<TotalLossFile> handler) {
		service.getTotalLossFile(id, new BigBangAsyncCallback<TotalLossFile>() {

			@Override
			public void onResponseSuccess(TotalLossFile result) {
				incrementDataVersion();
				for(DataBrokerClient<TotalLossFile> client : clients){
					((TotalLossFileBrokerClient)client).updateTotalLossFile(result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the total loss file")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void editTotalLossFile(TotalLossFile file,
			final ResponseHandler<TotalLossFile> handler) {
		service.editTotalLossFile(file, new BigBangAsyncCallback<TotalLossFile>() {

			@Override
			public void onResponseSuccess(TotalLossFile result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.TotalLossFileProcess.EDIT, result.id));
				incrementDataVersion();
				for(DataBrokerClient<TotalLossFile> client : clients){
					((TotalLossFileBrokerClient)client).updateTotalLossFile(result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the total loss file")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void sendMessage(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.sendMessage(conversation, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.TotalLossFileProcess.CONVERSATION, result.id));
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
	public void receiveMessage(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.receiveMessage(conversation, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.TotalLossFileProcess.CONVERSATION, result.id));
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
	public void closeProcess(String id, final ResponseHandler<TotalLossFile> handler) {
		service.closeProcess(id, new BigBangAsyncCallback<TotalLossFile>(){


			@Override
			public void onResponseSuccess(TotalLossFile result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.TotalLossFileProcess.CLOSE, result.id));
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);
			}			
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not close the process")		
				});	
				super.onResponseFailure(caught);
			}

	});
	}

	@Override
	public SearchDataBroker<TotalLossFileStub> getSearchBroker() {
		return searchBroker;
	}

}
