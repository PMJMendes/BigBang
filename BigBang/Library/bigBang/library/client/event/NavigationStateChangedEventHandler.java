package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationStateChangedEventHandler extends EventHandler{

	public void onNavigationStateChanged(NavigationStateChangedEvent event);
	
}
