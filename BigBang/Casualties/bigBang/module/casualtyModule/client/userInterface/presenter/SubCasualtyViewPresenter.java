package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SubCasualtyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE
	}
	
	public static interface Display {
		HasValue<Casualty> getParentForm();
		HasEditableValue<SubCasualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void setSaveModeEnabled(boolean enabled);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		
		Widget asWidget();
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
		
		String subCasualtyId = parameterHolder.getParameter("subcasualtyid");
		
		if(subCasualtyId != null && ! subCasualtyId.isEmpty()) {
			showSubCasualty(subCasualtyId);
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
				}
			}
		});
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
						
						//TODO PERMISSIONS
						view.allowEdit(PermissionChecker.hasPermission(casualty, BigBangConstants.OperationIds.SubCasualtyProcess.UPDATE_SUB_CASUALTY));
						view.allowDelete(PermissionChecker.hasPermission(casualty, BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY));
						
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
	
	protected void onEdit(){
		view.setSaveModeEnabled(true);
		view.getForm().setReadOnly(false);
	}
	
	protected void onSave() {
		broker.updateSubCasualty(view.getForm().getInfo(), new ResponseHandler<SubCasualty>() {

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
	
	protected void onCancel(){
		NavigationHistoryManager.getInstance().reload();
	}
	
	protected void onDelete(){
		//TODO
	}
	
	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Guardado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}
	
	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onGetSubCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
}
