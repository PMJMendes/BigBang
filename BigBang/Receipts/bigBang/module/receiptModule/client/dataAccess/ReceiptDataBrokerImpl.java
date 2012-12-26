package bigBang.module.receiptModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.ImageItem;
import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.PaymentInfo;
import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.Rectangle;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter.SortableField;

public class ReceiptDataBrokerImpl extends DataBroker<Receipt> implements ReceiptDataBroker{

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
		this.cache.setEnabled(false);
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
				public void onResponseSuccess(Receipt result) {
					cache.add(id, result);
					incrementDataVersion();
					for(DataBrokerClient<Receipt> bc : getClients()){
						((ReceiptDataBrokerClient) bc).updateReceipt(result);
						((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
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
	public void updateReceipt(final Receipt receipt, final ResponseHandler<Receipt> handler) {
		this.service.editReceipt(receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(receipt.id, receipt);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.UPDATE_RECEIPT, result.id));
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
	public void updateAndValidateReceipt(final Receipt receipt,
			Rectangle imageCut, final ResponseHandler<Receipt> handler) {
		this.service.editAndValidateReceipt(receipt, imageCut, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(receipt.id, receipt);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.UPDATE_RECEIPT, result.id));
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.VALIDATE, result.id));
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
	public void removeReceipt(final String id, final ResponseHandler<String> handler) {
		this.getReceipt(id, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(final Receipt response) {
				service.deleteReceipt(id, new BigBangAsyncCallback<Void>() {

					@Override
					public void onResponseSuccess(Void result) {
						cache.remove(id);
						incrementDataVersion();
						for(DataBrokerClient<Receipt> bc : getClients()){
							((ReceiptDataBrokerClient) bc).removeReceipt(id);
							((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
						}
						EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.DELETE_RECEIPT, id));
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
	public void getReceiptsForOwner(String ownerId,
			final ResponseHandler<Collection<ReceiptStub>> handler) {
		ReceiptSearchParameter parameter = new ReceiptSearchParameter();
		parameter.ownerId = ownerId;

		ReceiptSearchParameter[] parameters = new ReceiptSearchParameter[]{
				parameter
		};

		ReceiptSortParameter sort = new ReceiptSortParameter(SortableField.MATURITY_DATE, SortOrder.DESC);
		ReceiptSortParameter[] sorts = new ReceiptSortParameter[]{
				sort
		};

		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<ReceiptStub>>() {

			@Override
			public void onResponse(Search<ReceiptStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the receipts for the owner id")
				});
			}
		}, true);

	}

	@Override
	public void transferToInsurancePolicy(String receiptId, String newPolicyId, final ResponseHandler<Receipt> handler){
		service.transferToPolicy(receiptId, newPolicyId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.TRANSFER_TO_POLICY, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not transfer receipt to insurance policy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public SearchDataBroker<ReceiptStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void associateWithDebitNote(String receiptId, String debitNoteId, final ResponseHandler<Receipt> handler){
		service.associateWithDebitNote(receiptId, debitNoteId, new BigBangAsyncCallback<Receipt>() {
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.ASSOCIATE_WITH_DEBIT_NOTE, result.id));
				handler.onResponse(result);
			}


			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not associate receipt with debit note")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getRelevantDebitNotes(String receiptId, final ResponseHandler<DebitNote[]> handler){
		service.getRelevantDebitNotes(receiptId, new BigBangAsyncCallback<DebitNote[]>() {

			@Override
			public void onResponseSuccess(DebitNote[] result) {
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get relevant debit notes")
				});
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void setForReturn(ReturnMessage message, final ResponseHandler<Receipt> handler){
		service.setForReturn(message, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SET_FOR_RETURN, result.id));
				handler.onResponse(result);
			};

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not set receipt for return")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void validateReceipt(String receiptId, final ResponseHandler<Receipt> handler){
		service.validateReceipt(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.VALIDATE, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not validate receipt")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getReceiptsWithNumber(String label, final ResponseHandler<Collection<ReceiptStub>> handler){
		service.getExactResults(label, new BigBangAsyncCallback<SearchResult[]>() {

			@Override
			public void onResponseSuccess(SearchResult[] result) {
				ArrayList<ReceiptStub> receipts = new ArrayList<ReceiptStub>();

				for(int i = 0; i<result.length; i++){
					receipts.add((ReceiptStub) result[i]);
				}
				handler.onResponse(receipts);
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not find exact results")
				});
				super.onResponseFailure(caught);


			}
		});
	}

	@Override
	public void serialCreateReceipt(Receipt receipt, DocuShareHandle source, final ResponseHandler<Receipt> handler){
		service.serialCreateReceipt(receipt, source, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save receipt (SerialCreateReceipt)")
				});
				super.onResponseFailure(caught);

			}

		});
	}

	@Override
	public void receiveImage(String receiptId, DocuShareHandle source,
			final ResponseHandler<Receipt> handler) {
		service.receiveImage(receiptId, source, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save receipt (SerialCreateReceipt)")
				});
				super.onResponseFailure(caught);
			}

		});

	}

	@Override
	public void massCreatePaymentNotice(String[] receiptIds, final ResponseHandler<Void> handler){
		service.massCreatePaymentNotice(receiptIds, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.CREATE_PAYMENT_NOTICE, null));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the mass payment notice")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void createPaymentNotice(String receiptId, final ResponseHandler<Receipt> handler){
		service.createPaymentNotice(receiptId,new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.CREATE_PAYMENT_NOTICE, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create payment notice")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void sendPayment(String receiptId, final ResponseHandler<Receipt> handler){
		service.sendPayment(receiptId, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SEND_PAYMENT_TO_CLIENT, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send payment")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void returnToInsurer(String receiptId, final
			ResponseHandler<Receipt> handler) {
		service.returnToInsurer(receiptId, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.RETURN_TO_AGENCY, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not return to insurer")
				});
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void massReturnToInsurer(String[] receiptIds,
			final ResponseHandler<Void> handler) {
		service.massReturnToInsurer(receiptIds, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.RETURN_TO_AGENCY, null));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not mass return receipts to insurer")
				});
				super.onResponseFailure(caught);
			}

		});

	}

	@Override
	public void createSignatureRequest(SignatureRequest request,
			final ResponseHandler<Receipt> handler) {
		service.createSignatureRequest(request, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.CREATE_SIGNATURE_REQUEST, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create signature request")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void markPayed(PaymentInfo paymentInfo,
			final ResponseHandler<Receipt> handler) {
		service.markPayed(paymentInfo, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.MARK_FOR_PAYMENT, result.id));
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not mark for Payment")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void sendReceipt(String receiptId, final ResponseHandler<Void> handler) {
		service.sendReceipt(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				incrementDataVersion();
				cache.add(result.id, result);
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SEND_RECEIPT, result.id));
				handler.onResponse(null);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the receipt")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void sendReceipt(final String[] receiptIds, final ResponseHandler<Void> handler) {
		service.massSendReceipt(receiptIds, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				for(String id : receiptIds) {
					cache.remove(id);
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SEND_RECEIPT, null));
				handler.onResponse(null);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the receipt")
				});
				super.onResponseFailure(caught);
			}
		});
	}


	@Override
	public void insurerAccounting(String receiptId, final ResponseHandler<Void> handler) {
		service.insurerAccouting(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				incrementDataVersion();
				cache.add(result.id, result);
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.INSURER_ACCOUNTING, result.id));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the receipt")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void insurerAccounting(final String[] receiptIds, InsurerAccountingExtra[] extras, final ResponseHandler<Void> handler) {
		service.massInsurerAccounting(receiptIds, extras, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.INSURER_ACCOUNTING, null));
				handler.onResponse(null);
				for(String id : receiptIds) {
					cache.remove(id);
				}

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not perform the insurer accounting")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void agentAccounting(String receiptId, final ResponseHandler<Void> handler) {
		service.mediatorAccounting(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				incrementDataVersion();
				cache.add(result.id, result);
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.AGENT_ACCOUNTING, result.id));
				handler.onResponse(null);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the accoutning to the mediator")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void agentAccounting(final String[] receiptIds, final ResponseHandler<Void> handler) {
		service.massMediatorAccounting(receiptIds, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				for(String id : receiptIds) {
					cache.remove(id);
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.AGENT_ACCOUNTING, null));
				handler.onResponse(null);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not perform the mediator accounting")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void setDASNotNecessary(String receiptId, final ResponseHandler<Receipt> handler){
		service.setDASNotNecessary(receiptId, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SET_DAS_NOT_NECESSARY, result.id));

				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not set DAS as not necessary")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createDASRequest(DASRequest request,
			final ResponseHandler<Receipt> handler) {
		service.createDASRequest(request, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.CREATE_DAS_REQUEST, result.id));
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create DAS request")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void indicateNotPaid(String receiptId,
			final ResponseHandler<Receipt> handler) {
		service.markNotPayed(receiptId, new BigBangAsyncCallback<Receipt>() {
			@Override
			public void onResponseSuccess(Receipt result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Receipt> bc : getClients()){
					((ReceiptDataBrokerClient) bc).updateReceipt(result);
					((ReceiptDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.RECEIPT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.NOT_PAID_INDICATION, result.id));
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not set receipt as not paid")
				});
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void massCreateSignatureRequest(String[] receiptIds, int replyLimit,
			final ResponseHandler<Void> handler) {
		service.massCreateSignatureRequest(receiptIds, replyLimit, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.CREATE_SIGNATURE_REQUEST, null));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						"Cannot create Signature requests"
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void returnPayment(String receiptId,
			final ResponseHandler<Receipt> handler) {
		service.returnPayment(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						"Could not return payment"
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getReceiptImageItem(String receiptId,
			final ResponseHandler<ImageItem> responseHandler) {
		service.getItemAsImage(receiptId, 0, new BigBangAsyncCallback<ImageItem>() {

			@Override
			public void onResponseSuccess(ImageItem result) {
				responseHandler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						"Cannot get the receipt image"
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void massSendPayment(String[] array,
			final ResponseHandler<Void> responseHandler) {

		service.massSendPayment(array, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.SEND_PAYMENT_TO_CLIENT, null));
				responseHandler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						"Cannot create Signature requests"
				});
				super.onResponseFailure(caught);
			}
		});

	}
	
	@Override
	public void massGenerateReceipt(String[] array,
			final ResponseHandler<Void> responseHandler) {
		
		service.massCreateInternalReceipt(array, new BigBangAsyncCallback<Void>() {
			
			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.RECEIPT_GENERATION, null));
				responseHandler.onResponse(null);
				
			}
		
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						"Cannot generate receipts"
				});
				super.onResponseFailure(caught);
			}
		
		});
		
	}
	
	@Override
	public void generateReceipt(final String receiptId,
			final ResponseHandler<Receipt> responseHandler) {
		service.createInternalReceipt(receiptId, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ReceiptProcess.RECEIPT_GENERATION, receiptId));
				responseHandler.onResponse(null);
				
			}
		
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						"Cannot generate receipts"
				});
				super.onResponseFailure(caught);
			}
			
		});

	}

	@Override
	public void sendMessage(Conversation info,
			final ResponseHandler<Conversation> responseHandler) {
		service.sendMessage(info, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CONVERSATION, result.id));
				responseHandler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not send the message")		
				});	
				super.onResponseFailure(caught);
			}


		});				
	}
	
	@Override
	public void receiveMessage(Conversation info, 
			final ResponseHandler<Conversation> responseHandler) {
		service.sendMessage(info, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CONVERSATION, result.id));
				responseHandler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not receive the message")		
				});	
				super.onResponseFailure(caught);
			}
		});					}

}

