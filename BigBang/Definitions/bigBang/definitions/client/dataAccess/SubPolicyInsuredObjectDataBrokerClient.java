package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.InsuredObjectOLD;

public interface SubPolicyInsuredObjectDataBrokerClient extends
		DataBrokerClient<InsuredObjectOLD> {

	public void addInsuredObject(InsuredObjectOLD object);
	
	public void updateInsuredObject(InsuredObjectOLD object);
	
	public void removeInsuredObject(String objectId);
	
}
