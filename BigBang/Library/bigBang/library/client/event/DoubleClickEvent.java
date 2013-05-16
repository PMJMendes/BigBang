package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DoubleClickEvent extends GwtEvent<DoubleClickEventHandler> {

	public static Type<DoubleClickEventHandler> TYPE = new Type<DoubleClickEventHandler>();
	
	@Override
	public Type<DoubleClickEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DoubleClickEventHandler handler) {
		handler.onDoubleClick();
	}
}
