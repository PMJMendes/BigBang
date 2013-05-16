package bigBang.library.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;

public interface HasNavigationStateChangedHandlers {
	
	HandlerRegistration registerNavigationStateChangedHandler(NavigationStateChangedEventHandler handler);

}
