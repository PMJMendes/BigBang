package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SubCasualty;
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
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubCasualtyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		MARK_FOR_CLOSING,
		CLOSE,
		REJECT_CLOSE,
		DELETE, SEND_MESSAGE, RECEIVE_MESSAGE, MARK_NOTIFICATION_SENT, BACK,
		CREATE_RECEIPT, CREATE_ASSESSMENT, CREATE_MEDICAL_FILE, CREATE_TOTAL_LOSSES
	}

	public static interface Display {
		HasValue<Casualty> getParentForm();
		HasEditableValue<SubCasualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();

		//PERMISSIONS
		void clearAllowedPermissions();
		void setSaveModeEnabled(boolean enabled);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowMarkForClosing(boolean allow);
		void allowClose(boolean allow);
		void allowRejectClose(boolean allow);
		void allowMarkNotificationSent(boolean allow);

		Widget asWidget();
		void allowSendMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowCreateMedicalFile(boolean hasPermission);
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<ConversationStub> getConversationList();
		HasValueSelectables<ReceiptStub> getReceiptsList();
		void setReferenceParameters(HasParameters parameterHolder);
		void openNewDetail();
		void openNewInsurerRequest();
		void allowCreateReceipt(boolean allow);
		void allowCreateAssessment(boolean allow);
		void allowCreateTotalLosses(boolean hasPermission);
	}


	protected boolean bound = false;
	protected Display view;
	protected SubCasualtyDataBroker broker;

	public SubCasualtyViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
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
		clearView();
		view.setReferenceParameters(parameterHolder);
		String subCasualtyId = parameterHolder.getParameter("subcasualtyid");
		String casualtyId = parameterHolder.getParameter("casualtyid");

		if(subCasualtyId != null && ! subCasualtyId.isEmpty()) {
			if(subCasualtyId.equalsIgnoreCase("new")) {
				showCreateSubCasualty(casualtyId);
			}else{
				showSubCasualty(subCasualtyId);
			}
		}else{
			onFailure();
		}
	}

	protected void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getParentForm().setValue(null);
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
	}

	protected void bind(){
		view.registerActionHandler(new ActionInvokedEventHandler<SubCasualtyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
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
				case MARK_FOR_CLOSING:
					onMarkForClosing();
					break;
				case CLOSE:
					onClose();
					break;
				case REJECT_CLOSE:
					onRejectClosing();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SEND_MESSAGE:
					onSendMessage();
					break;
				case MARK_NOTIFICATION_SENT:
					markNotificationSent(view.getForm().getInfo().id);
					break;
				case BACK:
					onBack(view.getParentForm().getValue().id);
					break;
				case CREATE_RECEIPT:
					onCreateReceipt();
					break;
				case CREATE_ASSESSMENT:
					onCreateAssessment();
					break;
				case CREATE_MEDICAL_FILE:
					onCreateMedicalFile();
					break;
				case CREATE_TOTAL_LOSSES:
					onCreateTotalLosses();
					break;
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getSubProcessesList()) {
						BigBangProcess process = (BigBangProcess) selected.getValue();
						showSubProcess(process);
					}else if(event.getSource() == view.getHistoryList()) {
						HistoryItemStub historyItem = (HistoryItemStub) selected.getValue();
						showHistory(historyItem.id);
					}else if(event.getSource() == view.getContactsList()){
						Contact contact = (Contact) selected.getValue();
						showContact(contact.id);
					}else if(event.getSource() == view.getDocumentsList()){
						Document doc = (Document) selected.getValue();
						showDocument(doc.id);
					}else if(event.getSource() == view.getConversationList()){
						ConversationStub stub = (ConversationStub) selected.getValue();
						showConversation(stub.id);
					}else if(event.getSource() == view.getReceiptsList()){
						ReceiptStub stub = (ReceiptStub) selected.getValue();
						showReceipt(stub.id);
					}
				}
			}
		};

		view.getContactsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getDocumentsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getConversationList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getReceiptsList().addSelectionChangedEventHandler(selectionChangedHandler);
	}

	protected void onCreateReceipt() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "subcasualtycreatereceipt");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateTotalLosses() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "totallossfile");
		navItem.setParameter("ownerid", view.getForm().getValue().id);
		navItem.setParameter("totallossfileid", "new");
		NavigationHistoryManager.getInstance().go(navItem);		
	}

	protected void onCreateMedicalFile() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "medicalfile");
		navItem.setParameter("ownerid", view.getForm().getValue().id);
		navItem.setParameter("medicalfileid", "new");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onCreateAssessment() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "assessment");
		navItem.setParameter("ownerid", view.getForm().getValue().id);
		navItem.setParameter("assessmentid", "new");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onBack(String id) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.removeParameter("ownerid");
		item.popFromStackParameter("display");
		item.setParameter("casualtyid", id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void markNotificationSent(String subCasualtyId) {

		broker.markNotificationSent(subCasualtyId, new ResponseHandler<SubCasualty>(){
			@Override
			public void onResponse(SubCasualty response) {
				onMarkNotificationSentSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onMarkNotificationSentFailed();
			}
		});
	}

	protected void onMarkNotificationSentFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Falhou envio da participação"), TYPE.TRAY_NOTIFICATION));

	}

	protected void onMarkNotificationSentSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Participação enviada"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();		
	}

	protected void showDocument(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "documentmanagement");
		navItem.setParameter("ownerid", navItem.getParameter("subcasualtyid"));
		navItem.setParameter("documentid", id);
		navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.SUB_CASUALTY);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showContact(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "contactmanagement");
		navItem.setParameter("ownerid", navItem.getParameter("subcasualtyid"));
		navItem.setParameter("contactid", id);
		navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.SUB_CASUALTY);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onSendMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "subcasualtysendmessage");
		item.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);		
	}

	protected void onReceiveMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "subcasualtyreceivemessage");
		item.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);			
	}

	protected void showSubCasualty(String subCasualtyId){
		broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(final SubCasualty subCasualty) {
				CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
				casualtyBroker.getCasualty(subCasualty.casualtyId, new ResponseHandler<Casualty>() {

					@Override
					public void onResponse(Casualty casualty) {
						view.getParentForm().setValue(casualty);
						view.getForm().setValue(subCasualty);
						view.allowSendMessage(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CONVERSATION));
						view.allowReceiveMessage(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CONVERSATION));
						view.allowEdit(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.UPDATE_SUB_CASUALTY));
						view.allowDelete(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY));
						view.allowMarkForClosing(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.MARK_CLOSE_SUB_CASUALTY));
						view.allowClose(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CLOSE_SUB_CASUALTY));
						view.allowRejectClose(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.REJECT_CLOSE_SUB_CASUALTY));
						view.allowMarkNotificationSent(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.MARK_NOTIFICATION_SENT));
						view.allowCreateReceipt(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_RECEIPT));
						view.allowCreateAssessment(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_ASSESSMENT));
						view.allowCreateMedicalFile(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_MEDICAL_FILE));
						view.allowCreateTotalLosses(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_TOTAL_LOSSES));
						view.getForm().setReadOnly(true);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onFailure();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetSubCasualtyFailed();
			}
		});
	}

	protected void showCreateSubCasualty(String parentId){
		CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
		casualtyBroker.getCasualty(parentId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty casualty) {
				view.getParentForm().setValue(casualty);

				SubCasualty subCasualty = new SubCasualty();
				subCasualty.casualtyId = casualty.id;

				view.getForm().setValue(subCasualty);
				view.openNewDetail();
				view.openNewInsurerRequest();
				view.allowEdit(true);
				view.allowDelete(true);
				view.getForm().setReadOnly(false);
				view.setSaveModeEnabled(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}

	protected void onEdit(){
		view.setSaveModeEnabled(true);
		view.getForm().setReadOnly(false);
	}

	protected void onSave() {
		if(view.getForm().validate()){
			SubCasualty subCasualty = view.getForm().getInfo();

			if(subCasualty.id == null) {
				CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
				casualtyBroker.createSubCasualty(subCasualty, new ResponseHandler<SubCasualty>(){

					@Override
					public void onResponse(SubCasualty response) {
						onCreateSubCasualtySuccess(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onCreateSubCasualtyFailed();
					}
				});
			}else{
				broker.updateSubCasualty(subCasualty, new ResponseHandler<SubCasualty>() {

					@Override
					public void onResponse(SubCasualty response) {
						onSaveSuccess();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onSaveFailed();
					}
				});
			}
		}else{
			onValidationFailed();
		}
	}

	private void onValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));				

	}

	
	protected void onCancel(){
		SubCasualty subCasualty = view.getForm().getValue();

		if(subCasualty.id == null) {
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.removeParameter("subcasualtyid");
			item.popFromStackParameter("display");
			NavigationHistoryManager.getInstance().go(item);
		}else{
			NavigationHistoryManager.getInstance().reload();
		}
	}

	protected void onDelete(){
		SubCasualty subCasualty = view.getForm().getValue();

		if(subCasualty.id == null) {
			onCancel();
		}else{
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.setParameter("show", "deletesubcasualty");
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	protected void onMarkForClosing(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "subcasualtymarkforclosing");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onClose(){
		broker.closeSubCasualty(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onCloseSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCloseFailed();
			}
		});
	}

	protected void onRejectClosing(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "subcasualtyrejectclose");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void showSubProcess(BigBangProcess process){
		String type = process.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			showConversation(process.dataId);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.ASSESSMENT)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "assessment");
			item.setParameter("assessmentid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MEDICAL_FILE)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "medicalfile");
			item.setParameter("medicalfileid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.TOTAL_LOSS_FILE)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "totallossfile");
			item.setParameter("totallossfileid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	private void showConversation(String dataId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "subcasualtyconversation");
		item.setParameter("conversationid", dataId);
		NavigationHistoryManager.getInstance().go(item);		
	}

	private void showReceipt(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("section", "receipt");
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display", "search"); 
		navItem.setParameter("receiptid", id);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showHistory(String historyItemId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", historyItemId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Guardado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onCreateSubCasualtySuccess(SubCasualty subCasualty){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Criado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("subcasualtyid", subCasualty.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateSubCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onGetSubCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Encerrado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onCloseFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
