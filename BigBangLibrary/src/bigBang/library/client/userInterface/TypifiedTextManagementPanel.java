package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.TypifiedText;
import bigBang.definitions.shared.User;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.dataAccess.TypifiedTextBroker;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.TypifiedListManagementPanel.TypifiedListEntry;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TypifiedTextManagementPanel extends View implements TypifiedTextClient, TypifiedListClient, TypifiedManagementPanel{

	private String listId;
	private String tag;
	private FilterableList<TipifiedListItem> list;
	private String selectedValueId;
	private boolean editModeEnabled, editable;
	private BigBangOperationsToolBar toolbar;
	private MenuItem delete;
	private int typifiedListDataVersion;
	private HorizontalPanel mainWrapper;
	protected TypifiedListBroker listBroker;
	protected TypifiedTextBroker textBroker;
	protected HandlerRegistration attachHandlerRegistration;
	private Button newTextButton;
	private TextBoxFormField label = new TextBoxFormField("Etiqueta");
	private TextBoxFormField subject = new TextBoxFormField("Assunto");
	private RichTextAreaFormField textBody = new RichTextAreaFormField();
	private boolean inNewTypifiedText = false;
	private TipifiedListItem selectedItem;

	public TypifiedTextManagementPanel(final String listId, String listDescription) {

		mainWrapper = new HorizontalPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		selectedItem = null;

		//BROKER
		textBroker = (TypifiedTextBroker)DataBrokerManager.staticGetBroker(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT);
		listBroker = BigBangTypifiedListBroker.Util.getInstance();


		//TOOLBAR
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				MessageBox.confirm("Eliminar Modelo", "Tem certeza que pretende eliminar o modelo seleccionado?", new MessageBox.ConfirmationCallback() {

					@Override
					public void onResult(boolean result) {
						if(result){
							deleteItem();
							setEditable(false);
						}
					}
				});
				
			}

		});

		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				setEditable(true);
			}

			@Override
			public void onSaveRequest() {
				setEditable(false);

			}
			@Override
			public void onCancelRequest() {
				setEditable(false);
			}

			@Override
			public void setSaveModeEnabled(boolean enabled) {
				super.setSaveModeEnabled(enabled);
			}

		};

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
				if(event.isAttached()){
					list.getElement().getStyle().setBackgroundColor("white");
				}
			}
		});

		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				if(selectedItem != null){
					for(ListEntry<TipifiedListItem> item : list){
						list.selectNext();
						
						if(item.getValue().id.equalsIgnoreCase(selectedItem.id)){
							item.setSelected(true, true);
							textBroker.getText(tag, item.value.id, new ResponseHandler<TypifiedText>() {
								@Override
								public void onResponse(TypifiedText response) {
									label.setValue(response.label);
									subject.setValue(response.subject);
									textBody.setValue(response.text);
								}

								@Override
								public void onError(Collection<ResponseError> errors) {
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o texto pedido."), TYPE.ALERT_NOTIFICATION));

								}
							});
							break;
						}
					}

					selectedItem = null;
				}
				else{
					for(ListEntry<TipifiedListItem> item : list){
						if(item.isSelected){
							textBroker.getText(tag, item.value.id, new ResponseHandler<TypifiedText>() {

								@Override
								public void onResponse(TypifiedText response) {
									label.setValue(response.label);
									subject.setValue(response.subject);
									textBody.setValue(response.text);
								}

								@Override
								public void onError(Collection<ResponseError> errors) {
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o texto pedido."), TYPE.ALERT_NOTIFICATION));

								}
							});
							break;
						}
					}
				}

			}
		});

		setListId(listId);
		newTextButton = new Button("Novo");
		newTextButton.setSize("80px", "27px");
		newTextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setupNewTypifiedText();

			}
		});
		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader();
		headerWrapper.add(header);
		header.setText("Modelos");
		header.setRightWidget(newTextButton);
		list.setSize("300px", "100%");
		list.setHeaderWidget(headerWrapper);
		list.showFilterField(false);
		VerticalPanel rightPanel = new VerticalPanel();

		//TypifiedText

		subject.setWidth("100%");
		subject.setFieldWidth("100%");
		textBody.setFieldWidth("100%");

		//wrap
		rightPanel.add(toolbar);
		rightPanel.add(label);
		rightPanel.add(subject);
		rightPanel.add(textBody);
		rightPanel.setWidth("100%");
		mainWrapper.add(list);
		mainWrapper.add(rightPanel);


		toolbar.setSaveModeEnabled(false);
		this.setEditable(false);
	}

	protected void deleteItem() {
		
		TypifiedListEntry toDelete = (TypifiedListEntry) list.getSelected().iterator().next();
		
		textBroker.removeText(tag, toDelete.getValue().id, new ResponseHandler<TypifiedText>() {
			
			@Override
			public void onResponse(TypifiedText response) {
				label.setValue("");
				subject.setValue("");
				textBody.setValue("");
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Modelo eliminado."), TYPE.TRAY_NOTIFICATION));
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o modelo seleccionado."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	protected void setupNewTypifiedText() {


		subject.setValue("");
		label.setValue("Novo Modelo");
		textBody.setValue("");

		createNewText();

		setEditable(true);


	}

	private void createNewText() {

		TypifiedText newT = new TypifiedText();
		newT.label = label.getValue();
		newT.subject = subject.getValue();
		newT.tag = tag;
		newT.text = textBody.getValue();

		textBroker.createText(tag, newT, new ResponseHandler<TypifiedText>() {

			@Override
			public void onResponse(TypifiedText response) {
				
				//EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Modelo criado."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o modelo."), TYPE.ALERT_NOTIFICATION));

			}
		});


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
		
		listBroker.removeListItem(listId, text.id, new ResponseHandler<TipifiedListItem>() {
			
			@Override
			public void onResponse(TipifiedListItem response) {
				listBroker.refreshListData(listId);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				
			}
		});
		
		
	}

	@Override
	public void addText(TypifiedText text) {
		
		list.clearSelection();
		listBroker.refreshListData(listId);
		TipifiedListItem temp = new TipifiedListItem();
		temp.id = text.id;
		temp.value = text.label;
		selectedItem = temp;		
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
			String[] splitted = listId.split("/");
			setTag(splitted[1]);
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
		label.setReadOnly(!editable);
		subject.setReadOnly(!editable);
		textBody.setReadOnly(!editable);
		toolbar.setSaveModeEnabled(editable);
		delete.setEnabled(editable);
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
		textBroker.unregisterClient(this);
		textBroker.registerClient(tag, this);
	}

	@Override
	public int getTypifiedTextDataVersionNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTypifiedTextDataVersionNumber(int number) {
		// TODO Auto-generated method stub

	}


}
