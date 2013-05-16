package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.MedicalFile;
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
import bigBang.library.client.dataAccess.SubProcessesBroker;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;
import bigBang.module.casualtyModule.client.userInterface.view.MedicalFileView;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class MedicalFileViewPresenter implements ViewPresenter{

	public enum Action{
		SAVE,
		CANCEL,
		SEND_MESSAGE,
		RECEIVE_MESSAGE,
		CLOSE, EDIT, BACK
	}

	public interface Display{
		HasEditableValue<MedicalFile> getForm();
		HasValue<SubCasualty> getOwnerForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void allowEdit(boolean b);
		void setSaveMode(boolean b);
		void lockToolbar();
		void allowSendMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowClose(boolean hasPermission);
		void clear();
		void setOwner(String id);
		HasValueSelectables<BigBangProcess> getSubProcessList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		HasValueSelectables<ConversationStub> getConversationList();
	}

	Display view;
	private SubCasualtyDataBroker subCasualtyBroker;
	private MedicalFileBroker broker;
	private boolean bound;
	private String medicalFileId;
	private String subCasualtyId;
	protected MedicalFile medicalFile;

	public MedicalFileViewPresenter(MedicalFileView view) {
		setView((UIObject)view);
		this.subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;

	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());

	}

	private void bind() {
		if(bound){
			return;
		}
		view.registerActionHandler(new ActionInvokedEventHandler<MedicalFileViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CLOSE:
					onClose();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SAVE:
					if(view.getForm().validate()){
						onSave();
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem problemas no preenchimento do formulário."), TYPE.ERROR_TRAY_NOTIFICATION));
					}
					break;
				case SEND_MESSAGE:
					onSendMessage();
					break;
				case EDIT:
					onEdit();
					break;
				case BACK:
					onNavigateBack();
					break;
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selectable = (ValueSelectable<?>) event.getFirstSelected();

				if(selectable != null) {

					if(event.getSource() == view.getHistoryList()){ //HISTORY
						showHistory(((HistoryItemStub) selectable.getValue()).id);
					} else if(event.getSource() == view.getSubProcessList()){ //SUB PROCESSES
						showSubProcess(((BigBangProcess) selectable.getValue()).dataId);
					} else if(event.getSource() == view.getConversationList()){
						showConversation(((ConversationStub)selectable.getValue()).id);
					}
				}
			}
		};

		view.getSubProcessList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getConversationList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;		
	}

	protected void showConversation(String id) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "medicalfileconversation");
		item.setParameter("conversationid", id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void showSubProcess(String dataId) {
		SubProcessesBroker subProcessBroker = (SubProcessesBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.PROCESS);

		subProcessBroker.getSubProcess(dataId, new ResponseHandler<BigBangProcess>() {

			@Override
			public void onResponse(BigBangProcess response) {
				String type = response.dataTypeId;
				if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)) {
					showConversation(response.dataId);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				view.getSubProcessList().clearSelection();
			}
		});		
	}

	protected void showHistory(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", id);
		NavigationHistoryManager.getInstance().go(navItem);				
	}

	protected void onNavigateBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("medicalfileid");
		NavigationHistoryManager.getInstance().go(item);			
	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);

	}

	protected void onSendMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "medicalfilesendmessage");
		NavigationHistoryManager.getInstance().go(item);				
	}

	protected void onSave() {
		if(medicalFileId.equalsIgnoreCase("new")){
			subCasualtyBroker.createMedicalFile(view.getForm().getInfo(), new ResponseHandler<MedicalFile>() {

				@Override
				public void onResponse(MedicalFile response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ficha Clínica criada com sucesso."), TYPE.TRAY_NOTIFICATION));
					medicalFile = response;
					medicalFileId = response.id;
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("medicalfileid", medicalFileId);
					NavigationHistoryManager.getInstance().go(item);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Ficha Clínica."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		else{
			broker.editMedicalFile(view.getForm().getInfo(), new ResponseHandler<MedicalFile>() {

				@Override
				public void onResponse(MedicalFile response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Alterações gravadas com sucesso."), TYPE.TRAY_NOTIFICATION));
					medicalFile = response;
					medicalFileId = response.id;
					getMedicalFile();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}		
	}

	protected void onReceiveMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "medicalfilereceivemessage");
		item.setParameter("ownerid", item.getParameter("medicalfileid"));
		NavigationHistoryManager.getInstance().go(item);				

	}

	protected void onClose() {
		broker.closeProcess(medicalFileId, new ResponseHandler<MedicalFile>() {

			@Override
			public void onResponse(MedicalFile response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Processo fechado com sucesso."), TYPE.TRAY_NOTIFICATION));
				medicalFile = response;
				getMedicalFile();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível fechar o processo."), TYPE.ALERT_NOTIFICATION));
			}
		});		
	}

	protected void onCancel() {
		if(medicalFileId.equalsIgnoreCase("new")){
			onNavigateBack();
		}else{
			NavigationHistoryManager.getInstance().reload();
		}
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		medicalFileId = parameterHolder.getParameter("medicalfileid");
		subCasualtyId = parameterHolder.getParameter("subcasualtyid");
		view.clear();

		if(subCasualtyId != null){
			subCasualtyBroker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

				@Override
				public void onResponse(SubCasualty response) {
					view.getOwnerForm().setValue(response);
					getMedicalFile();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();

				}
			});
		}
		else{
			onGetOwnerFailed();
		}

	}

	protected void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o sub-sinistro."), TYPE.ALERT_NOTIFICATION));				
	}

	protected void getMedicalFile() {
		if(medicalFileId.equalsIgnoreCase("new")){
			view.lockToolbar();
			view.setSaveMode(true);
			view.allowEdit(true);
			view.getForm().setReadOnly(false);
			MedicalFile file = new MedicalFile();
			file.subCasualtyId = subCasualtyId;
			view.getForm().setValue(file);
		}

		else{
			broker.getMedicalFile(medicalFileId, new ResponseHandler<MedicalFile>() {

				@Override
				public void onResponse(MedicalFile response) {
					medicalFile = response;
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					view.setSaveMode(false);
					view.setOwner(response.id);
					setPermissions();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Ficha Clínica."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
	}

	protected void setPermissions() {
		view.lockToolbar();
		view.allowClose(PermissionChecker.hasPermission(medicalFile, BigBangConstants.OperationIds.MedicalFileProcess.CLOSE));
		view.allowReceiveMessage(PermissionChecker.hasPermission(medicalFile, BigBangConstants.OperationIds.MedicalFileProcess.CONVERSATION));
		view.allowSendMessage(PermissionChecker.hasPermission(medicalFile, BigBangConstants.OperationIds.MedicalFileProcess.CONVERSATION));
		view.allowEdit(PermissionChecker.hasPermission(medicalFile, BigBangConstants.OperationIds.MedicalFileProcess.EDIT));
	}

}
