package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSortParameter;

public class QuoteRequestInsuredObjectBrokerImpl extends DataBroker<QuoteRequestObject>
		implements QuoteRequestObjectDataBroker {

	protected QuoteRequestObjectServiceAsync service;
	protected QuoteRequestServiceAsync requestService;
	protected SearchDataBroker<QuoteRequestObjectStub> searchBroker;

	protected Map<String, String> objectsInScratchPad;
	protected QuoteRequestBroker quoteRequestBroker;
	protected boolean requiresRefresh;

	public QuoteRequestInsuredObjectBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT;
		this.service = QuoteRequestObjectService.Util.getInstance();
		this.requestService = QuoteRequestService.Util.getInstance();
		this.searchBroker = new QuoteRequestObjectSearchBroker(this.service);
		this.objectsInScratchPad = new HashMap<String, String>();
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		itemId = getEffectiveId(itemId);
		this.getQuoteRequestObject(itemId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(QuoteRequestObject response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
					((QuoteRequestObjectDataBrokerClient) bc).addQuoteRequestObject(response);
					((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
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
		incrementDataVersion();
		itemId = getEffectiveId(itemId);
		for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
			((QuoteRequestObjectDataBrokerClient) bc).removeQuoteRequestObject(itemId);
			((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		itemId = getEffectiveId(itemId);
		this.getQuoteRequestObject(itemId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(QuoteRequestObject response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
					((QuoteRequestObjectDataBrokerClient) bc).updateQuoteRequestObject(response);
					((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getQuoteRequestObject(String id,
			final ResponseHandler<QuoteRequestObject> handler) {
		id = getEffectiveId(id);
		if(isTemp(id)){
			requestService.getObjectInPad(id, new BigBangAsyncCallback<QuoteRequestObject>() {

				@Override
				public void onResponseSuccess(QuoteRequestObject result) {
					result.id = getFinalMapping(result.id);
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested insured object")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			this.service.getObject(id, new BigBangAsyncCallback<QuoteRequestObject>() {

				@Override
				public void onResponseSuccess(QuoteRequestObject result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested insured object")	
					});
					super.onResponseFailure(caught);
				}

			});
		}
	}

	@Override
	public void createQuoteRequestObject(String ownerId, String type, final ResponseHandler<QuoteRequestObject> handler) {
		if(getQuoteRequestBroker().isTemp(ownerId)){
			ownerId = getQuoteRequestBroker().getEffectiveId(ownerId);
			requestService.createObjectInPad(ownerId, type, new BigBangAsyncCallback<QuoteRequestObject>() { //TODO

				@Override
				public void onResponseSuccess(QuoteRequestObject result) {
					objectsInScratchPad.put(result.id, result.id);
					incrementDataVersion();
					for(DataBrokerClient<QuoteRequestObject> client : clients) {
						((QuoteRequestObjectDataBrokerClient)client).addQuoteRequestObject(result);
						((QuoteRequestObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create new insured object in scratchpad")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Cannot create insured object in policy not in editable mode")	
			});
		}
	}

	@Override
	public void updateQuoteRequestObject(QuoteRequestObject object,
			final ResponseHandler<QuoteRequestObject> handler) {
		String id = getEffectiveId(object.id);
		if(isTemp(id)){
			String tempId = object.id;
			object.id = id;

			requestService.updateObjectInPad(object, new BigBangAsyncCallback<QuoteRequestObject>() {

				@Override
				public void onResponseSuccess(QuoteRequestObject result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<QuoteRequestObject> client : clients) {
						((QuoteRequestObjectDataBrokerClient)client).updateQuoteRequestObject(result);
						((QuoteRequestObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String()	
					});
					super.onResponseFailure(caught);
				}
			});
			object.id = tempId;
		}else{
			handler.onError(new String[]{
					new String("Cannot update insured object in policy not in editable mode")	
			});
		}
	}

	@Override
	public void deleteQuoteRequestObject(String objectId,
			final ResponseHandler<Void> handler) {
		objectId = getEffectiveId(objectId);
		getQuoteRequestObject(objectId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(final QuoteRequestObject response) {
				String ownerId = response.ownerId;
				if(getQuoteRequestBroker().isTemp(ownerId)){
					String tempId = getEffectiveId(response.id);
					requestService.deleteObjectInPad(tempId, new BigBangAsyncCallback<Void>() {

						@Override
						public void onResponseSuccess(Void result) {
							incrementDataVersion();
							for(DataBrokerClient<QuoteRequestObject> client : clients) {
								((QuoteRequestObjectDataBrokerClient)client).removeQuoteRequestObject(response.id);
								((QuoteRequestObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
							}
							handler.onResponse(null);
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							handler.onError(new String[]{
									new String("Could not delete the object")	
							});
							super.onResponseFailure(caught);
						}
					});				
				}else{
					handler.onError(new String[]{
							new String("Cannot delete the object")	
					});
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not find the object")	
				});
			}
		});
	}

	@Override
	public void getProcessQuoteRequestObjects(String ownerId,
			final ResponseHandler<Collection<QuoteRequestObjectStub>> handler) {

		ownerId = getQuoteRequestBroker().getFinalMapping(ownerId);

		QuoteRequestObjectSearchParameter parameter = new QuoteRequestObjectSearchParameter();
		parameter.requestId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter	
		};

		SortParameter sort = new QuoteRequestObjectSortParameter(QuoteRequestObjectSortParameter.SortableField.NAME, SortOrder.ASC);

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		this.getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<QuoteRequestObjectStub>>() {

			@Override
			public void onResponse(Search<QuoteRequestObjectStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the requested process insured objects")	
				});
			}
		});
	}

	private String getEffectiveId(String id){
		id = id.toLowerCase();
		if(objectsInScratchPad.containsKey(id)){
			return objectsInScratchPad.get(id);
		}
		return id;
	}

	private String getFinalMapping(String tempId){
		for(String key : objectsInScratchPad.keySet()){
			if(objectsInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

	private boolean isTemp(String id){
		id = id.toLowerCase();
		return objectsInScratchPad.containsKey(id) || objectsInScratchPad.containsValue(id);
	}

	@Override
	public SearchDataBroker<QuoteRequestObjectStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void remapItemId(String oldId, String newId,
			boolean newIdInScratchPad) {
		oldId = oldId.toLowerCase();
		newId = newId == null ? null : newId.toLowerCase();

		if(newIdInScratchPad){
			objectsInScratchPad.put(oldId, newId);
		}else if(newId == null){
			this.objectsInScratchPad.remove(oldId);
		} else {
			objectsInScratchPad.remove(newId);
		}
	}

	private QuoteRequestBroker getQuoteRequestBroker(){
		if(this.quoteRequestBroker == null) {
			this.quoteRequestBroker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		}
		return this.quoteRequestBroker;
	}

}
