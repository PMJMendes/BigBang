package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Policy2;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExerciseViewPresenter implements ViewPresenter{

	public static interface Display{
		HasEditableValue<Exercise> getExerciseForm();
		HasEditableValue<Policy2> getInsurancePolicyForm();

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
	private ExerciseDataBroker broker;
	private InsurancePolicyBroker policyBroker;
	protected String ownerId;

	public ExerciseViewPresenter(Display display){
		broker = (ExerciseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
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
		String exerciseId = parameterHolder.getParameter("exerciseid");
		exerciseId = exerciseId == null ? new String() : exerciseId;

		if(ownerId.isEmpty()){
			clearView();
			onGetOwnerFailed();
		}else if(exerciseId.isEmpty()){
			clearView();
			onGetExerciseFailed();
		}else if(exerciseId.equalsIgnoreCase("new")){
			showCreateExercise(this.ownerId);
		}else{
			showExercise(exerciseId);
		}
	}

	public void bind() {
		if(bound){return;}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					if(view.getExerciseForm().validate()) {
						Exercise value = view.getExerciseForm().getInfo();
						saveExercise(value);
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
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void clearView(){
		view.clearAllowedPermissions();
		view.setSaveModeEnabled(false);
		view.getInsurancePolicyForm().setValue(null);
		view.getInsurancePolicyForm().setReadOnly(true);
		view.getExerciseForm().setValue(null);
		view.getExerciseForm().setReadOnly(true);
	}

	private void onEdit(){
		String exerciseId = view.getExerciseForm().getValue().id;
		broker.getExercise(exerciseId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(final Exercise exercise) {
				policyBroker.getPolicy(exercise.ownerId, new ResponseHandler<Policy2>() {

					@Override
					public void onResponse(Policy2 policy) {
						if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) || policyBroker.isTemp(policy.id)){
							if(!policyBroker.isTemp(policy.id)){
								policyBroker.openPolicyResource(policy.id, new ResponseHandler<Policy2>() { //TODO

									@Override
									public void onResponse(
											Policy2 response) {
										broker.getExercise(exercise.id, new ResponseHandler<Exercise>() {

											@Override
											public void onResponse(
													Exercise response) {
												view.getExerciseForm().setValue(response);
												view.allowEdit(true);
												view.allowDelete(true);
												view.setSaveModeEnabled(true);
												view.getExerciseForm().setReadOnly(false);
											}

											@Override
											public void onError(
													Collection<ResponseError> errors) {
												onGetExerciseFailed();
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
								broker.getExercise(exercise.id, new ResponseHandler<Exercise>() {

									@Override
									public void onResponse(
											Exercise response) {
										view.getExerciseForm().setValue(response);
										view.allowEdit(true);
										view.allowDelete(true);
										view.setSaveModeEnabled(true);
										view.getExerciseForm().setReadOnly(false);
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onGetExerciseFailed();
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
				onGetExerciseFailed();
			}
		});
	}

	private void onDelete(){
		final String objectId = view.getExerciseForm().getValue().id;
		broker.getExercise(objectId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(final Exercise object) {
				policyBroker.getPolicy(object.ownerId, new ResponseHandler<Policy2>() {

					@Override
					public void onResponse(Policy2 policy) {
						if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) || policyBroker.isTemp(policy.id)){
							if(!policyBroker.isTemp(policy.id)){
								policyBroker.openPolicyResource(policy.id, new ResponseHandler<Policy2>() { //TODO

									@Override
									public void onResponse(
											Policy2 response) {
										broker.deleteExercise(objectId, new ResponseHandler<Void>() {

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
								broker.deleteExercise(objectId, new ResponseHandler<Void>() {

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
				onGetExerciseFailed();
			}
		});
	}

	private void showExercise(String exerciseId){
		broker.getExercise(exerciseId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(final Exercise object) {
				policyBroker.getPolicy(object.ownerId, new ResponseHandler<Policy2>() {

					@Override
					public void onResponse(Policy2 policy) {
						view.getExerciseForm().setReadOnly(true);
						view.setSaveModeEnabled(false);
						boolean hasPermissions = PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY) ||
								policyBroker.isTemp(policy.id);
						view.allowEdit(hasPermissions);
						view.allowDelete(hasPermissions);
						view.getInsurancePolicyForm().setValue(policy);
						view.getExerciseForm().setValue(object);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetExerciseFailed();
			}
		});
	}

	private void showCreateExercise(String ownerId) {
		policyBroker.getPolicy(ownerId, new ResponseHandler<Policy2>() {

			@Override
			public void onResponse(final Policy2 policy) {
				boolean hasPermissions = true; //PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_EXERCISE);

				if(hasPermissions){
					if(policyBroker.isTemp(policy.id)){
						broker.createExercise(policy.id, new ResponseHandler<Exercise>() {

							@Override
							public void onResponse(Exercise exercise) {
								view.getExerciseForm().setReadOnly(false);
								view.allowEdit(true);
								view.allowDelete(true);
								view.setSaveModeEnabled(true);
								view.getInsurancePolicyForm().setValue(policy);
								view.getExerciseForm().setValue(exercise);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onCreateExerciseFailed();
							}
						});
					}else{
						policyBroker.openPolicyResource(policy.id, new ResponseHandler<Policy2>() {

							@Override
							public void onResponse(Policy2 tempPolicy) {
								broker.createExercise(tempPolicy.id, new ResponseHandler<Exercise>() {

									@Override
									public void onResponse(Exercise object) {
										view.getExerciseForm().setReadOnly(false);
										view.allowEdit(true);
										view.allowDelete(true);
										view.setSaveModeEnabled(true);
										view.getInsurancePolicyForm().setValue(policy);
										view.getExerciseForm().setValue(object);
									}

									@Override
									public void onError(Collection<ResponseError> errors) {
										onCreateExerciseFailed();
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

	protected void saveExercise(Exercise exercise) {
		broker.updateExercise(exercise, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise response) {
				onSaveExerciseSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveExerciseFailed();
			}
		});
	}

	private void onBack(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveExerciseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Exercício guardado no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice a que pertence o Exercício"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Exercício"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o Exercício no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o Exercício no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksCreatePermission(){
		NavigationHistoryManager.getInstance().reload();
	}

	private void onOpenResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice, assim como os respectivos Exercícios"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksEditPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar apólice. Não tem as permissões necessárias"), TYPE.ALERT_NOTIFICATION));
	}

	private void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Exercício foi eliminado no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("exerciseid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o Exercício"), TYPE.ALERT_NOTIFICATION));
	}

}
