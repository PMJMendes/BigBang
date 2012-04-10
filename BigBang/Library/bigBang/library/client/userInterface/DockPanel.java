package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.List;

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
	private List<DockItem> dockItems;
	private ValueChangeHandler<Boolean> valueChangeHandler;
	

	private boolean valueChangeHandlerInitialized = false;

	public DockPanel(){
		this.toolbarPanel  = new HorizontalPanel();
		this.toolbarPanel.setStyleName("dockPanelItemsWrapper");

		this.dockItems = new ArrayList<DockItem>();
		valueChangeHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				DockItem item = (DockItem) event.getSource();
				setValue(item.getRepresentedValue());
			}
		};		

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidth("100%");
		scrollPanel.add(this.toolbarPanel);
		scrollPanel.setAlwaysShowScrollBars(false);
		scrollPanel.setStyleName("dockPanelBackground");

		initWidget(scrollPanel);
	}

	public void addItem(final DockItem item){
		this.toolbarPanel.add(item);
		this.dockItems.add(item);
		item.addValueChangeHandler(this.valueChangeHandler);
	}

	protected void setSelectedItem(DockItem item){
		if(item != null){
			for(DockItem dockItem : this.dockItems) {
				if(dockItem == item) {
					if(!item.isDown()) {
						item.setDown(true);
					}
				}else{
					if(dockItem.isDown()){
						dockItem.setDown(false);
					}
				}
			}
		}else{
			for(DockItem dockItem : this.dockItems) {
				dockItem.setDown(false);
			}
		}
		this.selectedItem = item;
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Object> handler) {

		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Object getValue() {
		return this.selectedItem == null ? null : this.selectedItem.getRepresentedValue();
	}

	public void setValue(Object value) {
		setValue(value, true);
	}

	public void setValue(Object value, boolean fireEvents) {
		if(value != null) {
			for(DockItem item : this.dockItems) {
				if(item.getRepresentedValue().equals(value)){
					setSelectedItem(item);
					break;
				}
			}
		}else{
			setSelectedItem(null);
		}

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

	public Object getSelectedValue(){
		return this.selectedItem == null ? null : selectedItem.getRepresentedValue();
	}
	
}
