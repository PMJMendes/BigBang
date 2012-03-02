package bigBang.library.client;

import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class BigBangAsyncCallback<T> implements AsyncCallback<T> {
	
	public BigBangAsyncCallback(){
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", "wait"); 
	}
	
	@Override
	public void onSuccess(T result) {
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", ""); 
		onResponseSuccess(result);
	};
	
	@Override
	public void onFailure(Throwable caught) {
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", ""); 
		onResponseFailure(caught);
	}
	
	public void onResponseFailure(Throwable caught) {
		try{
			throw(caught);
		}catch(SessionExpiredException see){
			onSessionExpiredException();
		} catch (Throwable e) {
			GWT.log("Erro: " + e.getMessage());
		}
	}

	public abstract void onResponseSuccess(T result);
	
	protected void onSessionExpiredException(){
		EventBus.getInstance().fireEvent(new SessionExpiredEvent());
	}

}
