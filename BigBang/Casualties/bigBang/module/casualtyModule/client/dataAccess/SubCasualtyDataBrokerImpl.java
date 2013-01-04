package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;
import bigBang.module.casualtyModule.interfaces.SubCasualtyServiceAsync;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter.SortableField;

public class SubCasualtyDataBrokerImpl extends DataBroker<SubCasualty>
implements SubCasualtyDataBroker{

	protected SubCasualtyServiceAsync service;
	protected SubCasualtySearchDataBroker searchBroker;

	public SubCasualtyDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.SUB_CASUALTY;
		this.service = SubCasualtyService.Util.getInstance();
		this.searchBroker = new SubCasualtySearchDataBroker();
	}

	@Override
	public void requireDataRefresh() {
		return;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		getSubCasualty(itemId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).addSubCasualty(response);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		for(DataBrokerClient<SubCasualty> client : getClients()) {
			((SubCasualtyDataBrokerClient) client).removeSubCasualty(itemId);
			((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getSubCasualty(itemId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(response);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getSubCasualty(String id, final ResponseHandler<SubCasualty> handler) {
		service.getSubCasualty(id, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(result);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty,
			final ResponseHandler<SubCasualty> handler) {
		service.editSubCasualty(subCasualty, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(result);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.UPDATE_SUB_CASUALTY, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void deleteSubCasualty(final String subCasualtyId, String reason,
			final ResponseHandler<Void> handler) {
		service.deleteSubCasualty(subCasualtyId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).removeSubCasualty(subCasualtyId);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY, subCasualtyId));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getSubCasualties(String ownerId,
			final ResponseHandler<Collection<SubCasualtyStub>> responseHandler) {

		SubCasualtySearchParameter parameter = new SubCasualtySearchParameter();
		parameter.ownerId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SubCasualtySortParameter sort = new SubCasualtySortParameter(SortableField.NUMBER, SortOrder.DESC);

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<SubCasualtyStub>>() {

			@Override
			public void onResponse(Search<SubCasualtyStub> response) {
				responseHandler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				responseHandler.onError(new String[]{
						new String("Could not get the sub casualties for the given owner")	
				});
			}
		}, true);
	}

	@Override
	public SearchDataBroker<SubCasualtyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void markForClosing(String subCasualtyId, String revisorId,
			final ResponseHandler<Void> handler) {
		service.markForClosing(subCasualtyId, revisorId, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.MARK_CLOSE_SUB_CASUALTY, result.id));
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not mark the closing")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void closeSubCasualty(String subCasualtyId,
			final ResponseHandler<Void> handler) {
		service.closeProcess(subCasualtyId, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.CLOSE_SUB_CASUALTY, result.id));
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not close the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void rejectCloseSubCasualty(String subCasualtyId, String reason,
			final ResponseHandler<Void> handler) {
		service.rejectClosing(subCasualtyId, reason, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.REJECT_CLOSE_SUB_CASUALTY, result.id));
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not reject the sub casualty closing")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void markNotificationSent(String subCasualtyId,
			final ResponseHandler<SubCasualty> responseHandler) {
service.sendNotification(subCasualtyId, new BigBangAsyncCallback<SubCasualty>() {

	@Override
	public void onResponseSuccess(SubCasualty result) {
		EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.MARK_NOTIFICATION_SENT, result.id));
		responseHandler.onResponse(result);
		
	}
	
	@Override
	public void onResponseFailure(Throwable caught) {
		responseHandler.onError(new String[]{
				new String("Could not send notification")	
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.CONVERSATION, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the message")		
				});	
				super.onResponseFailure(caught);
			}


		});			}

	@Override
	public void receiveMessage(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.receiveMessage(conversation, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.CONVERSATION, result.id));
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
	public void createAssessment(Assessment assessment, final ResponseHandler<Assessment> handler){
		service.createAssessment(assessment, new BigBangAsyncCallback<Assessment>() {

			@Override
			public void onResponseSuccess(Assessment result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_ASSESSMENT, result.id));
				handler.onResponse(result);								
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the assessment")		
				});	
				super.onResponseFailure(caught);
			}
		});
	}

}
