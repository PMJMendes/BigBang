package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseForm;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm_OLD;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter.Action;

public class ExerciseView extends View implements ExerciseViewPresenter.Display{
	
	private ExerciseForm exerciseForm;
	private InsurancePolicyForm_OLD policyForm;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ExerciseOperationsToolbar toolbar;	
	
	public ExerciseView(){
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		exerciseForm = new ExerciseForm();
		policyForm = new InsurancePolicyForm_OLD(){
			
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
		
		insurancePolicyFormHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.BACK));
			}
		}));
		
		policyForm.setSize("100%", "100%");
		policyForm.setReadOnly(true);
		insurancePolicyFormWrapper.add(policyForm);
		insurancePolicyFormWrapper.setCellHeight(policyForm, "100%");
		mainWrapper.addWest(insurancePolicyFormWrapper, 600);
		
		VerticalPanel exerciseFormWrapper = new VerticalPanel();
		exerciseFormWrapper.setSize("100%", "100%");
		
		ListHeader exerciseHeader = new ListHeader("Exercício");
		exerciseHeader.setHeight("30px");
		exerciseFormWrapper.add(exerciseHeader);
		
		toolbar = new ExerciseOperationsToolbar(){

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
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.CANCEL_EDIT));
			}
			
			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExerciseViewPresenter.Action>(Action.DELETE));
			}
		};
		
		exerciseForm.setSize("100%", "100%");
		exerciseFormWrapper.add(toolbar);
		exerciseFormWrapper.add(exerciseForm);
		exerciseFormWrapper.setCellHeight(exerciseForm, "100%");
		mainWrapper.add(exerciseFormWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
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
	public HasEditableValue<InsurancePolicy> getInsurancePolicyForm() {
		return this.policyForm;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.allowEdit(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

}
