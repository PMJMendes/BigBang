package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AsyncRequestHandler extends EventHandler {
	
	public void onRequest(AsyncRequest<Object> request);
	
}
