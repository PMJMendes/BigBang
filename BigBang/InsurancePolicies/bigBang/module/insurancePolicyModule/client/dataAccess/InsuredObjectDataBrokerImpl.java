package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;

public class InsuredObjectDataBrokerImpl extends DataBroker<InsuredObject> implements InsuredObjectDataBroker{

	protected PolicyObjectServiceAsync service;
	protected InsurancePolicyServiceAsync policyService;
	protected SearchDataBroker<InsuredObjectStub> searchBroker;


	public InsuredObjectDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_INSURED_OBJECT;
		this.service = PolicyObjectService.Util.getInstance();
		this.searchBroker = new InsuredObjectSearchBroker(this.service);
	}

	@Override
	public void requireDataRefresh() {
		return;	
	}

	@Override
	public void notifyItemCreation(String itemId) {
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
		for(DataBrokerClient<InsuredObject> bc : getClients()){
			((InsuredObjectDataBrokerClient) bc).removeInsuredObject(itemId);
			((InsuredObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS, getCurrentDataVersion());
		}		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
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
	public void createInsuredObject(String ownerId,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInsuredObject(String id,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInsuredObject(InsuredObject object,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteInsuredObject(String objectId,
			ResponseHandler<Void> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getProcessInsuredObjects(String ownerId,
			ResponseHandler<Collection<InsuredObjectStub>> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public SearchDataBroker<InsuredObjectStub> getSearchBroker() {
		return searchBroker;
	}

	@Override
	public void remapItemId(String Id, String newId, boolean newIdInScratchPad) {
		// TODO Auto-generated method stub

	}

}
