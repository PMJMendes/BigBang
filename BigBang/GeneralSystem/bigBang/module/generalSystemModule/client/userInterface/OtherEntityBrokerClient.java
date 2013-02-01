package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.OtherEntity;

public interface OtherEntityBrokerClient extends DataBrokerClient<OtherEntity>{
	
	public void setOtherEntities(OtherEntity[] entities);
	
	public void addOtherEntity(OtherEntity entity);
	
	public void updateOtherEntity(OtherEntity entity);
	
	public void removeOtherEntity(String entityId);

}
