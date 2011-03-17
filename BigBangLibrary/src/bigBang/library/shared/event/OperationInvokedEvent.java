package bigBang.library.shared.event;

import bigBang.library.shared.userInterface.presenter.ViewPresenterManager;

import com.google.gwt.event.shared.GwtEvent;

public class OperationInvokedEvent extends GwtEvent<OperationInvokedEventHandler> {

	public static Type<OperationInvokedEventHandler> TYPE = new Type<OperationInvokedEventHandler>();
	private String operationId;
	private String operationInstanceId;
	private ViewPresenterManager manager;
	
	public OperationInvokedEvent(String operationId, String operationInstanceId, ViewPresenterManager manager) {
		this.operationId = operationId;
		this.operationInstanceId = operationInstanceId;
		this.manager = manager;
	}
	
	public OperationInvokedEvent(String operationId, String operationInstanceId) {
		this.operationId = operationId;
		this.operationInstanceId = operationInstanceId;
	}
	
	public String getOperationId(){
		return this.operationId;
	}
	
	public String getOperationInstanceId(){
		return this.operationInstanceId;
	}
	
	public ViewPresenterManager getViewPresenterManager() {
		return this.manager;
	}
	
	//If no container if defined, it is assumed that this is a call for the operation screen
	//otherwise, it is a call for the operation view only 
	public boolean goToScreen(){
		return this.manager == null;
	}
	
	@Override
	public Type<OperationInvokedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OperationInvokedEventHandler handler) {
		handler.onOperationInvoked(this);
	}

}