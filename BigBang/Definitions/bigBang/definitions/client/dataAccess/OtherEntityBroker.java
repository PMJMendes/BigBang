package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.OtherEntity;

public interface OtherEntityBroker extends DataBrokerInterface<OtherEntity>{
	
	public void getOtherEntities(ResponseHandler<OtherEntity[]> handler);
	
	public void getOtherEntity(String id, ResponseHandler<OtherEntity> handler);
	
	public void createOtherEntity(OtherEntity entity, ResponseHandler<OtherEntity> handler);
	
	public void saveOtherEntity(OtherEntity entity, ResponseHandler<OtherEntity> handler);
	
	public void removeOtherEntity(String entityId, ResponseHandler<Void> handler);

}
