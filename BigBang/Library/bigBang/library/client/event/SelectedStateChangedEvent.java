package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SelectedStateChangedEvent extends GwtEvent<SelectedStateChangedEventHandler> {

	public static Type<SelectedStateChangedEventHandler> TYPE = new Type<SelectedStateChangedEventHandler>();
	private boolean selected;
	
	public SelectedStateChangedEvent(boolean selected) {
		this.selected = selected;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectedStateChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectedStateChangedEventHandler handler) {
		handler.onSelectedStateChanged(this);
	}

}

