package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.CompositeFieldContainer.SubLineFieldContainer;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsuredObjectStub.Change;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSublineFormSection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSearchOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		SAVE, 
		RECEIVE_MESSAGE, 
		CLOSE, 
		MANAGER_TRANSFER, 
		CREATE_NEGOTIATION,
		DELETE, SEND_MESSAGE, 
		SEND_RESPONSE_TO_CLIENT, 
		CANCEL, 
		EDIT, QUOTE_REQUEST_SELECTED, NEW_OBJECT, NEW_SUBLINE, NEW_OBJECT_CHOSEN, NEW_SUBLINE_CHOSEN, DELETE_SUBLINE, CLOSE_SUBLINE_SECTION, OPEN_SUBLINE_SECTION, CLOSE_ON_OPEN_SUBLINE_SECTION

	}

	public interface Display {

		void doSearch(boolean b);

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		void clear();

		void setToolbarEditMode(boolean b);

		void allowEdit(boolean b);

		HasValue<QuoteRequestStub> getQuoteRequestSelector();

		void clearObjectsList();

		HasValue<String> getQuoteRequestNotesForm();

		void setReadOnly(boolean b);

		HasValueSelectables<QuoteRequestStub> getList();

		void addEntryToList(QuoteRequestSearchPanel.Entry entry);

		void removeElementFromList(ValueSelectable<QuoteRequestStub> stub);

		HasValueSelectables<QuoteRequestObjectStub> getObjectList();

		HasEditableValue<QuoteRequest> getQuoteRequestHeaderForm();

		void showObjectForm(boolean b);

		void showQuoteRequestForm(boolean b);

		void setNotesReadOnly(boolean b);

		void setQuoteRequestEntrySelected(boolean b);

		void allowSendMessage(boolean hasPermission);

		void allowReceiveMessage(boolean hasPermission);

		void allowDelete(boolean hasPermission);

		HasEditableValue<QuoteRequestObject> getObjectForm();

		void clearQuoteRequestSelection();

		void dealWithObject(QuoteRequestObjectStub quoteRequestObjectStub);

		void setOwner(QuoteRequest object);

		void focusObjectForm();

		void showSubLineChooser();

		void showObjectTypeChooser();

		void lockToolbar();

		void allowCreateNegotiation(boolean hasPermission);

		void allowManagerTransfer(boolean hasPermission);

		String getNewSublineId();

		QuoteRequestSublineFormSection createSublineFormSection(SubLineFieldContainer container);

		void deleteSubLine(QuoteRequestSublineFormSection currentSubline);

		QuoteRequestSublineFormSection getOpenedSection();

		String getObjectType();

		void setSelectedObject(String id);

		HasClickHandlers getObjectDeleteButton();

		String getSublineId();

		void clearSubLines();

		boolean containsSubLine(String sublineId);

		void setSelectedSubline(String sublineId);

		void closeSublineSection(boolean b);

		List<HistoryItemStub> getHistoryList();

		List<Contact> getContactsList();

		List<Document> getDocumentsList();

		List<BigBangProcess> getSubProcessesList();

		List<ConversationStub> getConversationList();

		void allowCloseProcess(boolean hasPermission);
	}

	protected QuoteRequestBroker broker;
	protected Display view;
	protected boolean bound = false;
	private String quoteRequestId;
	protected boolean isEditModeEnabled;
	private boolean onQuoteRequest;
	private QuoteRequestSublineFormSection currentSubline;

	public QuoteRequestSearchOperationViewPresenter(View view) {
		this.broker = (QuoteRequestBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		quoteRequestId = parameterHolder.getParameter("quoteRequestId");

		currentSubline = null;

		clearListSelectionNoFireEvent();

		if(quoteRequestId != null){
			if(quoteRequestId.equalsIgnoreCase("new")){
				String clientId = parameterHolder.getParameter("clientid");
				broker.getEmptyQuoteRequest(clientId, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest response) {
						isEditModeEnabled = true;
						view.lockToolbar();
						view.setToolbarEditMode(true);
						view.allowEdit(true);
						view.getQuoteRequestSelector().setValue(response);
						view.setOwner(null);
						view.clearObjectsList();
						fillQuoteRequest();
						view.setReadOnly(false);
						ensureListedAndSelected(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar uma nova Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
					}
				});
			}
			else{
				broker.getQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest response) {
						isEditModeEnabled = false;
						view.setOwner(response);
						view.setToolbarEditMode(false);
						view.getQuoteRequestSelector().setValue(response);
						view.getQuoteRequestNotesForm().setValue(response.notes);
						fillQuoteRequest();
						setSublines(broker.getLocalSubLines(response.id));
						view.setReadOnly(true);		
						ensureListedAndSelected(response);
						setPermissions(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
					}
				});
			}
		}
		else{
			view.lockToolbar();
			view.getQuoteRequestHeaderForm().setValue(null);
			view.getObjectForm().setValue(null);
			view.getQuoteRequestNotesForm().setValue(null);
			view.setOwner(null);
			view.getQuoteRequestSelector().setValue(null);
			view.clearObjectsList();
			view.setReadOnly(true);
			view.setQuoteRequestEntrySelected(false);
		}

	}

	protected void setSublines(SubLineFieldContainer[] subLineData) {
		view.clearSubLines();

		if(subLineData != null){
			for(SubLineFieldContainer cont : subLineData){
				view.createSublineFormSection(cont);
			}
		}
	}

	private void clearListSelectionNoFireEvent() {

		if(view.getQuoteRequestHeaderForm() != null && view.getQuoteRequestHeaderForm().getValue() != null && view.getQuoteRequestHeaderForm().getValue().id != null){
			for(ValueSelectable<QuoteRequestStub> stub : view.getList().getSelected()){
				if(!stub.getValue().id.equals(quoteRequestId)){
					stub.setSelected(false, false);
					return;
				}

			}
		}		
	}

	protected void setPermissions(QuoteRequest response) {
		view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST));
		view.allowSendMessage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CONVERSATION));
		view.allowReceiveMessage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CONVERSATION));
		view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.DELETE_QUOTE_REQUEST));
		view.allowCreateNegotiation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_NEGOTIATION));
		view.allowManagerTransfer(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER));
		view.allowCloseProcess(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST));
	}

	protected void ensureListedAndSelected(QuoteRequest response) {
		boolean found = false;
		for(ValueSelectable<QuoteRequestStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(response.id)){
				found  = true;
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false,false);
			}
		}
		if(!found){
			QuoteRequestSearchPanel.Entry entry = new QuoteRequestSearchPanel.Entry(response);
			view.addEntryToList(entry);
		}

		if(view.getList().getAll().size() > 0 && !quoteRequestId.equals("new")){
			removeNewListEntry();
		}

	}

	private void removeNewListEntry() {
		for(ValueSelectable<QuoteRequestStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equalsIgnoreCase("new")){
				view.removeElementFromList(stub);
				return;
			}
		}		
	}

	protected void fillQuoteRequest() {
		QuoteRequest quote = broker.getRequestHeader(quoteRequestId);
		view.getObjectList().clearSelection();
		view.getQuoteRequestHeaderForm().setValue(quote);
		view.showObjectForm(false);
		view.showQuoteRequestForm(true);
		view.setNotesReadOnly(false);
		view.setQuoteRequestEntrySelected(true);
		onQuoteRequest = true;
		view.getQuoteRequestHeaderForm().validate();
		if(currentSubline != null && currentSubline.isOpen()){
			currentSubline.setData(broker.getContextForRequest(quoteRequestId, view.getSublineId()));
		}
	}

	public void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CLOSE:
					onClose();
					break;
				case CREATE_NEGOTIATION:
					onCreateNegotiation();
					break;
				case DELETE:
					onDelete();
					break;
				case EDIT:
					onEdit();
					break;
				case MANAGER_TRANSFER:
					onManagerTransfer();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SAVE:
					onSave();
					break;
				case SEND_MESSAGE:
					onSendMessage();
					break;
