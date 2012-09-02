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
		DELETE,
		BACK
	}

	protected Display view;
	protected boolean bound = false;
	protected InsuredObjectDataBroker broker;
	protected InsurancePolicyBroker policyBroker;
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
		this.ownerId = parameterHolder.getParameter("policyid");
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
					if(view.getInsuredObjectForm().validate()) {
						InsuredObject value = view.getInsuredObjectForm().getInfo();
						saveObject(value);
					}
					break;
				case EDIT:
					onEdit();
					break;
				case CANCEL_EDIT:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				case BACK:
					onBack();
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);

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
						if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) || policyBroker.isTemp(policy.id)){
							if(!policyBroker.isTemp(policy.id)){
								policyBroker.openPolicyResource(policy.id, new ResponseHandler<InsurancePolicy>() { //TODO

									@Override
									public void onResponse(
											InsurancePolicy response) {
										broker.getInsuredObject(object.id, new ResponseHandler<InsuredObject>() {

											@Override
											public void onResponse(
													InsuredObject response) {
												view.getInsuredObjectForm().setValue(response);
												view.setSaveModeEnabled(true);
												view.getInsuredObjectForm().setReadOnly(false);
												view.allowEdit(true);
												view.allowDelete(true);
											}

											@Override
											public void onError(
													Collection<ResponseError> errors) {
												onGetObjectFailed();
											}
										});
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onOpenResourceFailed();
									}
								});
							}else{
								broker.getInsuredObject(object.id, new ResponseHandler<InsuredObject>() {

									@Override
									public void onResponse(
											InsuredObject response) {
										view.getInsuredObjectForm().setValue(response);
										view.setSaveModeEnabled(true);
										view.getInsuredObjectForm().setReadOnly(false);
										view.allowEdit(true);
										view.allowDelete(true);
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onGetObjectFailed();
									}
								});
							}
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

	private void onDelete(){
		final String objectId = view.getInsuredObjectForm().getValue().id;
		broker.getInsuredObject(objectId, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(final InsuredObject object) {
				policyBroker.getPolicy(object.ownerId, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy policy) {
						if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) || policyBroker.isTemp(policy.id)){
							if(!policyBroker.isTemp(policy.id)){
								policyBroker.openPolicyResource(policy.id, new ResponseHandler<InsurancePolicy>() { //TODO

									@Override
									public void onResponse(
											InsurancePolicy response) {
										broker.deleteInsuredObject(objectId, new ResponseHandler<Void>() {

											@Override
											public void onResponse(Void response) {
												onDeleteSuccess();
											}

											@Override
											public void onError(
													Collection<ResponseError> errors) {
												onDeleteFailed();
											}
										});
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onOpenResourceFailed();
									}
								});
							}else{
								broker.deleteInsuredObject(objectId, new ResponseHandler<Void>() {

									@Override
									public void onResponse(Void response) {
										onDeleteSuccess();
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onDeleteFailed();
									}
								});
							}
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
						boolean hasPermissions = PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) ||
								policyBroker.isTemp(policy.id);
						view.allowEdit(hasPermissions);
						view.allowDelete(hasPermissions);
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

	private void showCreateObject(final String ownerId) {
		policyBroker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(final InsurancePolicy policy) {
				boolean hasPermissions = true; //PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.INCLUDE_INSURED_OBJECT);

				if(hasPermissions){
					if(policyBroker.isTemp(ownerId)){
						broker.createInsuredObject(ownerId, new ResponseHandler<InsuredObject>() {

							@Override
							public void onResponse(InsuredObject object) {
								view.getInsuredObjectForm().setReadOnly(false);
								view.setSaveModeEnabled(true);
								view.allowEdit(true);
								view.allowDelete(true);
								view.getInsurancePolicyForm().setValue(policy);
								view.getInsuredObjectForm().setValue(object);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onCreateObjectFailed();
							}
						});
					}else{
						policyBroker.openPolicyResource(ownerId, new ResponseHandler<InsurancePolicy>() {

							@Override
							public void onResponse(InsurancePolicy tempPolicy) {
								broker.createInsuredObject(tempPolicy.id, new ResponseHandler<InsuredObject>() {

									@Override
									public void onResponse(InsuredObject object) {
										view.getInsuredObjectForm().setReadOnly(false);
										view.setSaveModeEnabled(true);
										view.allowEdit(true);
										view.allowDelete(true);
										view.getInsurancePolicyForm().setValue(policy);
										view.getInsuredObjectForm().setValue(object);
									}

									@Override
									public void onError(Collection<ResponseError> errors) {
										onCreateObjectFailed();
									}
								});
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onOpenResourceFailed();
							}
						});
					}
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

	private void onBack(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveObjectSucces(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Unidade de Risco guardada no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice a que pertence a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Unidade de Risco no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a Unidade de Risco no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksCreatePermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onOpenResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice, assim como as respectivas unidades de risco"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksEditPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice. Não tem as permissões necessárias"), TYPE.ALERT_NOTIFICATION));
	}

	private void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Unidade de Risco foi eliminada no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
	}

}
