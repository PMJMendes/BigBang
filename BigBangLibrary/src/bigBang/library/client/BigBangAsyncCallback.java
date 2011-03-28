package bigBang.library.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BigBangAsyncCallback<T> implements AsyncCallback<T> {
	
	private EventBus eventBus;
	
	public BigBangAsyncCallback(EventBus eventBus){
		this.eventBus = eventBus;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		onSessionExpiredException();
	}

	@Override
	public abstract void onSuccess(T result);
	
	protected void onSessionExpiredException(){
		GWT.log("SESSION EXPIRED");
	}

}
