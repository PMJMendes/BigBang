package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;

public class InsuredObjectDataBrokerImpl extends DataBroker<InsuredObject>
		implements InsuredObjectDataBroker {

	public InsuredObjectDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.INSURED_OBJECT;
	}
	
	@Override
	public void incrementDataVersion() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkClientDataVersions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCurrentDataVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerClient(DataBrokerClient<InsuredObject> client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterClient(DataBrokerClient<InsuredObject> client) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<DataBrokerClient<InsuredObject>> getClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataElementId() {
		return this.dataElementId;
	}

	@Override
	public void requireDataRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInsuredObject(String id,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPolicyInsuredObjects(String policyId,
			ResponseHandler<Collection<InsuredObject>> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInsuredObject(InsuredObject object,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteInsuredObject(String id, ResponseHandler<Void> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public SearchDataBroker<InsuredObject> getSearchBroker() {
		// TODO Auto-generated method stub
		return null;
	}

}
