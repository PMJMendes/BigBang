package bigBang.library.client;

import bigBang.library.client.event.CheckedStateChangedEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface Checkable {

	public void setChecked(boolean selected);
	
	public void setChecked(boolean selected, boolean fireEvents);
	
	public boolean isChecked();
	
	public HandlerRegistration addCheckedStateChangedEventHandler(CheckedStateChangedEventHandler handler);
	
}
