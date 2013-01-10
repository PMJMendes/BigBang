package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;

public class MedicalFileTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action{
		EDIT, GO_TO_PROCESS, CANCEL, SAVE
	}

	public static interface Display {
		HasEditableValue<MedicalFile> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void clearAllowedPermissions();
		void allowEdit(boolean b);
	}

	protected boolean bound = false;
	protected MedicalFileBroker broker;
	protected Display view;


	public MedicalFileTasksViewPresenter(Display view) {
		setView((UIObject) view);
		broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
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

	private void bind() {
		if(bound) {return;}		

		view.registerActionHandler(new ActionInvokedEventHandler<MedicalFileTasksViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case EDIT:
					onEdit();
					break;
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.MEDICAL_FILE, view.getForm().getValue().id);
					break;
				case SAVE:
					onSave();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onSave() {
		if(view.getForm().validate()){
			broker.editMedicalFile(view.getForm().getInfo(), new ResponseHandler<MedicalFile>() {
				
				@Override
				public void onResponse(MedicalFile response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ficha Clínica gravada com êxito."), TYPE.TRAY_NOTIFICATION));
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações à Ficha Clínica"), TYPE.ERROR_NOTIFICATION));
				}
			});
		}
		else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Problemas no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);
	}

	protected void onCancel() {
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		String medicalFileId = parameterHolder.getParameter("id");
		showMedicalFile(medicalFileId);
	}

	private void showMedicalFile(String medicalFileId) {
		broker.getMedicalFile(medicalFileId, new ResponseHandler<MedicalFile>() {
			
			@Override
			public void onResponse(MedicalFile response) {
				view.getForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Ficha Clínica"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	private void clearView() {
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		view.clearAllowedPermissions();
		for(String opid : operationIds){
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.MedicalFileProcess.EDIT)){
				view.allowEdit(true);
			}
		}
	}

}
