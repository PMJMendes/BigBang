package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CreateInsurancePolicyViewPresenter implements ViewPresenter {

	public enum Action {
		SAVE,
		CANCEL,
		MODALITY_CHANGED,
		CREATE_OBJECT,
		CREATE_EXERCISE
	}

	public interface Display {
		HasEditableValue<Client> getClientForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();
		HasValueSelectables<InsuredObjectStub> getInsuredObjectsList();
		HasValueSelectables<ExerciseStub> getExercisesList();

		void setSaveModeEnabled(boolean enabled);

		public void setActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected boolean bound;
	protected Display view;
	protected ClientProcessBroker clientBroker;
	protected InsurancePolicyBroker policyBroker;
	protected String currentInsuredObjectFilterId, currentExerciseFilterId;

	public CreateInsurancePolicyViewPresenter(Display view){
		setView((UIObject) view);
		clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
		policyBroker = ((InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
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

		String clientId = parameterHolder.getParameter("clientid");
		String tempPolicyId = parameterHolder.getParameter("policyid");

		view.setSaveModeEnabled(true);

		if(clientId == null || clientId.isEmpty()) {
			onFailure();
		}else if(tempPolicyId != null && tempPolicyId.equalsIgnoreCase("new")){
			showCreatePolicy(clientId);
		}else if(tempPolicyId != null && !tempPolicyId.isEmpty()){
			showPolicy(clientId, tempPolicyId);
		}else{
			clearView();
			onFailure();
		}
	}

	public void bind() {
		if(bound){return;}

		view.setActionHandler(new ActionInvokedEventHandler<CreateInsurancePolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {

				case SAVE:
					onSave();
					break;

				case CANCEL:
					onCancel();
					break;

				case MODALITY_CHANGED:
					onModalityChanged();
					break;

				case CREATE_OBJECT:
					onCreateObject();
					break;

				case CREATE_EXERCISE:
					onCreateExercise();
					break;
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selectable = (ValueSelectable<?>) event.getFirstSelected();
				if(selectable != null){
					if(event.getSource() == view.getInsuredObjectsList()){
						InsuredObjectStub object = (InsuredObjectStub) selectable.getValue();
						showObject(object.id);
					}else if(event.getSource() == view.getExercisesList()){
						ExerciseStub exercise = (ExerciseStub) selectable.getValue();
						showExercise(exercise.id);
					}
				}
			}
		};
		view.getInsuredObjectsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getExercisesList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	private void clearView(){
		InsurancePolicy policy = view.getInsurancePolicyForm().getInfo();
		if(policy != null && policy.id != null && !policy.id.equalsIgnoreCase("new")) {
			policyBroker.discardTemp(policy.id);
		}
		view.getClientForm().setValue(null);
		view.getInsurancePolicyForm().setValue(null);
		view.setSaveModeEnabled(true);
	}

	private void onSave(){
		final InsurancePolicy policy = view.getInsurancePolicyForm().getInfo();
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				policyBroker.commitPolicy(policy, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy response) {
						onCreateSuccess();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onCreateFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateFailed();
			}
		});
	}

	private void onCancel(){
		InsurancePolicy policy = view.getInsurancePolicyForm().getValue();
		String policyId = policy == null ? null : policy.id;

		if(policyId != null && this.policyBroker.isTemp(policyId)){
			this.policyBroker.closePolicyResource(policyId, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("policyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onModalityChanged(){
		InsurancePolicy changedPolicy = view.getInsurancePolicyForm().getInfo();
//		InsurancePolicy valuePolicy = view.getInsurancePolicyForm().getValue();
//		if(valuePolicy.subLineId == null) {
			policyBroker.initPolicy(changedPolicy, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.getInsurancePolicyForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onFailure();
				}
			});
	}

	protected void saveWorkState(final ResponseHandler<Void> handler){
		final InsurancePolicy policy = view.getInsurancePolicyForm().getInfo(); 
		policyBroker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				handler.onResponse(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{});
			}
		});
	}

	private void onCreateObject(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "viewpolicyinsuredobject");
		item.setParameter("objectid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateExercise(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "viewexercise");
		item.setParameter("exerciseid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showCreatePolicy(String ownerId) {
		CreateInsurancePolicyViewPresenter.this.policyBroker.openPolicyResource(null, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("policyid", response.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}

	private void showPolicy(String ownerId, final String policyId){
		if(policyBroker.isTemp(policyId)) {
			clientBroker.getClient(ownerId, new ResponseHandler<Client>() {

				@Override
				public void onResponse(final Client client) {
					policyBroker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy policy) {
							view.getClientForm().setValue(client);
							policy.clientId = client.id;
							policy.clientNumber = client.clientNumber;
							policy.clientName = client.name;
							policy.managerId = Session.getUserId();
							view.getInsurancePolicyForm().setValue(policy);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onFailure();
						}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onFailure();
				}
			});
		}else{
			onFailure();
		}
	}

	private void showObject(String objectId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "viewpolicyinsuredobject");
		item.setParameter("objectid", objectId);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showExercise(String exerciseId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "viewexercise");
		item.setParameter("exerciseid", exerciseId);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Apólice"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("policyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Apólice foi criada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("policyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Apólice"), TYPE.ALERT_NOTIFICATION));
	}

}
