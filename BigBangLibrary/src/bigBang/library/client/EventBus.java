package bigBang.library.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;

public class EventBus extends HandlerManager {

	public EventBus() {
		super(null);
	}
	
	public void onBrowserEvent(Event event) {
		GWT.log(event.getString() + "<-");
	}
}
