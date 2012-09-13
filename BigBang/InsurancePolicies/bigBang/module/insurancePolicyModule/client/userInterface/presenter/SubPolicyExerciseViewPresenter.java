package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubPolicyExerciseDataBroker;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubPolicyExerciseViewPresenter implements ViewPresenter {

	public static interface Display{
		HasEditableValue<Exercise> getExerciseForm();
		HasEditableValue<SubPolicy> getSubPolicyForm();

		Widget asWidget();
		HasClickHandlers getBackButton();
	}
	
	protected Display view;
	protected boolean bound = false;
	private SubPolicyExerciseDataBroker broker;
	private InsuranceSubPolicyBroker subPolicyBroker;

	public SubPolicyExerciseViewPresenter(Display display){
		broker = (SubPolicyExerciseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
		subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
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

	private void bind() {
		if(bound){
			return;
		}
		
		view.getBackButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.popFromStackParameter("display");
				item.removeParameter("exerciseid");
				NavigationHistoryManager.getInstance().go(item);
			}
		});
		
		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String ownerId = parameterHolder.getParameter("subpolicyid");
		ownerId = ownerId == null ? new String() : ownerId;
		String exerciseId = parameterHolder.getParameter("exerciseid");
		exerciseId = exerciseId == null ? new String() : exerciseId;

		if(ownerId.isEmpty()){
			clearView();
			onGetOwnerFailed();
		}else if(exerciseId.isEmpty()){
			clearView();
			onGetExerciseFailed();
		}
		else{
			showExercise(exerciseId, ownerId);
		}
	}

	private void clearView(){
		view.getSubPolicyForm().setValue(null);
		view.getExerciseForm().setValue(null);
	}

	private void showExercise(String exerciseId, final String parentId){
		broker.getSubPolicyExercise(exerciseId, parentId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise exercise) {
				view.getExerciseForm().setValue(exercise);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetExerciseFailed();
			}
		});
		showParentSubPolicy(parentId);
	}

	private void showParentSubPolicy(String subPolicyId){
		subPolicyBroker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				view.getSubPolicyForm().setValue(response);				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();				
			}
		});
		
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

}
