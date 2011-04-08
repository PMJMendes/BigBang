package bigBang.library.client.userInterface;

import java.util.Collection;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.shared.TipifiedListItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ExpandableListBoxFormField extends ListBoxFormField {

	protected Image expandImage;
	protected TypifiedListManagementPanel list;

	public ExpandableListBoxFormField(final String listId, final String listDescription){
		super();

		label.setText(listDescription);

		wrapper.clear();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.add((Widget) this.listBox);
		Resources r = GWT.create(Resources.class);
		expandImage = new Image(r.listExpandIcon());
		expandImage.getElement().getStyle().setCursor(Cursor.POINTER);

		expandImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PopupPanel popup = new PopupPanel(true, "Listagem");
				popup.add(list);
				list.clearFilters();
				list.setEditModeEnabled(false);
				synchronizeToListBox();
				popup.center();
			}
		});

		list = new TypifiedListManagementPanel(listId, listDescription) {
			@Override
			protected void onSizeChanged() {
				synchronizeToListBox();
			};
		};
		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.isEmpty()){
					setValue("");
				}else{
					for(Selectable i : selected) {
						@SuppressWarnings("unchecked")
						ValueSelectable<TipifiedListItem> iv = (ValueSelectable<TipifiedListItem> ) i; 
						if(getValue().equals(iv.getValue().id))
							break;
						setValue(iv.getValue().id);
						break;
					}
				}
			}
		});

		wrapper.add(expandImage);
		wrapper.add(mandatoryIndicatorLabel);
		setFieldWidth("150px");

		clearValues();
	}

	protected void synchronizeToListBox(){
		String selectedId = getValue();
		clearValues();
		for(ValueSelectable<TipifiedListItem> i : list){
			addItem(i.getValue().value, i.getValue().id);
			if(i.getValue().id.equals(selectedId))
				i.setSelected(true, true);
		}
		if(selectedId.equals(""))
			list.clearSelection();
	}

	@Override
	public void setReadOnly(boolean readonly) {
		super.setReadOnly(readonly);
		expandImage.setVisible(!readonly);
	}

	public ExpandableListBoxFormField(String listId, FieldValidator<String> validator) {
		this(listId, "");
		setValidator(validator);
	}

	public ExpandableListBoxFormField(String listId, String description, FieldValidator<String> validator) {
		this(listId, description);
		setValidator(validator);
	}
	
	@Override
	public void clear() {
		list.clearSelection();
		super.clear();
	}

}
