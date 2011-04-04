package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.Mediator;

public interface MediatorServiceAsync
	extends Service
{
	void createMediator(Mediator mediator, AsyncCallback<Mediator> callback);
	void deleteMediator(String id, AsyncCallback<Void> callback);
	void getMediators(AsyncCallback<Mediator[]> callback);
	void saveMediator(Mediator mediator, AsyncCallback<Mediator> callback);
}
