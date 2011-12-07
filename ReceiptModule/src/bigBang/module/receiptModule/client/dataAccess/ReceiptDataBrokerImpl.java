package bigBang.module.receiptModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;

public class ReceiptDataBrokerImpl extends DataBroker<Receipt> implements ReceiptProcessDataBroker{

	protected ReceiptServiceAsync service;
	protected SearchDataBroker<ReceiptStub> searchBroker;
	protected boolean refreshRequired = true;
	
	public ReceiptDataBrokerImpl(){
		this(ReceiptService.Util.getInstance());
	}
	
	public ReceiptDataBrokerImpl(ReceiptServiceAsync service){
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.RECEIPT;
		this.searchBroker = new ReceiptSearchDataBroker(this.service);
	}
	
	@Override
	public void requireDataRefresh() {
		this.refreshRequired = true;
	}
	
	protected boolean isRefreshRequired(){
		return this.refreshRequired;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getReceipt(itemId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).addReceipt(response);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<Receipt> bc : getClients()){
			((ReceiptDataBrokerClient) bc).removeReceipt(itemId);
			((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getReceipt(itemId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(response);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void getReceipt(final String id, final ResponseHandler<Receipt> handler) {
		if(cache.contains(id) && !refreshRequired) {
			handler.onResponse((Receipt) cache.get(id));
		}else{
			this.service.getReceipt(id, new BigBangAsyncCallback<Receipt>() {

				@Override
				public void onSuccess(Receipt result) {
					cache.add(id, result);
					incrementDataVersion();
					for(DataBrokerClient<Receipt> bc : getClients()){
						((ReceiptDataBrokerClient) bc).updateReceipt(result);
						((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
					}
					handler.onResponse(result);
					refreshRequired = false;
				}
			});
		}
	}

	@Override
	public void updateReceipt(final Receipt receipt, final ResponseHandler<Receipt> handler) {
		this.service.editReceipt(receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onSuccess(Receipt result) {
				cache.add(receipt.id, receipt);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeReceipt(final String id, final ResponseHandler<String> handler) {
		this.getReceipt(id, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(final Receipt response) {
				service.deleteReceipt(id, new BigBangAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						cache.remove(id);
						incrementDataVersion();
						for(DataBrokerClient<Receipt> bc : getClients()){
							((ReceiptDataBrokerClient) bc).removeReceipt(id);
							((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
						}
						handler.onResponse(id);
					}
				});		
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}

	@Override
	public SearchDataBroker<ReceiptStub> getSearchBroker() {
		return this.searchBroker;
	}

}
