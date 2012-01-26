package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.TypifiedListManagementPanel.TypifiedListEntry;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.userInterface.view.View;

public class TypifiedTextManagementPanel extends View implements TypifiedTextClient, TypifiedListClient, TypifiedManagementPanel{

	private String listId;
	private FilterableList<TipifiedListItem> list;
	private String selectedValueId;
	private boolean editModeEnabled, editable;
	private BigBangOperationsToolBar toolbar;
	private MenuItem delete;
	private int typifiedListDataVersion;
	private HorizontalPanel mainWrapper;
	protected TypifiedListBroker listBroker;
	protected HandlerRegistration attachHandlerRegistration;
	private Button newText;


	public TypifiedTextManagementPanel(final String listId, String listDescription) {

		mainWrapper = new HorizontalPanel();
		initWidget(mainWrapper);
		
		//TOOLBAR
		
		delete = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				fireAction(Action.DELETE);
				
			}

		});
		
		toolbar = new BigBangOperationsToolBar(){
			
			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT);

			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE);

			}
			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL);
			}
			
			@Override
			public void setSaveModeEnabled(boolean enabled) {
				super.setSaveModeEnabled(enabled);
			}

		};
		
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.addItem(delete);
		delete.getElement().getStyle().setProperty("textAlign", "center");
		delete.setEnabled(false);
		toolbar.setHeight("21px");
		toolbar.setWidth("100%");
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.addItem(delete);
		delete.getElement().getStyle().setProperty("textAlign", "center");
		delete.setEnabled(false);
		toolbar.setHeight("21px");
		toolbar.setWidth("100%");
		
		
		//LIST
		list = new FilterableList<TipifiedListItem>(){

			@Override
			protected void onCellDoubleClicked(bigBang.library.client.userInterface.ListEntry<TipifiedListItem> entry) {
				TypifiedTextManagementPanel.this.onCellDoubleClicked(entry);
			};

			@Override
			protected HandlerRegistration bindEntry(ListEntry<TipifiedListItem> e) {
				e.setDoubleClickable(true);
				return super.bindEntry(e);
			}

		};
		
		list.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached())
					getElement().getStyle().setBackgroundColor("white");
			}
		});
		
		
		listBroker = BigBangTypifiedListBroker.Util.getInstance();
		setListId(listId);
		mainWrapper.setSize("300px", "400px");
		newText = new Button("Novo");
		newText.setSize("80px", "27px");
		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader();
		headerWrapper.add(header);
		header.setText("Modelos");
		header.setRightWidget(newText);
		list.setHeaderWidget(headerWrapper);
		list.showFilterField(false);
		System.out.println(list.size());
		mainWrapper.add(list);
		mainWrapper.add(toolbar);

		
	}
	
	protected void fireAction(Action delete2) {
		// TODO Auto-generated method stub
		
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
	public void setTypifiedTexts(List<TypifiedText> texts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeText(TypifiedText text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addText(TypifiedText text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateText(TypifiedText text) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getListId() {
		return this.listId;
	}

	@Override
	public void setListId(final String listId) {
		if(listId != null) {
			listBroker.unregisterClient(this.listId, this);
		}
		this.listId = listId;
		editable = listId != null && !listId.equals("");
		list.clear();

		if(this.attachHandlerRegistration == null){
			this.attachHandlerRegistration = this.addAttachHandler(new AttachEvent.Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						listBroker.registerClient(TypifiedTextManagementPanel.this.listId, TypifiedTextManagementPanel.this);
						listBroker.getListItems(TypifiedTextManagementPanel.this.listId);
					}else{
						listBroker.unregisterClient(TypifiedTextManagementPanel.this.listId, TypifiedTextManagementPanel.this);
					}
				}
			});
		}

		if(list.isAttached()){
			listBroker.registerClient(listId, this);
			listBroker.getListItems(listId);
		}

	}


	@Override
	public void setEditModeEnabled(boolean enabled) {
		this.editModeEnabled = enabled;
		list.showSearchField(!enabled);
		for(ListEntry<TipifiedListItem> e : list.entries) {
			((TypifiedListEntry) e).deleteButton.setVisible(enabled);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public void setReadOnly(boolean readonly) {
		this.setEditModeEnabled(false);
	}

	@Override
	public void setTypifiedListItems(List<TipifiedListItem> items) {
		{
			Collection<ValueSelectable<TipifiedListItem>> selected = list.getSelected();
			list.clear();
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

	}

	@Override
	public void removeItem(TipifiedListItem item) {
		for(ValueSelectable<TipifiedListItem> i : this.list) {
			if(i.getValue() == item){
				this.list.remove(i);
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
		for(ValueSelectable<TipifiedListItem> i : list) {
			if(i.getValue().id == item.id){
				i.setValue(item);
				((TypifiedListEntry)i).setInfo(item);
				break;
			}
		}
	}

	protected TypifiedListEntry addEntry(TipifiedListItem item){
		final TypifiedListEntry entry = new TypifiedListEntry(item);
		entry.deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteEntry(entry);
			}
		});
		list.add(entry);
		if(this.selectedValueId != null && this.selectedValueId.equalsIgnoreCase(item.id))
			entry.setSelected(true);
		setEditModeEnabled(editModeEnabled);
		return entry;
	}

	private void deleteEntry(final TypifiedListEntry e){
		listBroker.removeListItem(listId, e.getValue().id, new ResponseHandler<TipifiedListItem>() {

			@Override
			public void onResponse(TipifiedListItem response) {
				list.remove(e);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	private boolean isEditModeEnabled(){
		return editModeEnabled;
	}

	public void onChanged(){

	}

	@Override
	public FilterableList<TipifiedListItem> getList() {
		return list;
	}

	@Override
	protected void initializeView() {
		return;
	}

	protected void onCellDoubleClicked(bigBang.library.client.userInterface.ListEntry<TipifiedListItem> entry) {};

	
}
