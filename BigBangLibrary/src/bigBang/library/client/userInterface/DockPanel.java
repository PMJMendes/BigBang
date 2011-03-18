package bigBang.library.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class DockPanel extends Composite implements HasValue <Object>, HasValueChangeHandlers<Object> {

	private DockItem selectedItem;
	private HorizontalPanel toolbarPanel;
	
	private boolean valueChangeHandlerInitialized = false;

	public DockPanel(){
		this.toolbarPanel  = new HorizontalPanel();
		this.toolbarPanel.setStyleName("dockPanelItemsWrapper");

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidth("100%");
		scrollPanel.add(this.toolbarPanel);
		scrollPanel.setAlwaysShowScrollBars(false);
		scrollPanel.setStyleName("dockPanelBackground");

		initWidget(scrollPanel);
	}

	public void addItem(final DockItem item){
		this.toolbarPanel.add(item);
		item.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(item == selectedItem){
					item.setDown(true);
					return;
				}
				
				if(event.getValue())
					setSelectedItem(item);					
			}
			
		});
		if(this.selectedItem == null)
			this.setSelectedItem(item);
	}

	public void setSelectedItem(DockItem item){
		if(item.equals(this.selectedItem))
			return;		

		if(this.selectedItem != null) 
			this.selectedItem.setDown(false);

		if(item.getParent().equals(this.toolbarPanel))
			this.selectedItem = item;
		item.setDown(true);
		setValue(item.getRepresentedValue());
	}

	public void addSpecialItem(DockItem item){
		this.toolbarPanel.add(item);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Object> handler) {

		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Object getValue() {
		return this.selectedItem.getRepresentedValue();
	}

	public void setValue(Object value) {
		setValue(value, true);
	}

	public void setValue(Object value, boolean fireEvents) {
		//if(getValue() == value)
		//	return;

		if(fireEvents)
			ValueChangeEvent.fire(this, getValue());
		
		int nItems = toolbarPanel.getWidgetCount();
		for(int i = 0; i < nItems; i++){
			DockItem item = ((DockItem)toolbarPanel.getWidget(i));
			if(item.getRepresentedValue() == value){
				this.setSelectedItem(item);
				return;
			}
		}
		GWT.log("The value does not exist in the DockPanel");
	}

}
