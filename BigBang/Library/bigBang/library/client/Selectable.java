package bigBang.library.client;

import bigBang.library.client.event.SelectedStateChangedEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface Selectable {

	public void setSelected(boolean selected);
	
	public void setSelected(boolean selected, boolean fireEvents);
	
	public boolean isSelected();
	
	public HandlerRegistration addSelectedStateChangedEventHandler(SelectedStateChangedEventHandler handler);
	
}
