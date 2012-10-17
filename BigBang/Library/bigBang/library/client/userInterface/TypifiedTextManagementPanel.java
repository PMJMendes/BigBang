package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.dataAccess.TypifiedTextBroker;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.TypifiedListManagementPanel.TypifiedListEntry;
import bigBang.library.client.userInterface.form.TypifiedTextForm;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TypifiedTextManagementPanel extends View implements TypifiedTextClient, TypifiedListClient, TypifiedManagementPanel{


	public class TypifiedTextOperationsToolbar extends BigBangOperationsToolBar{

		private MenuItem delete;

		@Override
		public void lockAll() {
			adminMenuItem.setEnabled(false);
			saveMenuItem.setEnabled(false);
			editCancelMenuItem.setEnabled(false);
		}

		public TypifiedTextOperationsToolbar(){
			super();
			delete = new MenuItem("Eliminar", new Command() {

				@Override
				public void execute() {
					onDeleteRequest();
				}



			});

			this.addItem(SUB_MENU.ADMIN, delete);
			this.hideAll();
			this.showItem(SUB_MENU.EDIT, true);
			this.adminSubMenu.setVisible(true);
			this.showItem(SUB_MENU.ADMIN, true);
			this.setHeight("21px");
			this.adminSubMenu.getElement().getStyle().setZIndex(12000);
			lockAll();

		}

		@Override
		public void onEditRequest() {

		}

		@Override
		public void onSaveRequest() {
		}

		@Override
		public void onCancelRequest() {
		}

		public void onDeleteRequest(){

		}
	}

	private String listId;
	private String tag;
	private FilterableList<TipifiedListItem> list;
	private String selectedValueId;
	private boolean editModeEnabled;
	private int typifiedListDataVersion;
	private HorizontalPanel mainWrapper;
	protected TypifiedListBroker listBroker;
	protected TypifiedTextBroker textBroker;
	protected HandlerRegistration attachHandlerRegistration;
	private ToolButton newTextButton;
	private TypifiedTextForm form;
	private TypifiedTextOperationsToolbar toolbar;
	private boolean firstTime = true;

	public TypifiedTextManagementPanel(final String listId, String listDescription) {

		mainWrapper = new HorizontalPanel();

		initWidget(mainWrapper);

		form = new TypifiedTextForm();

		mainWrapper.setSize("100%", "100%");

		//BROKER
		textBroker = (TypifiedTextBroker)DataBrokerManager.staticGetBroker(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT);
		listBroker = BigBangTypifiedListBroker.Util.getInstance();

		//TOOLBAR
		toolbar = new TypifiedTextOperationsToolbar(){

			@Override
			public void onEditRequest() {
				allowEdition(true);
			}

			@Override
			public void onSaveRequest(){
				saveItem();
			}
			@Override
			public void onCancelRequest() {
				cancelChanges();
			}

			@Override
			public void onDeleteRequest() {
				MessageBox.confirm("Eliminar Modelo", "Tem certeza que pretende eliminar o modelo seleccionado?", new MessageBox.ConfirmationCallback() {

					@Override
					public void onResult(boolean result) {
						if(result){
							deleteItem();
						}
					}
				});
			}

		};

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
				if(event.isAttached()){
					list.getElement().getStyle().setBackgroundColor("white");
					TypifiedTextManagementPanel.this.allowEdition(false);
				}
			}
		});

		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				if(list.getSelected().isEmpty()){
					toolbar.setEditionAvailable(false);
					form.setValue(null);
					form.setReadOnly(true);
					return;
				}

				for(ListEntry<TipifiedListItem> item : list){

					if(item.isSelected){
						if(form.getValue()!= null && item.getValue().id.equalsIgnoreCase("new")){
							return;
						}

						textBroker.getText(tag, item.value.id, new ResponseHandler<TypifiedText>() {

							@Override
							public void onResponse(TypifiedText response) {

								form.setValue(response);
								toolbar.setEditionAvailable(true);

							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o texto pedido."), TYPE.ALERT_NOTIFICATION));

							}
						});
						break;
					}
				}

				removeNewListEntry();
			}
		});

		setListId(listId);

		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader();
		header.showNewButton("Novo");
		newTextButton = header.getNewButton();
		newTextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(form.getValue() == null || !form.getValue().id.equals("new")){
					createNewText();
				}
			}
		});
		headerWrapper.add(header);
		header.setText("Modelos");
		header.setRightWidget(newTextButton);
		list.setSize("300px", "100%");
		list.setHeaderWidget(headerWrapper);
		list.showFilterField(false);
		VerticalPanel rightPanel = new VerticalPanel();

		//TypifiedText

		rightPanel.add(toolbar);
		rightPanel.add(form.getNonScrollableContent());
		rightPanel.setCellHeight(form.getNonScrollableContent(),"100%");

		//wrap
		mainWrapper.add(list);
		mainWrapper.add(rightPanel);

		toolbar.setSaveModeEnabled(false);
	}

	protected void removeNewListEntry() {

		for( ListEntry<TipifiedListItem> stub : list){
			if(stub.getValue().id.equals("new")){
				list.remove(stub);
				return;
			}
		}

	}

	protected void cancelChanges() {

		if(form.getValue().id.equalsIgnoreCase("new")){
			removeNewListEntry();
			return;
		}
		form.revert();
	}

	protected void saveItem() {

		TypifiedText text = form.getInfo();
		if(form.validate()){
			if(text.id.equalsIgnoreCase("new")){
				text.id = null;
				textBroker.createText(tag, text, new ResponseHandler<TypifiedText>() {
					
					@Override
					public void onResponse(TypifiedText response) {
						selectedValueId = response.id;
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o modelo"), TYPE.ALERT_NOTIFICATION));
					}
				});
			}else{
				textBroker.updateText(tag, text, new ResponseHandler<TypifiedText>() {
					
					@Override
					public void onResponse(TypifiedText response) {
						selectedValueId = response.id;
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o modelo"), TYPE.ALERT_NOTIFICATION));
					}
				});
			}
		}
		else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}

	}

	protected void deleteItem() {

		textBroker.removeText(tag, form.getValue().id, new ResponseHandler<TypifiedText>() {
			
			@Override
			public void onResponse(TypifiedText response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o modelo"), TYPE.ALERT_NOTIFICATION));
			}
		});
		
	}


	private void createNewText() {

		TypifiedText newT = new TypifiedText();
		newT.id = "new";
		newT.label = "Novo Modelo";
		newT.subject = "";
		newT.tag = tag;
		newT.text = "";
		list.clearSelection();

		TipifiedListItem listItem = new TipifiedListItem();
		listItem.id = newT.id;
		ListEntry<TipifiedListItem> entry = new ListEntry<TipifiedListItem>(listItem);
		entry.setTitle(newT.label);
		list.add(entry);
		entry.setSelected(true, false);

		form.setValue(newT);
		form.setReadOnly(false);

		toolbar.setSaveModeEnabled(true);
		toolbar.setEditionAvailable(true);
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

	}

	@Override
	public void removeText(TypifiedText text) {

		listBroker.refreshListData(listId);

	}

	@Override
	public void addText(TypifiedText text) {

		listBroker.refreshListData(listId);
	}

	@Override
	public void updateText(TypifiedText text) {

		listBroker.refreshListData(listId);

	}

	@Override
	public String getListId() {
		return this.listId;
	}

	@Override
	public void setListId(final String listId) {
		if(listId == null){
			return;
		}

		listBroker.unregisterClient(this.listId, this);
		String[] splitted = listId.split("/");
		setTag(splitted[1]);
		this.listId = listId;
		list.clear();

		if(firstTime ){
			listBroker.registerClient(TypifiedTextManagementPanel.this.listId, TypifiedTextManagementPanel.this);
			listBroker.getListItems(TypifiedTextManagementPanel.this.listId);
			firstTime = false;
		}

		if(this.attachHandlerRegistration == null){
			this.attachHandlerRegistration = this.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						listBroker.registerClient(TypifiedTextManagementPanel.this.listId, TypifiedTextManagementPanel.this);
						listBroker.getListItems(TypifiedTextManagementPanel.this.listId);
					}else{
						toolbar.lockAll();
						form.setValue(null);
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
		if(list != null){
			list.showSearchField(!enabled);
			for(ListEntry<TipifiedListItem> e : list.entries) {
				((TypifiedListEntry) e).deleteButton.setVisible(enabled);
			}
		}
	}

	@Override
	public void allowEdition(boolean editable) {

		form.setReadOnly(!editable);
		toolbar.setSaveModeEnabled(editable);
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
					if(entry.getValue().id.equalsIgnoreCase(s.getValue().id)){
						entry.setSelected(true, true);
						selectedValueId = null;
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

		listBroker.refreshListData(listId);
	}

	@Override
	public void addItem(TipifiedListItem item) {
		addEntry(item);
	}

	@Override
	public void updateItem(TipifiedListItem item) {
		for(ValueSelectable<TipifiedListItem> i : list) {
			if(i.getValue().id.equalsIgnoreCase(item.id)){
				i.setValue(item);
				((TypifiedListEntry)i).setInfo(item);
				break;
			}
		}
	}

	protected TypifiedListEntry addEntry(TipifiedListItem item){
		final TypifiedListEntry entry = new TypifiedListEntry(item);
		list.add(entry);
		if(this.selectedValueId != null && this.selectedValueId.equalsIgnoreCase(item.id)){
			entry.setSelected(true);
			selectedValueId = "";
		}
		return entry;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if(this.tag != null){
			textBroker.unregisterClient(this.tag, this);
		}
		this.tag = tag;
		textBroker.registerClient(tag, this);
	}

	@Override
	public int getTypifiedTextDataVersionNumber() {
		return 0;
	}

	@Override
	public void setTypifiedTextDataVersionNumber(int number) {

	}

	@Override
	public void setTypifiedDataBroker(TypifiedListBroker broker) {
		if(getListId() != null){
			this.listBroker.unregisterClient(getListId(), this);
		}
		this.listBroker = broker;
	}

	@Override
	public void setParameters(HasParameters parameters) {
		return;
	}
}
