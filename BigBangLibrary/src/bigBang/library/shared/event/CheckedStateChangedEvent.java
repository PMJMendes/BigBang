package bigBang.library.shared.event;

import com.google.gwt.event.shared.GwtEvent;

public class CheckedStateChangedEvent extends GwtEvent<CheckedStateChangedEventHandler> {

	public static Type<CheckedStateChangedEventHandler> TYPE = new Type<CheckedStateChangedEventHandler>();
	private boolean checked;
	
	public CheckedStateChangedEvent(boolean checked) {
		this.checked = checked;
	}
	
	public boolean getChecked(){
		return checked;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CheckedStateChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CheckedStateChangedEventHandler handler) {
		handler.onCheckedStateChanged(this);
	}

}
