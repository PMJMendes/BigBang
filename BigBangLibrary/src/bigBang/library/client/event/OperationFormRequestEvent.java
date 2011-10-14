package bigBang.library.client.event;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.HasEditableValue;

import com.google.gwt.event.shared.GwtEvent;

public class OperationFormRequestEvent extends
		GwtEvent<OperationFormRequestEventHandler> {
	
	public static Type<OperationFormRequestEventHandler> TYPE = new Type<OperationFormRequestEventHandler>();
	
	protected String operationId;
	protected ResponseHandler<HasEditableValue<?>> responseHandler;

	public OperationFormRequestEvent(String operationId, ResponseHandler<HasEditableValue<?>> handler){
		this.operationId = operationId;
		this.responseHandler = handler;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OperationFormRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OperationFormRequestEventHandler handler) {
		handler.onOperationFormRequest(operationId, responseHandler);
	}

}
