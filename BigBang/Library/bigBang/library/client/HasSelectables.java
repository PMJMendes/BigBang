package bigBang.library.client;

import java.util.Collection;

import bigBang.library.client.event.SelectionChangedEventHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasSelectables <T extends Selectable> {

	public Collection<T> getSelected();
	
	public Collection<T> getAll();
	
	/*public void setSelected(Collection<T> selectables, boolean selected);
	
	public void setSelected(Collection<T> selectables, boolean selected, boolean fireEvents);
	
	public void setSelected(T selectable, boolean selected);
	
	public void setSelected(T selectable, boolean selected, boolean fireEvents);*/
	
	public void clearSelection();
	
	public void setMultipleSelectability(boolean multi);
	
	public boolean isMultipleSelectabilityActive();
	
	public HandlerRegistration addSelectionChangedEventHandler(SelectionChangedEventHandler handler);
	
}
