package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InsuredObjectViewPresenter implements ViewPresenter {

	public static interface Display{
		HasEditableValue<InsuredObject> getInsuredObjectForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();

		//PERMISSIONS
		void setSaveModeEnabled(boolean enabled);
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		Widget asWidget();
	}

	public static enum Action{
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	protected Display view;
	protected boolean bound = false;
	private InsuredObjectDataBroker broker;
	private InsurancePolicyBroker policyBroker;
	protected String ownerId;

	public InsuredObjectViewPresenter(Display display){
		broker = (InsuredObjectDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT);
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject) display);		
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.ownerId = parameterHolder.getParameter("id");
		ownerId = ownerId == null ? new String() : ownerId;
		String objectId = parameterHolder.getParameter("objectid");
		objectId = objectId == null ? new String() : objectId;

		if(ownerId.isEmpty()){
			clearView();
			onGetOwnerFailed();
		}else if(objectId.isEmpty()){
			clearView();
			onGetObjectFailed();
		}else if(objectId.equalsIgnoreCase("new")){
			showCreateObject(this.ownerId);
		}else{
			showObject(objectId);
		}
	}

	public void bind() {
		if(bound){return;}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					InsuredObject value = view.getInsuredObjectForm().getInfo();
					saveObject(value);
					break;
				case EDIT:
					onEdit();
					break;
				case CANCEL_EDIT:
					NavigationHistoryManager.Util.getInstance().reload();
					break;
				case DELETE:
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	private void clearView(){
		view.clearAllowedPermissions();
		view.setSaveModeEnabled(false);
		view.getInsurancePolicyForm().setValue(null);
		view.getInsurancePolicyForm().setReadOnly(true);
		view.getInsuredObjectForm().setValue(null);
		view.getInsuredObjectForm().setReadOnly(true);
	}

	private void onEdit(){
		String objectId = view.getInsuredObjectForm().getValue().id;
		broker.getInsuredObject(objectId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(final InsuredObject object) {
				policyBroker.getPolicy(object.ownerId, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy policy) {
						if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY)){
//							if(!policyBroker.isTemp(policy)){
////								policyBroker.openPolicyResource(policy, new ResponseHandler<InsurancePolicy>() { //TODO
////
////									@Override
////									public void onResponse(
////											InsurancePolicy response) {
////										view.setSaveModeEnabled(true);
////										view.getInsuredObjectForm().setReadOnly(false);
////										view.allowEdit(true);
////										view.allowDelete(true);
////									}
////
////									@Override
////									public void onError(
////											Collection<ResponseError> errors) {
////										onOpenResourceFailed();
////									}
////								});
//							}else{
//								view.setSaveModeEnabled(true);
//								view.getInsuredObjectForm().setReadOnly(false);
//								view.allowEdit(true);
//								view.allowDelete(true);
//							}
						}else {
							onUserLacksEditPermission();
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetObjectFailed();
			}
		});
	}

	private void showObject(String objectId){
		broker.getInsuredObject(objectId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(final InsuredObject object) {
				policyBroker.getPolicy(object.ownerId, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy policy) {
						view.getInsuredObjectForm().setReadOnly(true);
						view.setSaveModeEnabled(false);
						view.allowEdit(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY));
						view.allowDelete(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY));
						view.getInsurancePolicyForm().setValue(policy);
						view.getInsuredObjectForm().setValue(object);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetObjectFailed();
			}
		});
	}

	private void showCreateObject(String ownerId) {
		policyBroker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				boolean hasPermissions = PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY);

				if(hasPermissions){
					broker.createInsuredObject(null, new ResponseHandler<InsuredObject>() {

						@Override
						public void onResponse(InsuredObject response) {
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.setParameter("objectid", response.id);
							NavigationHistoryManager.getInstance().go(item);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onCreateObjectFailed();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.removeParameter("id");
							NavigationHistoryManager.getInstance().go(item);
						}
					});
				}else{
					onUserLacksCreatePermission();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected void saveObject(InsuredObject object) {
		broker.updateInsuredObject(object, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				onSaveObjectSucces();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveObjectFailed();
			}
		});
	}

	private void onSaveObjectSucces(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Unidade de Risco guardada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice a que pertence a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("operation");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("operation");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
	}

	private void onSaveObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksCreatePermission(){
		NavigationHistoryManager.getInstance().reload();
	}

	private void onOpenResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice, assim como as respectivas unidades de risco"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksEditPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice. Não tem as permissões necessárias"), TYPE.ALERT_NOTIFICATION));
	}

}
