package bigBang.library.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;

public class EventBus extends HandlerManager {

	public static class Util {
		private static EventBus instance;
		
		public static EventBus getInstance(){
			if(instance == null){
				instance = new EventBus();
			}
			return instance;
		}
	}
	
	private EventBus() {
		super(null);
	}
	
	public void onBrowserEvent(Event event) {
		GWT.log(event.getString() + "<-");
	}
	
	public static EventBus getInstance(){
		return EventBus.Util.getInstance();
	}
}
