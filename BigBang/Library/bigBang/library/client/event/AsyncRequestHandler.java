package bigBang.library.client.event;

import bigBang.library.client.HasParameters;

import com.google.gwt.event.shared.EventHandler;

public interface AsyncRequestHandler extends EventHandler {
	
	public void onRequest(HasParameters parameters);
	
}
