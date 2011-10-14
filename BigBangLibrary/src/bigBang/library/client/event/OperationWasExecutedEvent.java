package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class OperationWasExecutedEvent extends GwtEvent<OperationWasExecutedEventHandler> {
	
	public static Type<OperationWasExecutedEventHandler> TYPE = new Type<OperationWasExecutedEventHandler>();

	protected String operationId, processId;
	
	public OperationWasExecutedEvent(String operationId, String processId){
		this.operationId = operationId;
		this.processId = processId;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OperationWasExecutedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OperationWasExecutedEventHandler handler) {
		handler.onOperationWasExecuted(this.operationId, this.processId);
	}

}
