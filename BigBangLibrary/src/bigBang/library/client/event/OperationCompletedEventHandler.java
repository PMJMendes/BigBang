package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface OperationCompletedEventHandler extends EventHandler {

	public void onOperationCompleted(OperationCompletedEvent event);

}
