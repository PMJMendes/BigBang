package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;

public interface InsuredObjectDataBroker extends
		DataBrokerInterface<InsuredObject> {

	public void createInsuredObject(String ownerId, ResponseHandler<InsuredObject> handler);
	
	public void getInsuredObject(String id, ResponseHandler<InsuredObject> handler);
	
	public void updateInsuredObject(InsuredObject object, ResponseHandler<InsuredObject> handler);
	
	public void deleteInsuredObject(String objectId, ResponseHandler<Void> handler);
	
	public void getProcessInsuredObjects(String ownerId, ResponseHandler<Collection<InsuredObjectStub>> handler);
	
	public SearchDataBroker<InsuredObjectStub> getSearchBroker();
	
	public void remapItemId(String Id, String newId, boolean newIdInScratchPad);

}
