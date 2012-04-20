package bigBang.module.expenseModule.client.dataAccess;

import java.util.Collection;


import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.interfaces.ExpenseServiceAsync;

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
					handler.onError(new String[0]);
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
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[0]);
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
						handler.onResponse(id);
					}
					
					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[0]);
						super.onResponseFailure(caught);
					}
					
				});
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[0]);
			}
			
			
			
		});
	}


	@Override
	public SearchDataBroker<ExpenseStub> getSearchBroker() {
		return this.searchBroker;
	}

}
