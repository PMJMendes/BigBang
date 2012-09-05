package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.InsuredObjectStubOLD;

public interface InsuredObjectDataBroker extends
		DataBrokerInterface<InsuredObjectOLD> {

	public void createInsuredObject(String ownerId, ResponseHandler<InsuredObjectOLD> handler);
	
	public void getInsuredObject(String id, ResponseHandler<InsuredObjectOLD> handler);
	
	public void updateInsuredObject(InsuredObjectOLD object, ResponseHandler<InsuredObjectOLD> handler);
	
	public void deleteInsuredObject(String objectId, ResponseHandler<Void> handler);
	
	public void getProcessInsuredObjects(String ownerId, ResponseHandler<Collection<InsuredObjectStubOLD>> handler);
	
	public SearchDataBroker<InsuredObjectStubOLD> getSearchBroker();
	
	public void remapItemId(String oldId, String newId, boolean newIdInScratchPad);

}
