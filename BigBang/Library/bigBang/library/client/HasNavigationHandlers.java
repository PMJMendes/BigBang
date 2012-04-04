package bigBang.library.client;

import bigBang.library.client.event.NavigationRequestEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasNavigationHandlers {

	public HandlerRegistration addNavigationHandler(NavigationRequestEventHandler handler);
	
}
