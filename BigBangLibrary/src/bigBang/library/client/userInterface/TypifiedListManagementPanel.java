package bigBang.library.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.BigBangTypifiedListBroker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TypifiedListManagementPanel extends FilterableList<TipifiedListItem> implements TypifiedListClient {

	public static class TypifiedListEntry extends ListEntry<TipifiedListItem> {

		public Image deleteButton;

		public TypifiedListEntry(TipifiedListItem value) {
			super(value);
			if(deleteButton == null)
				deleteButton = new Image();
			deleteButton.getElement().getStyle().setCursor(Cursor.POINTER);
			deleteButton.setTitle("Apagar");
			setRightWidget(deleteButton);
			deleteButton.setVisible(false);
		}

		@Override
		public <I extends Object> void setInfo(I info) {
			TipifiedListItem item = (TipifiedListItem) info;
			setTitle(item.value);
		};

		@Override
		public void setSelected(boolean selected, boolean fireEvents) {
			super.setSelected(selected, fireEvents);
			Resources r = GWT.create(Resources.class);
			if(deleteButton == null)
				deleteButton = new Image();
			deleteButton.setResource(isSelected() ? r.listDeleteIconSmallWhite() : r.listDeleteIconSmallBlack());
		}

	}

	private String listId;
	private String selectedValueId;
	private TextBox valueTextBox;
	private boolean editModeEnabled, editable;
	private int typifiedListDataVersion;
	protected TypifiedListBroker listBroker;

	//UI
	private Button editButton;
	private HorizontalPanel toolBar;

	public TypifiedListManagementPanel(final String listId, String listName){
		super();
		this.listBroker = BigBangTypifiedListBroker.Util.getInstance();
		this.listId = listId;
		editable = listId != null && !listId.equals("");
		this.setSize("300px", "400px");

		this.editButton = new Button();
		this.editButton.setSize("80px", "27px");

		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader();
		header.setText(listName);
		header.setRightWidget(editButton);
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setEditModeEnabled(!isEditModeEnabled());
			}
		});

		headerWrapper.add(header);

		toolBar = new HorizontalPanel();
		toolBar.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		toolBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolBar.setSpacing(5);
		toolBar.setSize("100%", "30px");

		valueTextBox = new TextBox();
		valueTextBox.setWidth("100%");

		final Button addButton = new Button("Criar");
		addButton.setWidth("80px");

		toolBar.add(valueTextBox);
		toolBar.add(addButton);
		toolBar.setCellWidth(valueTextBox, "100%");

		headerWrapper.add(toolBar);

		setHeaderWidget(headerWrapper);

		showFilterField(false);
		
		addButton.setEnabled(false);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNew();
			}
		});
		valueTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				addButton.setEnabled(valueTextBox.getValue().length() > 0);
			}
		});
		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached())
					getElement().getStyle().setBackgroundColor("white");
			}
		});

		setEditModeEnabled(false);
		this.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					listBroker.registerClient(listId, TypifiedListManagementPanel.this);
					listBroker.getListItems(listId);
				}else{
					listBroker.unregisterClient(listId, TypifiedListManagementPanel.this);
				}
			}
		});
		
		showFilterField(false);
	}

	private void addNew(){	
		String value = valueTextBox.getValue();
		TipifiedListItem item = new TipifiedListItem();
		item.value = value;
		
		this.listBroker.createListItem(this.listId, item, new ResponseHandler<TipifiedListItem>() {
			
			@Override
			public void onResponse(TipifiedListItem response) {
				for(ListEntry<TipifiedListItem> e : TypifiedListManagementPanel.this){
					if(e.getValue() == response){
						e.setSelected(true);
						break;
					}
				}
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				GWT.log("Recebi erro");
			}
		});
	}

	private void deleteEntry(final TypifiedListEntry e){
		this.listBroker.removeListItem(this.listId, e.getValue().id, new ResponseHandler<TipifiedListItem>() {

			@Override
			public void onResponse(TipifiedListItem response) {
				remove(e);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void setEditModeEnabled(boolean enabled){
		this.editModeEnabled = enabled;
		this.editButton.setText(enabled ? "Cancelar" : "Editar");
		showSearchField(!enabled);
		this.toolBar.setVisible(enabled);
		for(ListEntry<TipifiedListItem> e : entries) {
			((TypifiedListEntry) e).deleteButton.setVisible(enabled);
		}
	}

	private boolean isEditModeEnabled(){
		return this.editModeEnabled;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		this.editButton.setVisible(editable);
	}
	
	public void setReadOnly(boolean readonly) {
		this.setEditModeEnabled(false);
		this.editButton.setVisible(!readonly && editable);
	}

	
	//TypifiedListClient methods
	
	@Override
	protected HandlerRegistration bindEntry(ListEntry<TipifiedListItem> e) {
		e.setDoubleClickable(true);
		return super.bindEntry(e);
	}
	
	public void onChanged(){
		
	}

	@Override
	public int getTypifiedDataVersionNumber() {
		return typifiedListDataVersion;
	}

	@Override
	public void setTypifiedDataVersionNumber(int number) {
		this.typifiedListDataVersion = number;
	}

	@Override
	public void setTypifiedListItems(java.util.List<TipifiedListItem> items) {
		Collection<ValueSelectable<TipifiedListItem>> selected = getSelected();
		clear();
		for(int i = 0; i < items.size(); i++) {
			TypifiedListEntry entry = addEntry(items.get(i));
			for(ValueSelectable<TipifiedListItem> s : selected){
				if(entry.getValue().id.equals(s.getValue().id)){
					entry.setSelected(true);
					break;
				}
			}
		}
		setEditModeEnabled(isEditModeEnabled());
		onChanged();	
	}
	
	protected TypifiedListEntry addEntry(TipifiedListItem item){
		final TypifiedListEntry entry = new TypifiedListEntry(item);
		entry.deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteEntry(entry);
			}
		});
		add(entry);
		if(this.selectedValueId != null && this.selectedValueId.equalsIgnoreCase(item.id))
			entry.setSelected(true);
		setEditModeEnabled(this.editModeEnabled);
		return entry;
	}
	
	
	//TypifiedListClient methods

	@Override
	public void removeItem(TipifiedListItem item) {
		for(ValueSelectable<TipifiedListItem> i : this) {
			if(i.getValue() == item){
				this.remove(i);
				break;
			}
		}
	}

	@Override
	public void addItem(TipifiedListItem item) {
		addEntry(item);
	}

	@Override
	public void updateItem(TipifiedListItem item) {
		for(ValueSelectable<TipifiedListItem> i : this) {
			if(i.getValue().id == item.id){
				i.setValue(item);
				((TypifiedListEntry)i).setInfo(item);
				break;
			}
		}
	}

}
