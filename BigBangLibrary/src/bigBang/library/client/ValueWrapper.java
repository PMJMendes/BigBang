package bigBang.library.client;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class ValueWrapper <T> implements HasValue<T>, HasValueChangeHandlers<T> {

	protected T value;
	protected boolean valueChangeHandlerInitialized;
	//protected Collection<ValueChangeHandler<T>> valueChangeHandlers;
	
	public ValueWrapper(T value){
		setValue(value);
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		fireEvent(event);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		if(!valueChangeHandlerInitialized){
			return addValueChangeHandler(handler);
		}
		return null;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@Override
	public void setValue(T value) {
		setValue(value, true);
		fireChanges();
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		this.value = value;
	}
	
	public void fireChanges(){
		ValueChangeEvent.fire(this, getValue());
	}

}
