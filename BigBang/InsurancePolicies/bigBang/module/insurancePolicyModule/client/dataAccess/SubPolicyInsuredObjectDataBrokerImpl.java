package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSortParameter;

public class SubPolicyInsuredObjectDataBrokerImpl extends DataBroker<InsuredObject>
implements InsuredObjectDataBroker {

	protected SubPolicyObjectServiceAsync service;
	protected SubPolicyServiceAsync subPolicyService;
	protected SearchDataBroker<InsuredObjectStub> searchBroker;

	protected Map<String, String> objectsInScratchPad;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected boolean requiresRefresh;

	public SubPolicyInsuredObjectDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS;
		this.service = SubPolicyObjectService.Util.getInstance();
		this.subPolicyService = SubPolicyService.Util.getInstance();
		this.searchBroker = new SubPolicyInsuredObjectSearchBroker(this.service);
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
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS, getCurrentDataVersion());
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
			((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS, getCurrentDataVersion());
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
					((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS, getCurrentDataVersion());
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

	@Override
	public SearchDataBroker<InsuredObjectStub> getSearchBroker() {
		return this.searchBroker;
	}

//	private InsuranceSubPolicyBroker getSubPolicyBroker(){
//		if(this.subPolicyBroker == null) {
//			this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
//		}
//		return this.subPolicyBroker;
//	}

}
