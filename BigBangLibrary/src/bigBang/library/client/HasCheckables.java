package bigBang.library.client;

import java.util.Collection;

import bigBang.library.client.event.CheckedSelectionChangedEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasCheckables {

public void clearSelection();
	
	public Collection<Checkable> getChecked();

	public HandlerRegistration addCheckedSelectionChangedEventHandler(CheckedSelectionChangedEventHandler handler);
	
}
