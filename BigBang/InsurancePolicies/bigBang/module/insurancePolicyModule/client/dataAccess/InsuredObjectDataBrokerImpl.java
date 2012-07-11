package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSortParameter;

public class InsuredObjectDataBrokerImpl extends DataBroker<InsuredObject>
implements InsuredObjectDataBroker {

	protected PolicyObjectServiceAsync service;
	protected InsurancePolicyServiceAsync policyService;
	protected SearchDataBroker<InsuredObjectStub> searchBroker;

	protected Map<String, String> objectsInScratchPad;
	protected InsurancePolicyBroker insurancePolicyBroker;
	protected boolean requiresRefresh;

	public InsuredObjectDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_INSURED_OBJECT;
		this.service = PolicyObjectService.Util.getInstance();
		this.policyService = InsurancePolicyService.Util.getInstance();
		this.searchBroker = new InsuredObjectSearchBroker(this.service);
		this.objectsInScratchPad = new HashMap<String, String>();
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		itemId = getEffectiveId(itemId);
		this.getInsuredObject(itemId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				incrementDataVersion();
				for(DataBrokerClient<InsuredObject> bc : getClients()){
					((InsuredObjectDataBrokerClient) bc).addInsuredObject(response);
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
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
		for(DataBrokerClient<InsuredObject> bc : getClients()){
			((InsuredObjectDataBrokerClient) bc).removeInsuredObject(itemId);
			((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		itemId = getEffectiveId(itemId);
		this.getInsuredObject(itemId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				incrementDataVersion();
				for(DataBrokerClient<InsuredObject> bc : getClients()){
					((InsuredObjectDataBrokerClient) bc).updateInsuredObject(response);
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getInsuredObject(String id,
			final ResponseHandler<InsuredObject> handler) {
		id = getEffectiveId(id);
		if(isTemp(id)){
			policyService.getObjectInPad(id, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
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
			this.service.getObject(id, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
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
	public void createInsuredObject(String ownerId, final ResponseHandler<InsuredObject> handler) {
		if(getInsurancePolicyBroker().isTemp(ownerId)){
			ownerId = getInsurancePolicyBroker().getEffectiveId(ownerId);
			policyService.createObjectInPad(ownerId, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
					objectsInScratchPad.put(result.id, result.id);
					incrementDataVersion();
					for(DataBrokerClient<InsuredObject> client : clients) {
						((InsuredObjectDataBrokerClient)client).addInsuredObject(result);
						((InsuredObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
	public void updateInsuredObject(InsuredObject object,
			final ResponseHandler<InsuredObject> handler) {
		String id = getEffectiveId(object.id);
		if(isTemp(id)){
			String tempId = object.id;
			object.id = id;

			policyService.updateObjectInPad(object, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<InsuredObject> client : clients) {
						((InsuredObjectDataBrokerClient)client).updateInsuredObject(result);
						((InsuredObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
	public void deleteInsuredObject(String objectId,
			final ResponseHandler<Void> handler) {
		objectId = getEffectiveId(objectId);
		getInsuredObject(objectId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(final InsuredObject response) {
				String ownerId = response.ownerId;
				if(getInsurancePolicyBroker().isTemp(ownerId)){
					String tempId = getEffectiveId(response.id);
					policyService.deleteObjectInPad(tempId, new BigBangAsyncCallback<Void>() {

						@Override
						public void onResponseSuccess(Void result) {
							incrementDataVersion();
							for(DataBrokerClient<InsuredObject> client : clients) {
								((InsuredObjectDataBrokerClient)client).removeInsuredObject(response.id);
								((InsuredObjectDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
	public void getProcessInsuredObjects(String ownerId,
			final ResponseHandler<Collection<InsuredObjectStub>> handler) {

		ownerId = getInsurancePolicyBroker().getFinalMapping(ownerId);

		InsuredObjectSearchParameter parameter = new InsuredObjectSearchParameter();
		parameter.policyId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter	
		};

		SortParameter sort = new InsuredObjectSortParameter(InsuredObjectSortParameter.SortableField.NAME, SortOrder.ASC);

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		this.getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<InsuredObjectStub>>() {

			@Override
			public void onResponse(Search<InsuredObjectStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the resquested process insured objects")	
				});
			}
		}, true);
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
	public SearchDataBroker<InsuredObjectStub> getSearchBroker() {
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

	private InsurancePolicyBroker getInsurancePolicyBroker(){
		if(this.insurancePolicyBroker == null) {
			this.insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		}
		return this.insurancePolicyBroker;
	}

}
