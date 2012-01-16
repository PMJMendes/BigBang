package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
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
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSortParameter;

public class InsuredObjectDataBrokerImpl extends DataBroker<InsuredObject>
		implements InsuredObjectDataBroker {
	
	protected PolicyObjectServiceAsync service;
	protected SearchDataBroker<InsuredObjectStub> searchBroker;
	protected Collection<String> objectsInScratchPad;
	protected boolean requiresRefresh;
	
	public InsuredObjectDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_INSURED_OBJECT;
		this.service = PolicyObjectService.Util.getInstance();
		this.searchBroker = new InsuredObjectSearchBroker(this.service);
		this.objectsInScratchPad = new ArrayList<String>();
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getInsuredObject(itemId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsuredObject> bc : getClients()){
					((InsuredObjectDataBrokerClient) bc).addInsuredObject(response);
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<InsuredObject> bc : getClients()){
			((InsuredObjectDataBrokerClient) bc).removeInsuredObject(itemId);
			((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getInsuredObject(itemId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsuredObject> bc : getClients()){
					((InsuredObjectDataBrokerClient) bc).updateInsuredObject(response);
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}

	@Override
	public void getInsuredObject(String id,
			final ResponseHandler<InsuredObject> handler) {
		this.service.getObject(id, new BigBangAsyncCallback<InsuredObject>() {

			@Override
			public void onSuccess(InsuredObject result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void getProcessInsuredObjects(String ownerId,
			final ResponseHandler<Collection<InsuredObjectStub>> handler) {
		
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
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public SearchDataBroker<InsuredObjectStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void remapItemId(String oldId, String newId,
			boolean newIdInScratchPad) {
		InsuredObject object = (InsuredObject) this.cache.get(oldId);
		if(object != null) {
			cache.remove(oldId);
			object.id = newId;
			cache.add(newId, object);
		}
		for(String s : this.objectsInScratchPad){
			if(s.equalsIgnoreCase(oldId)){
				objectsInScratchPad.remove(s);
				objectsInScratchPad.add(newId);
				break;
			}
		}
		incrementDataVersion();
		for(DataBrokerClient<InsuredObject> bc : getClients()){
			((InsuredObjectDataBrokerClient) bc).remapItemId(oldId, newId);
			((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT, getCurrentDataVersion());
		}
	}

}
