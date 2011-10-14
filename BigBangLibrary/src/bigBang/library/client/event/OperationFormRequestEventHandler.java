package bigBang.library.client.event;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.HasEditableValue;

import com.google.gwt.event.shared.EventHandler;

public interface OperationFormRequestEventHandler extends EventHandler {

	public void onOperationFormRequest(String operationId, ResponseHandler<HasEditableValue<?>> handler);
	
}
