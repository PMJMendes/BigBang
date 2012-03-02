package bigBang.library.client;

import bigBang.library.client.event.NavigationEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasNavigationHandlers {

	public HandlerRegistration addNavigationHandler(NavigationEventHandler handler);
	
}
