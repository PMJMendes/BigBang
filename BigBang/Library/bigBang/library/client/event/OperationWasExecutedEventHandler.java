package bigBang.library.client.event;
import com.google.gwt.event.shared.EventHandler;


public interface OperationWasExecutedEventHandler extends EventHandler {

	public void onOperationWasExecuted(String operationId, String processId);
	
}