//				case SEND_RESPONSE_TO_CLIENT:
//					onSendResponseToClient();
//					break;
				case QUOTE_REQUEST_SELECTED:
					onQuoteRequestSelected();
					break;
				case NEW_OBJECT:
					onNewObject();
					break;
				case NEW_OBJECT_CHOSEN:
					onNewObjectChosen();
					break;
				case NEW_SUBLINE:
					onNewSubLine();
					break;
				case NEW_SUBLINE_CHOSEN:
					onNewSublineChosen();
					break;
				case DELETE_SUBLINE:
					onDeleteSubLine();
					break;
				case CLOSE_ON_OPEN_SUBLINE_SECTION:
					onCloseOnOpenSublineSection();
					break;
				case OPEN_SUBLINE_SECTION:
					onOpenSublineSection();
					break;
				case CLOSE_SUBLINE_SECTION:
					onCloseSublineSection();
					break;
				}
			}

		});

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<QuoteRequestStub> selected = (ValueSelectable<QuoteRequestStub>) event.getFirstSelected();

				if(selected != null && selected.getValue().id.equals("new")){
					return;
				}
				removeNewListEntry();
				QuoteRequestStub selectedValue = selected == null ? null : selected.getValue();
				String selectedQuoteRequestId = selectedValue == null ? new String() : selectedValue.id;
				selectedQuoteRequestId = selectedQuoteRequestId == null ? new String() : selectedQuoteRequestId;
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(selectedQuoteRequestId.isEmpty()){
					item.removeParameter("quoterequestid");
				}else{
					item.setParameter("quoterequestid", selectedQuoteRequestId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.getObjectList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getFirstSelected() != null){
					@SuppressWarnings("unchecked")
					QuoteRequestObjectStub stub = ((ValueSelectable<QuoteRequestObjectStub>)event.getFirstSelected()).getValue();
					if(stub.change != Change.DELETED){
						onObjectSelected(stub);
					}
					else{
						if(onQuoteRequest){
							view.getObjectList().clearSelection();
						}else{
							view.setSelectedObject(view.getObjectForm().getValue().id);
						}
					}
				}
			}
		});

		view.getObjectDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onRemoveQuoteRequestObject();
			}
		});

		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> selected = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				HistoryItemStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.pushIntoStackParameter("display", "history");
					navItem.setParameter("historyownerid", view.getQuoteRequestHeaderForm().getValue().id);
					navItem.setParameter("historyItemId", itemId);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				Contact item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "contactmanagement");
					navItem.setParameter("ownerid", view.getQuoteRequestHeaderForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.QUOTE_REQUEST);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Document> selected = (ValueSelectable<Document>) event.getFirstSelected();
				Document item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "documentmanagement");
					navItem.setParameter("ownerid", item.ownerId);
					navItem.setParameter("documentid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.QUOTE_REQUEST);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<BigBangProcess> selected = (ValueSelectable<BigBangProcess>) event.getFirstSelected();
				BigBangProcess item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.dataId;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					goToSubProcess(item);
				}
			}
		});

		view.getConversationList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ConversationStub stub = event.getFirstSelected() == null ? null : ((ValueSelectable<ConversationStub>) event.getFirstSelected()).getValue();
				if(stub != null){
					showConversation(stub.id);
				}
			}
		});


		bound = true;
	}


	protected void showConversation(String id) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "conversation");
		item.setParameter("conversationid", id);
		NavigationHistoryManager.getInstance().go(item);			
	}

	protected void goToSubProcess(BigBangProcess process) {
		
		String type = process.dataTypeId;
		
		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MANAGER_TRANSFER)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewmanagertransfer");
			item.setParameter("transferid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
		else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			showConversation(process.dataId);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
			showNegotiation(process.dataId);
		}
	}

	private void showNegotiation(String dataId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "negotiation");
		item.setParameter("negotiationid", dataId);
		NavigationHistoryManager.getInstance().go(item);		
	}

	protected void onCloseSublineSection() {
		broker.updateSubLineCoverages(quoteRequestId, currentSubline.getValue().subLineId, view.getOpenedSection().getValue().coverages);

		if(onQuoteRequest){
			broker.saveContextForRequest(quoteRequestId, currentSubline.getValue().subLineId, currentSubline.getValue());
		}else{
			broker.saveContextForCompositeObject(quoteRequestId, currentSubline.getValue().subLineId,view.getObjectForm().getInfo().id, currentSubline.getValue());
		}
	}

	protected void onRemoveQuoteRequestObject() {
		view.dealWithObject(broker.removeRequestObject(quoteRequestId, view.getObjectForm().getValue().id));
		onQuoteRequestSelected();
	}

	protected void onObjectSelected(QuoteRequestObjectStub stub) {
		if(onQuoteRequest){
			view.clearQuoteRequestSelection();
		}
		saveInternally();

		fillObject(stub.id);

		onQuoteRequest = false;		
	}

	protected void onOpenSublineSection() {

		currentSubline = view.getOpenedSection();

		if(onQuoteRequest){
			currentSubline.setData(broker.getContextForRequest(quoteRequestId, view.getSublineId()));
		}
		else{
			currentSubline.setData(broker.getContextForCompositeObject(quoteRequestId, view.getSublineId(), view.getObjectForm().getInfo().id));
		}

	}

	protected void onCloseOnOpenSublineSection() {
		if(currentSubline != null){
			broker.updateSubLineCoverages(quoteRequestId, currentSubline.getValue().subLineId, view.getOpenedSection().getValue().coverages);

			if(onQuoteRequest){
				broker.saveContextForRequest(quoteRequestId, currentSubline.getValue().subLineId, currentSubline.getValue());
			}else{
				broker.saveContextForCompositeObject(quoteRequestId, currentSubline.getValue().subLineId,view.getObjectForm().getInfo().id, currentSubline.getValue());
			}

			view.closeSublineSection(false);
		}


	}

	protected void onDeleteSubLine() {
		broker.removeSubLine(quoteRequestId, currentSubline.getValue().subLineId);
		view.deleteSubLine(currentSubline);
		currentSubline = null;
	}

	protected void onNewSubLine() {
		view.showSubLineChooser();
	}

	protected void onNewSublineChosen() {
		if (!view.containsSubLine(view.getNewSublineId())){
			broker.createSubLine(quoteRequestId, view.getNewSublineId(), new ResponseHandler<CompositeFieldContainer.SubLineFieldContainer>() {

				@Override
				public void onResponse(SubLineFieldContainer response) {
					QuoteRequestSublineFormSection section = view.createSublineFormSection(response);
					section.setValue(response);
					section.expand();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar uma nova modalidade."), TYPE.ALERT_NOTIFICATION));					
				}
			});		
		}else{
			view.setSelectedSubline(view.getNewSublineId());
		}

	}

	protected void onNewObjectChosen() {
		if(onQuoteRequest){
			view.clearQuoteRequestSelection();
		}
		saveInternally();

		fillObject(null);
		onQuoteRequest = false;

	}

	protected void onNewObject() {
		view.showObjectTypeChooser();

	}

	private void fillObject(String id) {

		if(id == null){
			QuoteRequestObject newQuoteRequestObject = broker.createRequestObject(quoteRequestId, view.getObjectType());
			newQuoteRequestObject.unitIdentification = "Nova Unidade de Risco";
			view.getObjectForm().setValue(newQuoteRequestObject);
			view.showObjectForm(true);
			view.showQuoteRequestForm(false);
			view.dealWithObject(newQuoteRequestObject);
			view.focusObjectForm();
			view.getObjectForm().validate();
			if(currentSubline != null && currentSubline.isOpen()){
				currentSubline.setData(broker.getContextForCompositeObject(quoteRequestId, view.getSublineId(), newQuoteRequestObject.id));	
			}
		}
		else{
			broker.getRequestObject(quoteRequestId, id, new ResponseHandler<QuoteRequestObject>() {

				@Override
				public void onResponse(QuoteRequestObject response) {
					view.getObjectForm().setValue(response);
					view.showObjectForm(true);
					view.showQuoteRequestForm(false);
					view.dealWithObject(response);
					view.setNotesReadOnly(true);
					view.getObjectForm().validate();
					if(currentSubline != null && currentSubline.isOpen()){
						currentSubline.setData(broker.getContextForCompositeObject(quoteRequestId, view.getSublineId(), response.id));			
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Unidade de Risco."), TYPE.ALERT_NOTIFICATION));					

				}
			});
		}

	}

	protected void onQuoteRequestSelected() {
		if(onQuoteRequest)
			return;

		saveInternally();
		fillQuoteRequest();
	}

	protected void onCancel() {
		NavigationHistoryManager.getInstance().reload(); //TODO REGRESSAR A CLEINTE QUANDO CONSULTA É NOVA??
	}

//	protected void onSendResponseToClient() {
//		// TODO Auto-generated method stub
//
//	}

	protected void onSendMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "sendmessage");
		item.setParameter("ownerid", quoteRequestId);
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void onSave() {
		saveInternally();
		if(currentSubline != null && currentSubline.isOpen()){
			broker.updateSubLineCoverages(quoteRequestId, currentSubline.getValue().subLineId, view.getOpenedSection().getValue().coverages);
		}

		if(view.getQuoteRequestHeaderForm().validate()){
			broker.persistQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {

				@Override
				public void onResponse(QuoteRequest response) {
					if(quoteRequestId.equalsIgnoreCase("new")){
						removeNewListEntry();
					}
					onSaveQuoteRequestSuccess(response.id);
					isEditModeEnabled = false;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		else{
			onFormValidationFailed();
		}

	}

	protected void onSaveQuoteRequestSuccess(String id) {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Consulta de Mercado guardada com sucesso"), TYPE.TRAY_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("quoterequestid",id);
		NavigationHistoryManager.getInstance().go(item);

	}

	private void saveInternally() {
		if(!isEditModeEnabled){
			return;
		}
		if(onQuoteRequest){
			QuoteRequest changedRequest = view.getQuoteRequestHeaderForm().getInfo();
			changedRequest.notes = view.getQuoteRequestNotesForm().getValue();
			broker.updateRequestHeader(changedRequest);
			if(view.getOpenedSection() != null){
				broker.saveContextForRequest(quoteRequestId, currentSubline.getValue().subLineId, view.getOpenedSection().getValue());
			}
		}else{
			broker.updateRequestObject(quoteRequestId, view.getObjectForm().getInfo());
			if(view.getOpenedSection() != null){
				broker.saveContextForCompositeObject(quoteRequestId, currentSubline.getValue().subLineId, view.getObjectForm().getInfo().id , view.getOpenedSection().getValue());
			}
			view.dealWithObject(view.getObjectForm().getInfo());
		}
	}

	protected void onReceiveMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "receivemessage");
		item.setParameter("ownerid", quoteRequestId);
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void onManagerTransfer() {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "transfermanager");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onEdit() {
		view.setReadOnly(false);
		isEditModeEnabled = true;
		view.setToolbarEditMode(true);

	}

	protected void onDelete() {
//		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
//		item.setParameter("show", "deleterequest");
//		NavigationHistoryManager.getInstance().go(item);
		
		broker.removeQuoteRequest(quoteRequestId, null, new ResponseHandler<String>() {
			
			@Override
			public void onResponse(String response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("quoterequestid");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Consulta de Mercado eliminada com sucesso"), TYPE.TRAY_NOTIFICATION));
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void onCreateNegotiation() {
		showNegotiation("new");
	}

	protected void onClose() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "closerequest");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}
}

