package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface CheckedStateChangedEventHandler extends EventHandler {

	void onCheckedStateChanged(CheckedStateChangedEvent event); 
	
}
