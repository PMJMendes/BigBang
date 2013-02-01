package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.OtherEntity;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OtherEntityServiceAsync
	extends Service
{
	void getOtherEntities(AsyncCallback<OtherEntity[]> callback);
	void createOtherEntity(OtherEntity entity, AsyncCallback<OtherEntity> callback);
	void saveOtherEntity(OtherEntity entity, AsyncCallback<OtherEntity> callback);
	void deleteOtherEntity(String id, AsyncCallback<Void> callback);
}
