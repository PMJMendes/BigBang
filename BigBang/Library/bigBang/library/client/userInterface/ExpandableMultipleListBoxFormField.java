package bigBang.library.client.userInterface;

//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import bigBang.definitions.shared.TipifiedListItem;
//import bigBang.library.client.Checkable;
//import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
//import bigBang.library.client.Selectable;
//import bigBang.library.client.ValueSelectable;
//import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
//import bigBang.library.client.dataAccess.TypifiedListBroker;
//import bigBang.library.client.dataAccess.TypifiedListClient;
//import bigBang.library.client.event.CheckedStateChangedEvent;
//import bigBang.library.client.event.CheckedStateChangedEventHandler;
//import bigBang.library.client.event.SelectionChangedEvent;
//import bigBang.library.client.event.SelectionChangedEventHandler;
//import bigBang.library.client.resources.Resources;
//import bigBang.library.client.userInterface.view.PopupPanel;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Style.Cursor;
//import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HasVerticalAlignment;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Image;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.Widget;

public class ExpandableMultipleListBoxFormField extends FormField<String[]> {



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readonly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focus() {
		// TODO Auto-generated method stub
		
	}
	
	/*protected Image expandImage;
	protected TypifiedListManagementPanel list;
	protected int typifiedListDataVersion;
	protected TypifiedListBroker typifiedListBroker;
	protected Label selectedItemsLabel;

	protected boolean hasServices;

	public ExpandableMultipleListBoxFormField(String description) {
		this("", description);
	}

	public ExpandableMultipleListBoxFormField(String listId,
			FieldValidator<String[]> validator) {
		this(listId, "");
		setValidator(validator);
	}

	public ExpandableMultipleListBoxFormField(String listId, String description,
			FieldValidator<String[]> validator) {
		this(listId, description);
		setValidator(validator);
	}

	public ExpandableMultipleListBoxFormField(final String listId,
			final String listDescription) {
		super();
		this.typifiedListBroker = BigBangTypifiedListBroker.Util.getInstance();

		hasServices = listId != null && !listId.equals("");

		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label label = new Label();
		label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(label);
		wrapper.setCellWidth(label, "100px");
		wrapper.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.add((Widget) selectedItemsLabel);
		Resources r = GWT.create(Resources.class);
		expandImage = new Image(r.listExpandIcon());
		expandImage.getElement().getStyle().setCursor(Cursor.POINTER);
		wrapper.add(mandatoryIndicatorLabel);
		initWidget(wrapper);
		setFieldWidth("150px");
		
		final PopupPanel popup = new PopupPanel(true, "Listagem");

		list = new TypifiedListManagementPanel(listId, listDescription) {

			public void onCellDoubleClicked(
					bigBang.library.client.userInterface.ListEntry<TipifiedListItem> entry) {
				super.onCellDoubleClicked(entry);
				entry.setChecked(!entry.isChecked());
			};
		};
		
		list.setCheckable(true);
		
//		list.addCheckedStateChangedEventHandler(new CheckedStateChangedEventHandler() {
//			
//			@Override
//			public void onCheckedStateChanged(CheckedStateChangedEvent event) {
//				Collection<? extends Selectable> selected = event.getChecked();
//				for (Selectable i : selected) {
//					@SuppressWarnings("unchecked")
//					ValueSelectable<TipifiedListItem> iv = (ValueSelectable<TipifiedListItem>) i;
//					if (!getValue().equals(iv.getValue().id))
//						setValue(iv.getValue().id);
//					break;
//				}
//			}
//		});
		list.setReadOnly(!this.hasServices);
		list.setEditable(this.hasServices);

		expandImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.clear();
				popup.add(list);
				list.clearSelection();
				for(ListEntry<TipifiedListItem> e : list) {
					if(e.getValue().id.equals(ExpandableMultipleListBoxFormField.this.getValue())){
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
		wrapper.add(mandatoryIndicatorLabel);
		setFieldWidth("150px");
		
		this.typifiedListBroker.registerClient(listId, this);
	}

	public void setEditable(boolean editable) {
		this.list.setEditable(editable);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		list.setReadOnly(readonly);
		list.setSelectableEntries(!readonly);
	}

	@Override
	public void setValue(String[] value, boolean fireEvents) {
		String[] strValue = value;
		if (value == null)
			strValue = new String[0];
		for(int i = 0; i < strValue.length; i++) {
			
		}
	}

	@Override
	public void clear() {
		list.clearSelection();
		for(Checkable c : list.getChecked()){
			c.setChecked(false);
		}
	}

	public void setPopupWidth(String width) {
		list.setWidth(width);
	}

	public void setPopupHeight(String height) {
		list.setHeight(height);
	}

	/**
	 * Gets all the typified list item entries
	 * @return A collection of list items wrapped in value selectables
	 *
	public Collection<ValueSelectable<TipifiedListItem>> getListItems(){
		ArrayList<ValueSelectable<TipifiedListItem>> result = new ArrayList<ValueSelectable<TipifiedListItem>>(this.list);
		return result;
	}*/

}
