package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.client.types.Mediator;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MediatorServiceAsync
	extends Service
{
	void createMediator(Mediator mediator, AsyncCallback<Mediator> callback);
	void deleteMediator(String id, AsyncCallback<Void> callback);
	void getMediators(AsyncCallback<Mediator[]> callback);
	void saveMediator(Mediator mediator, AsyncCallback<Mediator> callback);
}
