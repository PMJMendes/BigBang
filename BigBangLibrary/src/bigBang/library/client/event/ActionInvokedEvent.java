package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ActionInvokedEvent<T> extends GwtEvent<ActionInvokedEventHandler<T>> {

	public Type<ActionInvokedEventHandler<T>> TYPE = new Type<ActionInvokedEventHandler<T>>();
	
	protected T action;
	
	public ActionInvokedEvent(T action){
		this.action = action;
	}
	
	public T getAction(){
		return this.action;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ActionInvokedEventHandler<T>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionInvokedEventHandler<T> handler) {
		handler.onActionInvoked(this);
	}

}
