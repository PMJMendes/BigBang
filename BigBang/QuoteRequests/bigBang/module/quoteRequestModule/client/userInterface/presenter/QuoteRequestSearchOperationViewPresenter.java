package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.AsyncRequest;
import bigBang.library.client.event.AsyncRequestHandler;
import bigBang.library.client.event.FiresAsyncRequests;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSearchOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE,
		CLOSE,
		CREATE_INSURED_OBJECT, CREATE_PERSON_INSURED_OBJECT, CREATE_COMPANY_INSURED_OBJECT, CREATE_EQUIPMENT_INSURED_OBJECT, CREATE_LOCATION_INSURED_OBJECT, CREATE_ANIMAL_INSURED_OBJECT,
		CREATE_MANAGER_TRANSFER,
		CREATE_INFO_OR_DOCUMENT_REQUEST
	}

	public interface Display {
		HasValueSelectables<QuoteRequestStub> getList();
		HasEditableValue<QuoteRequest> getForm();
		FiresAsyncRequests getFormAsFiresAsyncRequests();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowClose(boolean allow);
		void allowCreateInsuredObject(boolean allow);
		void allowCreateManagerTransfer(boolean allow);
		void allowCreateInfoorDocumentRequest(boolean allow);

		//Children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<QuoteRequestObjectStub> getObjectsList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();

		void setSaveModeEnabled(boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected QuoteRequestBroker broker;
	protected Display view;
	protected boolean bound = false;

	public QuoteRequestSearchOperationViewPresenter(View view) {
		setView(view);
		broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
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
		String quoteRequestId = parameterHolder.getParameter("quoterequestid");

		if(quoteRequestId == null || quoteRequestId.isEmpty()) {
			clearView();
		}else{
			showQuoteRequest(quoteRequestId);
		}
	}

	public void bind() {
		if(bound)
			return;

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				QuoteRequestStub quoteRequest = (QuoteRequestStub) (selected == null ? null : selected.getValue());

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(quoteRequest == null) {
					item.removeParameter("quoterequestid");
				}else{
					item.setParameter("quoterequestid", quoteRequest.id);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<QuoteRequestSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case EDIT:
					onEdit();
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				case CLOSE:
					onClose();
					break;
				case CREATE_INSURED_OBJECT:
					break;
				case CREATE_PERSON_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PERSON);
					break;
				case CREATE_COMPANY_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_COMPANY);
					break;
				case CREATE_EQUIPMENT_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_EQUIPMENT);
					break;
				case CREATE_LOCATION_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PLACE);
					break;
				case CREATE_ANIMAL_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_ANIMAL);
					break;
				case CREATE_MANAGER_TRANSFER:
					onCreateManagerTransfer();
					break;
				case CREATE_INFO_OR_DOCUMENT_REQUEST:
					onCreateInfoOrDocumentRequest();
					break;
				default:
					break;
				}
			}
		});

		view.getFormAsFiresAsyncRequests().registerRequestHandler(new AsyncRequestHandler() {

			@Override
			public void onRequest(final AsyncRequest<Object> request) {
				String subLineId = request.getParameters().getParameter("subLineId");
				String operation = request.getParameters().getParameter("operation");

				if(operation.equalsIgnoreCase("getsublinedefinition")){
					broker.addSubLine(view.getForm().getValue().id, subLineId, new ResponseHandler<QuoteRequest.RequestSubLine>() {

						@Override
						public void onResponse(RequestSubLine response) {
							request.onResponse(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetSubLineDefinitionFailed();
							request.onError(null);
						}
					});
				}else if(operation.equalsIgnoreCase("deletesubline")){
					broker.deleteSubLine(subLineId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							request.onResponse(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							request.onError(null);
						}
					});
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getObjectsList()){
						QuoteRequestObjectStub object = (QuoteRequestObjectStub) selected.getValue();
						showObject(object.id);
					}else if(event.getSource() == view.getSubProcessesList()) {
						BigBangProcess process = (BigBangProcess) selected.getValue();
						showSubProcess(process);
					}else if(event.getSource() == view.getHistoryList()) {
						HistoryItemStub historyItem = (HistoryItemStub) selected.getValue();
						showHistory(historyItem.id);
					}
				}
			}
		};
		
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
					navItem.setParameter("ownerid", view.getForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
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
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		view.getObjectsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	protected void clearView(){
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	protected void showQuoteRequest(String quoteRequestId) {
		if(broker.isTemp(quoteRequestId)){
			showQuoteRequestInPad(quoteRequestId);
		}else{
			for(ValueSelectable<QuoteRequestStub> entry : view.getList().getAll()) {
				QuoteRequestStub quoteRequest = entry.getValue();
				if(quoteRequest.id.equalsIgnoreCase(quoteRequestId)){
					entry.setSelected(true, false);
				}else if(entry.isSelected()){
					entry.setSelected(false, false);
				}
			}
			broker.getQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {

				@Override
				public void onResponse(QuoteRequest response) {
					view.clearAllowedPermissions();
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);

					//TODO PERMISSIONS
					view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST));
					view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.DELETE_QUOTE_REQUEST));
					view.allowClose(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST));
					view.allowCreateInsuredObject(false);
					view.allowCreateManagerTransfer(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER));
					view.allowCreateInfoorDocumentRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_INFO_OR_DOCUMENT_REQUEST));

					view.getForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetQuoteRequestFailed();
				}
			});
		}
	}

	protected void showQuoteRequestInPad(final String requestId) {
		broker.getQuoteRequest(requestId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				view.clearAllowedPermissions();
				view.allowEdit(true);
				view.allowCreateInsuredObject(true);
				view.setSaveModeEnabled(true);

				//TODO
				view.getForm().setValue(response);
				view.getForm().setReadOnly(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				broker.discardTemp(requestId);
				onOpenRequestResourceFailed();
			}
		});
	}

	protected void onEdit() {
		broker.openRequestResource(view.getForm().getValue().id, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onOpenRequestResourceFailed();
			}
		});
	}

	protected void onSave() {
		broker.commitRequest(view.getForm().getInfo(), new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				onSaveSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveFailed();
			}
		});
	}

	protected void saveWorkState(){
		QuoteRequest request = view.getForm().getValue();
		if(request != null && broker.isTemp(request.id)) {
			broker.updateQuoteRequest(view.getForm().getInfo(), new ResponseHandler<QuoteRequest>() {

				@Override
				public void onResponse(QuoteRequest response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	protected void onCancel(){
		broker.closeRequestResource(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void onDelete(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "deleterequest");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onClose(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "closerequest");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onCreateInsuredObject(String objectType){
		saveWorkState();
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "viewinsuredobject");
		navItem.setParameter("objectid", "new");
		navItem.setParameter("type", objectType);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onCreateManagerTransfer(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "managertransfer");
		NavigationHistoryManager.getInstance().go(navItem);
	}
	
	protected void onCreateInfoOrDocumentRequest(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "inforequest");
		navItem.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(navItem);
	}
	
	protected void showObject(String objectId) {
		saveWorkState();
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "viewinsuredobject");
		navItem.setParameter("objectid", objectId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showSubProcess(BigBangProcess process){
		saveWorkState();
		String type = process.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
			showNegotiation(process.dataId);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)) {
			showInfoRequest(process.dataId);
		}
	}

	protected void showNegotiation(String negotiationId) {
		saveWorkState();
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("negotiation", negotiationId);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void showInfoRequest(String requestId) {
		saveWorkState();
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("viewinforequest", requestId);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void showHistory(String historyItemId){
		saveWorkState();
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", historyItemId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onGetQuoteRequestFailed(){
		saveWorkState();
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Apresentar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("quoterequestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Consulta de Mercado foi Guardada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onOpenRequestResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar a Consulta de Mercado"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onGetSubLineDefinitionFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível acrescentar a Modalidade à Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
	}

}
