package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class OperationWasExecutedEvent extends GwtEvent<OperationWasExecutedEventHandler> {
	
	public static Type<OperationWasExecutedEventHandler> TYPE = new Type<OperationWasExecutedEventHandler>();

	protected String operationId, objectId;
	
	public OperationWasExecutedEvent(String operationId, String objectId){
		this.operationId = operationId;
		this.objectId = objectId;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OperationWasExecutedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OperationWasExecutedEventHandler handler) {
		handler.onOperationWasExecuted(this.operationId, this.objectId);
	}

}
