package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFileStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.MedicalFileBrokerClient;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.casualtyModule.interfaces.MedicalFileService;
import bigBang.module.casualtyModule.interfaces.MedicalFileServiceAsync;

public class MedicalFileBrokerImpl extends DataBroker<MedicalFile> implements MedicalFileBroker{

	protected MedicalFileServiceAsync service;
	protected MedicalFileSearchBroker searchBroker;
	
	public MedicalFileBrokerImpl(){
		this.service = MedicalFileService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.MEDICAL_FILE;
		searchBroker = new MedicalFileSearchBroker();
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
		getMedicalFile(itemId, new ResponseHandler<MedicalFile>(){

			@Override
			public void onResponse(MedicalFile response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}

		});
	}

	@Override
	public void getMedicalFile(String id, final ResponseHandler<MedicalFile> handler) {
		service.getMedicalFile(id, new BigBangAsyncCallback<MedicalFile>() {

			@Override
			public void onResponseSuccess(MedicalFile result) {
				incrementDataVersion();
				for(DataBrokerClient<MedicalFile> client: clients){
					((MedicalFileBrokerClient) client).updateMedicalFile(result);
				}
				handler.onResponse(result);
			}


			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the medical file")
				});

				super.onResponseFailure(caught);

			}

		});
	}

	@Override
	public void editMedicalFile(MedicalFile file,
			final ResponseHandler<MedicalFile> handler) {
		service.editMedicalFile(file, new BigBangAsyncCallback<MedicalFile>() {

			@Override
			public void onResponseSuccess(MedicalFile result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.MedicalFileProcess.EDIT, result.id));
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.MedicalFileProcess.CONVERSATION, result.id));
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
	public void closeProcess(String id, final ResponseHandler<MedicalFile> handler) {
		service.closeProcess(id, new BigBangAsyncCallback<MedicalFile>(){


			@Override
			public void onResponseSuccess(MedicalFile result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.MedicalFileProcess.CLOSE, result.id));
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
	public SearchDataBroker<MedicalFileStub> getSearchBroker() {
		return this.searchBroker;
	}

}
