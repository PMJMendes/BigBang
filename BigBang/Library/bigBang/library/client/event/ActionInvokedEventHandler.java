package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ActionInvokedEventHandler<T> extends EventHandler {

	public void onActionInvoked(ActionInvokedEvent<T> action);
	
}
