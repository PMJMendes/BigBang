package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface OperationInvokedEventHandler extends EventHandler {
	public void onOperationInvoked(OperationInvokedEvent event);
}
