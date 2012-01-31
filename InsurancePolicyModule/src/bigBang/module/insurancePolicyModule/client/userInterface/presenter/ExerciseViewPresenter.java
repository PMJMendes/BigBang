package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
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

	public static enum Action{
		EDIT,
		SAVE,
		CANCEL,
		DELETE
	}

	public static interface Display{
		HasEditableValue<Exercise> getExerciseForm();
		HasEditableValue<InsurancePolicy> getPolicyForm();

		void clearAllowedPermissions();
		void setSaveModeEnabled(boolean enabled);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		Widget asWidget();		
	}

	protected Display view;
	private ExerciseDataBroker broker;
	private InsurancePolicyBroker policyBroker;
	protected boolean bound = false;

	public ExerciseViewPresenter(Display view){
		this.policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		this.broker = (ExerciseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
		setView((UIObject) view);
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
		String ownerId = parameterHolder.getParameter("id");
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
			showCreateExercise(ownerId);
		}else{
			showExercise(exerciseId);
		}
	}

	private void bind() {
		if(bound){
			return;
		}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				Exercise value = view.getExerciseForm().getInfo();
				switch(action.getAction()){
				case SAVE:
					saveExercise(value);
					break;
				case EDIT:{
					view.getExerciseForm().setReadOnly(false);
					break;}
				case CANCEL:
					NavigationHistoryManager.getInstance().reload();
					break;
				case DELETE:
					deleteExercise(value.id);
					break;
				}
			}
		});

		bound = true;
	}

	private void clearView(){
		view.getPolicyForm().setValue(null);
		view.getExerciseForm().setValue(null);
		view.getPolicyForm().setReadOnly(true);
		view.getExerciseForm().setReadOnly(true);
		view.clearAllowedPermissions();
		view.setSaveModeEnabled(false);
	}

	private void showExercise(String exerciseId){
		broker.getExercise(exerciseId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(final Exercise exercise) {
				policyBroker.getPolicy(exercise.ownerId, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy policy) {
						view.getPolicyForm().setValue(policy);
						view.getPolicyForm().setReadOnly(true);
						view.getExerciseForm().setValue(exercise);
						view.getExerciseForm().setReadOnly(true);

						boolean hasPermission = PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY);
						view.allowEdit(hasPermission);
						view.allowDelete(hasPermission);
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

	private void showCreateExercise(String ownerId){
		policyBroker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy policy) {
				if(PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY)) {
					broker.createExercise(policy.id, new ResponseHandler<Exercise>() {

						@Override
						public void onResponse(Exercise exercise) {
							view.getExerciseForm().setValue(exercise);
							view.getExerciseForm().setReadOnly(false);
							view.allowEdit(true);
							view.allowDelete(true);
							view.setSaveModeEnabled(true);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onCreateExerciseFailed();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.removeParameter("exerciseid");
							item.removeParameter("operation");
							NavigationHistoryManager.getInstance().go(item);
						}
					});
				}else{
					onUserLacksEditPermission();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("exerciseid");
				item.removeParameter("operation");
				NavigationHistoryManager.getInstance().go(item);
			}
		});
	}

	private void saveExercise(Exercise exercise) {
		broker.updateExercise(exercise, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise response) {
				NavigationHistoryItem item = NavigationHistoryManager.Util.getInstance().getCurrentState();
				item.setParameter("id", response.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveExerciseFailed();
			}
		});
	}

	protected void deleteExercise(String exerciseId) {
		broker.deleteExercise(exerciseId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onDeleteExerciseSuccess();
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("exerciseid");
				item.removeParameter("operation");
				NavigationHistoryManager.Util.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDeleteExerciseFailed();
			}
		});
	}
	
	private void onCreateExerciseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Exercício foi criado com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onSaveExerciseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Exercício foi guardado com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onDeleteExerciseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Exercício foi eliminado com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onGetExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCreateExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o novo Exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onSaveExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o Exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onDeleteExerciseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o Exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksEditPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não dispõe de permissões para editar o exercício"), TYPE.ALERT_NOTIFICATION));
	}

	private void onResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível editar o exercício"), TYPE.ALERT_NOTIFICATION));
	}

}
