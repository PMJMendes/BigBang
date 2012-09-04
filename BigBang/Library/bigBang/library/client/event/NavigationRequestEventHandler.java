package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationRequestEventHandler extends EventHandler {
	public void onNavigationEvent(NavigationRequestEvent event);
}