package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.InsuredObject;

public interface InsuredObjectDataBrokerClient extends
		DataBrokerClient<InsuredObject> {

	public void addInsuredObject(InsuredObject object);
	
	public void updateInsuredObject(InsuredObject object);
	
	public void removeInsuredObject(String id);
	
}
