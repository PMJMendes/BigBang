package bigBang.library.client;

import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BigBangAsyncCallback<T> implements AsyncCallback<T> {

	private static int heldInstances = 0;
	
	public BigBangAsyncCallback(){
		hold();
	}

	@Override
	public void onSuccess(T result) {
		release();
		onResponseSuccess(result);
	};

	@Override
	public void onFailure(Throwable caught) {
		release();
		if ( caught instanceof SessionExpiredException )
			onSessionExpiredException();
		else
			onResponseFailure(caught);
	}

	public void onResponseFailure(Throwable caught) {
		GWT.log("Erro: " + caught.getMessage());
	}

	public abstract void onResponseSuccess(T result);

	protected void onSessionExpiredException(){
		EventBus.getInstance().fireEvent(new SessionExpiredEvent());
	}

	protected void hold(){
		heldInstances++;
		if(heldInstances == 1) {
			setWait();
		}
	}
	
	protected void release(){
		heldInstances--;
		if(heldInstances == 0){
			setNormal();
		}
	}
	
	public native void setWait() /*-{
		var styleElement = $doc.getElementById('styles_js');

		if (styleElement == null) {
			styleElement = $doc.createElement('style');
			styleElement.type = 'text/css';
			styleElement.id = 'styles_js';
			styleElement.appendChild($doc.createTextNode("* {cursor: wait !important;}"));
			$doc.getElementsByTagName('head')[0].appendChild(styleElement);
		}
	}-*/;

	public native void setNormal() /*-{
		var styleElement = $doc.getElementById('styles_js');

		if(styleElement != null) {
			$doc.getElementsByTagName('head')[0].removeChild(styleElement);
		}
	}-*/;

}
