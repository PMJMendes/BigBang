package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseForm;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter.Action;

public class ExerciseView extends View implements ExerciseViewPresenter.Display{
	
	ExerciseForm exerciseForm;
	InsurancePolicyForm policyForm;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public ExerciseView(){
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		exerciseForm = new ExerciseForm();
		policyForm = new InsurancePolicyForm(){
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
		
		VerticalPanel insurancePolicyFormWrapper = new VerticalPanel();
		insurancePolicyFormWrapper.setSize("100%", "100%");
				
		ListHeader insurancePolicyFormHeader = new ListHeader("Apólice");
		insurancePolicyFormHeader.setHeight("30px");
		insurancePolicyFormWrapper.add(insurancePolicyFormHeader);
		
		policyForm.setSize("100%", "100%");
		insurancePolicyFormWrapper.add(policyForm);
		insurancePolicyFormWrapper.setCellHeight(policyForm, "100%");
		mainWrapper.addWest(insurancePolicyFormWrapper, 600);
		
		VerticalPanel exerciseFormWrapper = new VerticalPanel();
		exerciseFormWrapper.setSize("100%", "100%");
		
		ListHeader exerciseHeader = new ListHeader("Exercício");
		exerciseHeader.setHeight("30px");
		exerciseFormWrapper.add(exerciseHeader);
		
		ExerciseOperationsToolbar toolbar = new ExerciseOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.EDIT));
				
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.SAVE));
				
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.CANCEL));
			}
			
			
			
			
			
		};
		
		exerciseForm.setSize("100%", "100%");
		exerciseFormWrapper.add(toolbar);
		exerciseFormWrapper.add(exerciseForm);
		exerciseFormWrapper.setCellHeight(exerciseForm, "100%");
		mainWrapper.add(exerciseFormWrapper);
		
		
		

	}
	
	@Override
	public void setExercise(Exercise exercise) {
		
		exerciseForm.setValue(exercise);
		
	}

	@Override
	public void setInsurancePolicy(InsurancePolicy policy) {
		policyForm.setValue(policy);
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		exerciseForm.setReadOnly(readOnly);
		
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
		
	}

	@Override
	public ExerciseForm getExerciseForm() {
		return exerciseForm;
	}

	@Override
	public void showError(String string) {
	}
	
	@Override
	public void showMessage(String string) {
		this.showMessage(string);
	}
	
	

}
