package bigBang.library.client;

import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BigBangAsyncCallback<T> implements AsyncCallback<T> {
	
	private static EventBus eventBus;
	
	public static void setEventBus(EventBus eventBus) {
		BigBangAsyncCallback.eventBus = eventBus;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		try{
			throw(caught);
		}catch(SessionExpiredException see){
			onSessionExpiredException();
		} catch (Throwable e) {
			GWT.log("Erro: " + e.getMessage());
		}
	}

	@Override
	public abstract void onSuccess(T result);
	
	protected void onSessionExpiredException(){
		eventBus.fireEvent(new SessionExpiredEvent());
	}

}
