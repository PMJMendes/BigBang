package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObject;

public interface InsuredObjectDataBroker extends
		DataBrokerInterface<InsuredObject> {

	public void getInsuredObject(String id, ResponseHandler<InsuredObject> handler);
	
	public void getPolicyInsuredObjects(String policyId, ResponseHandler<Collection<InsuredObject>> handler);
	
	public void updateInsuredObject(InsuredObject object, ResponseHandler<InsuredObject> handler);
	
	public void deleteInsuredObject(String id, ResponseHandler<Void> handler);
	
	public SearchDataBroker<InsuredObject> getSearchBroker();
}
