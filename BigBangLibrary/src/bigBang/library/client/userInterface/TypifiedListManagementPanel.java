package bigBang.library.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.resources.Resources;
import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.shared.TipifiedListItem;

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

public class TypifiedListManagementPanel extends FilterableList<TipifiedListItem> {

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
	private TextBox valueTextBox;
	private boolean editModeEnabled, hasService, editable;

	//UI
	private Button editButton;
	private HorizontalPanel toolBar;

	public TypifiedListManagementPanel(String listId, String listName){
		super();
		this.listId = listId;
		hasService = editable = listId != null && !listId.equals("");
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

		if(hasService){
			TipifiedListService.Util.getInstance().getListItems(this.listId, new BigBangAsyncCallback<TipifiedListItem[]>() {

				@Override
				public void onSuccess(TipifiedListItem[] result) {
					for(int i = 0; i < result.length; i++) {
						final TypifiedListEntry entry = new TypifiedListEntry(result[i]);
						entry.deleteButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								deleteEntry(entry);
							}
						});
						add(entry);
					}
					setEditModeEnabled(isEditModeEnabled());
				}
			});
		}
		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached())
					getElement().getStyle().setBackgroundColor("white");
			}
		});

		setEditModeEnabled(false);
	}

	private void addNew(){
		if(!hasService)
			return;
		String value = valueTextBox.getValue();
		TipifiedListItem item = new TipifiedListItem();
		item.value = value;
		TipifiedListService.Util.getInstance().createListItem(this.listId, item, new BigBangAsyncCallback<TipifiedListItem>() {

			@Override
			public void onSuccess(TipifiedListItem result) {
				final TypifiedListEntry entry = new TypifiedListEntry(result);
				entry.deleteButton.setVisible(isEditModeEnabled());
				entry.deleteButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						deleteEntry(entry);
					}
				});
				add(entry);
				valueTextBox.setValue("");
				clearSelection();
				entry.setSelected(true);
				getScrollable().scrollToBottom();
			}
		});
	}

	private void deleteEntry(final TypifiedListEntry e){
		if(!hasService)
			return;
		TipifiedListService.Util.getInstance().deleteListItem(this.listId, e.getValue().id,
				new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				clearSelection();
				remove(e);
			}

			@Override
			public void onFailure(Throwable caught) {

				super.onFailure(caught);
			}
		});
	}

	public void setEditModeEnabled(boolean enabled){
		this.editModeEnabled = enabled;
		this.editButton.setText(enabled ? "Cancelar" : "Editar");
		showFilterField(!enabled);
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
		this.editButton.setVisible(!editable);
	}
	
	public void setReadOnly(boolean readonly) {
		this.setEditModeEnabled(false);
		this.editButton.setVisible(!readonly && editable);
	}

	@Override
	protected HandlerRegistration bindEntry(ListEntry<TipifiedListItem> e) {
		e.setDoubleClickable(true);
		return super.bindEntry(e);
	}

}
