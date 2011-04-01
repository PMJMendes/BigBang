package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface SelectedStateChangedEventHandler extends EventHandler {

	public void onSelectedStateChanged(SelectedStateChangedEvent event);
	
}
