package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.BigBangTypifiedListBroker;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.PopupPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ExpandableListBoxFormField extends ListBoxFormField implements
TypifiedListClient {

	protected Image expandImage;
	protected TypifiedListManagementPanel list;
	protected String selectedValueId;
	protected int typifiedListDataVersion;
	protected TypifiedListBroker typifiedListBroker;
	protected boolean locked = false;
	protected ResponseHandler<Void> expectingResponseHandler = null;

	protected boolean hasServices;

	public ExpandableListBoxFormField(String description) {
		this("", description);
	}

	public ExpandableListBoxFormField(String listId,
			FieldValidator<String> validator) {
		this(listId, "");
		setValidator(validator);
	}

	public ExpandableListBoxFormField(String listId, String description,
			FieldValidator<String> validator) {
		this(listId, description);
		setValidator(validator);
	}

	public ExpandableListBoxFormField(final String listId,
			final String listDescription) {
		super();
		this.typifiedListBroker = BigBangTypifiedListBroker.Util.getInstance();
		clearValues();

		label.setText(listDescription);

		wrapper.clear();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		wrapper.add((Widget) this.listBox);
		Resources r = GWT.create(Resources.class);
		expandImage = new Image(r.listExpandIcon());
		expandImage.getElement().getStyle().setCursor(Cursor.POINTER);

		final PopupPanel popup = new PopupPanel(true, "Listagem");

		list = new TypifiedListManagementPanel(listId, listDescription) {

			public void onCellDoubleClicked(
					bigBang.library.client.userInterface.ListEntry<TipifiedListItem> entry) {
				super.onCellDoubleClicked(entry);
				popup.hidePopup();
			};
		};

		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				for (Selectable i : selected) {
					@SuppressWarnings("unchecked")
					ValueSelectable<TipifiedListItem> iv = (ValueSelectable<TipifiedListItem>) i;
					if ((iv != null && iv.getValue() != null) && (!getValue().equals(iv.getValue().id))){
						setValue(iv.getValue().id);
					}
					break;
				}

			}
		});

		expandImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.clear();
				popup.add(list);
				list.clearSelection();
				for(ListEntry<TipifiedListItem> e : list) {
					if(e.getValue().id.equals(ExpandableListBoxFormField.this.getValue())){
						e.setSelected(true);
						break;
					}
				}
				list.clearFilters();
				list.setEditModeEnabled(false);
				popup.center();
			}
		});

		wrapper.add(expandImage);
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		setFieldWidth("150px");

		setListId(listId, null);
	}

	public void setListId(final String listId, ResponseHandler<Void> listReadyHandler){
		hasServices = listId != null && !listId.equals("");
		String currentListId = this.list.getListId();
		if(hasServices){
			if(currentListId == null || !listId.equalsIgnoreCase(currentListId)){
				this.list.setListId(listId);
				list.setReadOnly(!this.hasServices);
				list.setEditable(this.hasServices);

				if(this.isAttached()){
					this.expectingResponseHandler = listReadyHandler;
					typifiedListBroker.registerClient(listId, this);
				}else{
					this.typifiedListBroker.unregisterClient(listId, this);
				}
			}
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		String listId = this.getListId();
		if(!typifiedListBroker.isClientRegistered(listId, ExpandableListBoxFormField.this)){
			typifiedListBroker.registerClient(listId, ExpandableListBoxFormField.this);
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		String listId = this.getListId();
		if(typifiedListBroker.isClientRegistered(listId, ExpandableListBoxFormField.this)){
			typifiedListBroker.unregisterClient(listId, ExpandableListBoxFormField.this);
		}
	}

	public String getListId() {
		return this.list.getListId();
	}

	public void setEditable(boolean editable) {
		this.list.setEditable(editable);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(!locked){
			super.setReadOnly(readonly);
			list.setReadOnly(readonly);
			list.setSelectableEntries(!readonly);
		}
	}

	public void lock(boolean locked){
		if(locked){
			setReadOnly(true);
		}
		this.locked = locked;
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		String strValue = value;
		if (value == null)
			strValue = "";

		if(isDifferentValue(value)){
			super.setValue(strValue, false);
			if(fireEvents)
				ValueChangeEvent.fire(this, value);
			selectedValueId = value;
		}
	}

	@Override
	public void clear() {
		if(list != null){
			list.clearSelection();
		}
		super.clear();
	}

	public void setPopupWidth(String width) {
		list.setWidth(width);
	}

	public void setPopupHeight(String height) {
		list.setHeight(height);
	}

	// TypifiedListClient methods

	@Override
	public int getTypifiedDataVersionNumber() {
		return this.typifiedListDataVersion;
	}

	@Override
	public void setTypifiedDataVersionNumber(int number) {
		this.typifiedListDataVersion = number;
	}

	@Override
	public void setTypifiedListItems(List<TipifiedListItem> items) {
		String selectedItemId = this.selectedValueId;
		boolean exists = false;
		this.clearValues();
		for (TipifiedListItem i : items) {
			addItem(i);
			exists |= i.id.equalsIgnoreCase(selectedItemId);
		}
		if(exists){
			setValue(selectedItemId, true);
		}else{
			clear();
		}
		if(this.expectingResponseHandler != null) {
			this.expectingResponseHandler.onResponse(null);
		}
	}

	@Override
	public void removeItem(TipifiedListItem item) {
		int index = super.getItemIndex(item.value, item.id);
		super.removeItem(index);
	}

	@Override
	public void addItem(TipifiedListItem item) {
		super.addItem(item.value, item.id);
		if(selectedValueId != null && item.id.equalsIgnoreCase(selectedValueId)){
			setValue(item.id, false);
		}
	}

	@Override
	public void updateItem(TipifiedListItem item) {
		int size = listBox.getItemCount();
		for(int i = 0; i < size; i++){ 
			if(listBox.getValue(i).equals(item.id)) {
				listBox.setItemText(i, item.value);
				break;
			}
		}
	}

	/**
	 * Gets all the typified list item entries
	 * @return A collection of list items wrapped in value selectables
	 */
	public Collection<ValueSelectable<TipifiedListItem>> getListItems(){
		ArrayList<ValueSelectable<TipifiedListItem>> result = new ArrayList<ValueSelectable<TipifiedListItem>>(this.list);
		return result;
	}

}
