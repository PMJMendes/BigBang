package bigBang.library.shared.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchPanelViewResult extends Composite implements HasClickHandlers, HasValue<Object> {
	
	private final int HEIGHT = 20; //px
	
	protected HorizontalPanel panel;
	protected CheckBox checkBox;
	protected SimplePanel clickableContainer;
	private boolean checkable = true;
	private boolean valueChangeHandlerInitialized = false;
		
	private Object value; //The object being represented by this result
	
	public SearchPanelViewResult(Object o){
		this();
		this.setValue(o);
	}
	
	public SearchPanelViewResult(){
		this.panel = new HorizontalPanel();
		this.panel.setSize("100%", HEIGHT + "px");
		this.panel.setStyleName("searchFormResult");
		
		this.checkBox = new CheckBox();
		setCheckable(this.checkable);
		this.panel.add(checkBox);
		
		this.clickableContainer = new SimplePanel();
		this.clickableContainer.setSize("100%", "100%");
		this.clickableContainer.setStyleName("teste");
		this.clickableContainer.getElement().getStyle().setProperty("cursor", "pointer");
		this.panel.add(clickableContainer);
		this.panel.setCellWidth(clickableContainer, "100%");
		this.panel.setCellHeight(clickableContainer, "100%");
		
		initWidget(this.panel);
	}
	
	public void setCheckable(boolean checkable){
		this.checkable = checkable;
		this.checkBox.setValue(false);
		this.checkBox.setVisible(checkable);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return clickableContainer.addDomHandler(handler, ClickEvent.getType());
	}
	
	public void setClickableContent(Widget w){
		this.clickableContainer.clear();
		this.clickableContainer.add(w);
	}
	
	public void setValue(Object o) {
		this.setValue(o, true);
	}
	
	public void setValue(Object value, boolean fireEvents) {
		this.value = value;
		
		if(fireEvents && valueChangeHandlerInitialized)
			ValueChangeEvent.fire(this, getValue());
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Object> handler) {
		
		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Object getValue() {
		return this.value;
	}
	
}
