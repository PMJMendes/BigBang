package bigBang.library.shared.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public class SearchPreviewListEntry extends Composite implements HasValue <Object>, HasClickHandlers {

	private Object value;
	private Panel panel;
	private boolean valueChangeHandlerInitialized;	

	public SearchPreviewListEntry(){
		this.panel = new HorizontalPanel();
		this.panel.setSize("100%", "100%");

		initWidget(this.panel);
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Object> handler) {
		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.setValue(value, true);
	}

	public void setValue(Object value, boolean fireEvents) {
		this.value = value;

		if(fireEvents && valueChangeHandlerInitialized)
			ValueChangeEvent.fire(this, getValue());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.panel.addDomHandler(handler, ClickEvent.getType());
	}

}
