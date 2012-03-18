package bigBang.library.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListBroker;

public class TypifiedListSelectionPanel extends
		ExpandableSelectionFormFieldPanel {

	protected class Entry extends ListEntry<TipifiedListItem> {

		public Entry(TipifiedListItem value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			TipifiedListItem item = (TipifiedListItem) info;
			setTitle(item.value == null ? "-" : item.value);
		};
	}
	
	protected FilterableList<TipifiedListItem> list;
	protected String listId;
	protected TypifiedListBroker broker;
	protected String value;
	protected ListHeader header;
	protected Button confirmButton, cancelButton;
	protected boolean readOnly = false;
	
	public TypifiedListSelectionPanel(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		wrapper.setSize("100%", "100%");

		header = new ListHeader();
		wrapper.add(header);
		
		ClickHandler clickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == confirmButton) {
					Collection<ValueSelectable<TipifiedListItem>> selected = list.getSelected();
					if(selected == null || selected.isEmpty()){
						setValue(null, true);
					}else{
						for(ValueSelectable<TipifiedListItem> entry : selected) {
							setValue(entry.getValue().id, true);
							break;
						}
					}
				}else if(event.getSource() == cancelButton) {
					setValue("CANCELLED_SELECTION", true);
				}
			}
		};
		
		confirmButton = new Button("Confirmar", clickHandler);
		cancelButton = new Button("Cancelar", clickHandler);
		
		HorizontalPanel buttonWrapper = new HorizontalPanel();
		buttonWrapper.setSpacing(5);
		buttonWrapper.add(confirmButton);
		buttonWrapper.add(cancelButton);
		header.setRightWidget(buttonWrapper);
		
		list = new FilterableList<TipifiedListItem>();
		list.showFilterField(false);
		wrapper.add(list);
		wrapper.setCellHeight(list, "100%");

		this.broker = BigBangTypifiedListBroker.Util.getInstance();
		setSize("300px", "400px");
	}
	
	@Override
	public void setListId(String listId) {
		this.listId = listId;
		if(this.listId != null){
			this.broker.getListItems(listId, new ResponseHandler<Collection<TipifiedListItem>>() {
				
				@Override
				public void onResponse(Collection<TipifiedListItem> response) {
					setListItems(response);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					list.clear();
				}
			});
		}
	}
	
	protected void setListItems(Collection<TipifiedListItem> items) {
		list.clear();
		for(TipifiedListItem item : items) {
			addItemEntry(item);
		}
	}
	
	protected void addItemEntry(TipifiedListItem item){
		ListEntry<TipifiedListItem> entry = new Entry(item);
		this.list.add(entry);
		if(value != null && item.id.equalsIgnoreCase(value)){
			entry.setSelected(true, true);
		}
	}
	
	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
		for(ValueSelectable<TipifiedListItem> selected : list) {
			TipifiedListItem item = selected.getValue();
			if(item.id != null && value != null && value.equalsIgnoreCase(item.id) && !selected.isSelected()){
				selected.setSelected(true, false);
			}
		}
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		this.list.setSelectableEntries(!readOnly);
	}

	@Override
	public boolean isReadOnly() {
		return this.readOnly;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
}
