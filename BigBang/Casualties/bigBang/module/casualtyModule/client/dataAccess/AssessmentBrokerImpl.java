package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.AssessmentStub;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.AssessmentBrokerClient;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.casualtyModule.interfaces.AssessmentService;
import bigBang.module.casualtyModule.interfaces.AssessmentServiceAsync;

public class AssessmentBrokerImpl extends DataBroker<Assessment> implements AssessmentBroker{

	protected AssessmentServiceAsync service;
	protected AssessmentSearchBroker searchBroker;

	public AssessmentBrokerImpl() {
		this.service = AssessmentService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.ASSESSMENT;
		this.searchBroker = new AssessmentSearchBroker();
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
		getAssessment(itemId, new ResponseHandler<Assessment>(){

			@Override
			public void onResponse(Assessment response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}

		});
	}

	@Override
	public void getAssessment(String id, final ResponseHandler<Assessment> handler) {
		service.getAssessment(id, new BigBangAsyncCallback<Assessment>() {

			@Override
			public void onResponseSuccess(Assessment result) {
				incrementDataVersion();
				for(DataBrokerClient<Assessment> client : clients){
					((AssessmentBrokerClient) client).updateAssessment(result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the assessment")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void editAssessment(Assessment assessment, final ResponseHandler<Assessment> handler) {
		service.editAssessment(assessment, new BigBangAsyncCallback<Assessment>() {

			@Override
			public void onResponseSuccess(Assessment result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.AssessmentProcess.EDIT, result.id));
				incrementDataVersion();
				notifyItemUpdate(result.id);
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not edit the assessment")
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.AssessmentProcess.CONVERSATION, result.id));
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.AssessmentProcess.CONVERSATION, result.id));
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
	public void closeProcess(String id, final ResponseHandler<Assessment> handler) {
		service.closeProcess(id, new BigBangAsyncCallback<Assessment>(){


			@Override
			public void onResponseSuccess(Assessment result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.AssessmentProcess.CLOSE, result.id));
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
	public SearchDataBroker<AssessmentStub> getSearchBroker() {
		return this.searchBroker;
	}

}
