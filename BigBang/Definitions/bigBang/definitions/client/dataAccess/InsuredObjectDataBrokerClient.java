package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.InsuredObjectOLD;

public interface InsuredObjectDataBrokerClient extends
		DataBrokerClient<InsuredObjectOLD> {

	public void addInsuredObject(InsuredObjectOLD object);
	
	public void updateInsuredObject(InsuredObjectOLD object);
	
	public void removeInsuredObject(String id);
	
	public void remapItemId(String newId, String oldId);
	
}
