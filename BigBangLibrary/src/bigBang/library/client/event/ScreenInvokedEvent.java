package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ScreenInvokedEvent extends GwtEvent<ScreenInvokedEventHandler> {
	
	public static Type<ScreenInvokedEventHandler> TYPE = new Type<ScreenInvokedEventHandler>();
	public static String OPERATION_TYPE_READ = "CRUD-READ";
	
	private String operationId;
	private String processTypeId;
	private String targetId;
	
	public ScreenInvokedEvent(String operationId, String processTypeId, String targetId){
		this.operationId = operationId;
		this.processTypeId = processTypeId;
		this.targetId = targetId;
	}

	@Override
	public Type<ScreenInvokedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScreenInvokedEventHandler handler) {
		handler.onScreenInvoked(this);
	}

	public String getOperationId(){
		return this.operationId;
	}
	
	public String getProcessTypeId(){
		return this.processTypeId;
	}
	
	public String getTargetId(){
		return this.targetId;
	}

}
