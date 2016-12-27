package bigBang.module.expenseModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.interfaces.ExpenseServiceAsync;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter.SortableField;

public class ExpenseBrokerImpl extends DataBroker<Expense> implements ExpenseDataBroker{

	private ExpenseServiceAsync service;
	private SearchDataBroker<ExpenseStub> searchBroker;
	private boolean refreshRequired;

	public ExpenseBrokerImpl() {
		this(ExpenseService.Util.getInstance());
	}


	public ExpenseBrokerImpl(ExpenseServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.EXPENSE;
		this.searchBroker = new ExpenseSearchDataBroker(this.service);
		this.cache.setEnabled(false);
	}
	@Override
	public void requireDataRefresh() {
		this.refreshRequired = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getExpense(itemId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).addExpense(response);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {	}
		});

	}

	@Override
	public void notifyItemDeletion(String itemId) {
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<Expense> bc : getClients())
		{
			((ExpenseDataBrokerClient)bc).deleteExpense(itemId);
			((ExpenseDataBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getExpense(itemId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(response);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {	}
		});

	}

	@Override
	public void getExpense(final String id, final ResponseHandler<Expense> handler){
		if(cache.contains(id) && !refreshRequired){
			handler.onResponse((Expense) cache.get(id));
		}else{
			service.getExpense(id, new BigBangAsyncCallback<Expense>() {

				@Override
				public void onResponseSuccess(Expense result) {
					cache.add(id, result);
					incrementDataVersion();
					for(DataBrokerClient<Expense> bc : getClients()){
						((ExpenseDataBrokerClient) bc).updateExpense(result);
						((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
					}
					handler.onResponse(result);
					refreshRequired = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get expense")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void updateExpense(final Expense expense, final ResponseHandler<Expense> handler){
		service.editExpense(expense, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.UPDATE_EXPENSE, result.id));

				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update expense")
				});
				super.onResponseFailure(caught);
			}


		});
	}

	@Override
	public void removeExpense(final String id, final String reason, final ResponseHandler<String> handler){
		getExpense(id, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				service.deleteExpense(id, reason, new BigBangAsyncCallback<Void>() {
					@Override
					public void onResponseSuccess(Void result) {
						cache.remove(id);
						incrementDataVersion();
						for(DataBrokerClient<Expense> bc : getClients()){
							((ExpenseDataBrokerClient) bc).deleteExpense(id);
							((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
						}
						EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.DELETE_EXPENSE,  id));

						handler.onResponse(id);
					}

					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								new String("Could not remove expense")
						});
						super.onResponseFailure(caught);
					}

				});

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not remove expense")
				});
			}



		});
	}


	@Override
	public SearchDataBroker<ExpenseStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void sendNotification(String id, final ResponseHandler<Expense> handler){
		service.sendNotification(id, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.SEND_NOTIFICATION,  result.id));

				handler.onResponse(result);	

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send notification")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void receiveAcceptance(Acceptance acceptance,
			final ResponseHandler<Expense> handler) {
		service.receiveAcceptance(acceptance, new BigBangAsyncCallback<Expense>() {
			@Override
			public void onResponseSuccess(Expense result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.RECEIVE_ACCEPTANCE,  result.id));

				handler.onResponse(result);	
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not receive acceptance")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void receiveReturn(ReturnEx returnEx,
			final ResponseHandler<Expense> handler) {
		service.receiveReturn(returnEx, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.RECEIVE_RETURN,  result.id));

				handler.onResponse(result);	

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get receive return")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void massSendNotification(String[] expenseIds,
			final ResponseHandler<Void> handler) {
		service.massSendNotification(expenseIds, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.SEND_NOTIFICATION,  null));

				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get send notifications")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void notifyClient(String expenseId,
			final ResponseHandler<Expense> responseHandler) {
		service.notifyClient(expenseId, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result){
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.NOTIFY_CLIENT,  result.id));

				responseHandler.onResponse(result);	

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not get receive return")
				});
				super.onResponseFailure(caught);
			}

		});

	}

	@Override
	public void returnToClient(String expenseId, final ResponseHandler <Expense> responseHandler){
		service.returnToClient(expenseId, new BigBangAsyncCallback<Expense>() {
			@Override
			public void onResponseSuccess(Expense result){
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.RETURN_TO_CLIENT,  result.id));

				responseHandler.onResponse(result);	

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not get receive return")
				});
				super.onResponseFailure(caught);
			}

		});


	}
	
	@Override
	public void massReturnToClient(String[] expenseIds, final ResponseHandler<Void> handler){
		service.massReturnToClient(expenseIds, new BigBangAsyncCallback<Void>() {
			
			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.RETURN_TO_CLIENT,  null));

				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not return to clients")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void massNotifyClient(String[] toNotify,
			final ResponseHandler<Void> responseHandler) {
		service.massNotifyClient(toNotify, new BigBangAsyncCallback<Void>() {
			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.NOTIFY_CLIENT,  null));

				responseHandler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not notify clients")
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void getExpensesForOwner(String ownerId,
			final ResponseHandler<Collection<ExpenseStub>> handler) {
		ExpenseSearchParameter parameter = new ExpenseSearchParameter();
		parameter.ownerId = ownerId;
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		// jcamilo: change from relevance to date by request from Ana (Saúde)
		ExpenseSortParameter sort = new ExpenseSortParameter(SortableField.DATE, SortOrder.DESC);
		SortParameter[] sorts = new SortParameter[]{
				sort
		};
		
		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<ExpenseStub>>() {

			@Override
			public void onResponse(Search<ExpenseStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the expenses for the given owner id")
				});
			}
		}, true);
	}


	@Override
	public void serialCreateExpense(Expense expense, ScanHandle handle,
			final ResponseHandler<Expense> responseHandler) {
		service.serialCreateExpense(expense, handle, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Expense> bc : getClients()){
					((ExpenseDataBrokerClient) bc).updateExpense(result);
					((ExpenseDataBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.EXPENSE, getCurrentDataVersion());
				}
				responseHandler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not save expense (SerialCreateExpense)")
				});
				super.onResponseFailure(caught);
			}
		
		});
		
	}


	@Override
	public void massReceiveProof(String[] toReceive, ScanHandle handle,
			final ResponseHandler<Void> responseHandler) {
		service.massReceiveReception(toReceive, handle, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				responseHandler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						"Could not receive proof"
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.CONVERSATION, result.id));
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
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExpenseProcess.CONVERSATION, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not receive the message " + caught.getMessage() + " " + caught.getCause() + " " + caught.getClass())
				});	
				super.onResponseFailure(caught);
			}

		});		
	}

}
