package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.NewInsurancePolicyChildrenPanel;
import bigBang.module.clientModule.client.userInterface.NewInsurancePolicyOperationsToolbar;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter.Action;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateInsurancePolicyView extends View implements CreateInsurancePolicyViewPresenter.Display {

	protected ClientFormView clientForm;
	protected InsurancePolicyForm insurancePolicyForm;
	protected NewInsurancePolicyChildrenPanel childrenPanel;	
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected NewInsurancePolicyOperationsToolbar toolbar;
	
	public CreateInsurancePolicyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel clientFormWrapper = new VerticalPanel();
		wrapper.addWest(clientFormWrapper, 600);
		clientFormWrapper.setSize("100%", "100%");
		ListHeader clientHeader = new ListHeader("Cliente");
		clientFormWrapper.add(clientHeader);
		clientHeader.setHeight("30px");
		clientForm = new ClientFormView();
		clientFormWrapper.add(clientForm);
		clientFormWrapper.setCellHeight(clientForm, "100%");
		clientForm.setSize("100%", "100%");
		clientForm.setReadOnly(true);
		
		toolbar = new NewInsurancePolicyOperationsToolbar() {
			
			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.SAVE));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onCreateObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CREATE_OBJECT));
			}
			
			@Override
			public void onCreateExercise() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CREATE_EXERCISE));
			}
		};

		VerticalPanel newPolicyWrapper = new VerticalPanel();
		wrapper.add(newPolicyWrapper);
		newPolicyWrapper.setSize("100%", "100%");
		
		ListHeader insurancePolicyHeader = new ListHeader("Apólice");
		newPolicyWrapper.add(insurancePolicyHeader);
		insurancePolicyHeader.setHeight("30px");
		
		SplitLayoutPanel policyDataWrapper = new SplitLayoutPanel();
		policyDataWrapper.setSize("100%", "100%");
		newPolicyWrapper.add(policyDataWrapper);
		newPolicyWrapper.setCellHeight(policyDataWrapper, "100%");
		
		VerticalPanel policyFormWrapper = new VerticalPanel();
		policyFormWrapper.setSize("100%", "100%");
		
		this.childrenPanel = new NewInsurancePolicyChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		
		policyDataWrapper.addEast(this.childrenPanel, 250);
		policyDataWrapper.add(policyFormWrapper);
		
		insurancePolicyForm = new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.MODALITY_CHANGED));
			}
		};
		insurancePolicyForm.setSize("100%", "100%");
		insurancePolicyForm.setReadOnly(false);
		insurancePolicyForm.setForNew();

		policyFormWrapper.add(toolbar);
		policyFormWrapper.add(insurancePolicyForm);
		policyFormWrapper.setCellHeight(insurancePolicyForm, "100%");
	}
	
	@Override
	protected void initializeView() {}

	@Override
	public HasEditableValue<Client> getClientForm() {
		return this.clientForm;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getInsurancePolicyForm() {
		return this.insurancePolicyForm;
	}

	@Override
	public void setActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasValueSelectables<InsuredObjectStub> getInsuredObjectsList() {
		return this.childrenPanel.insuredObjectsList;
	}

	@Override
	public HasValueSelectables<ExerciseStub> getExercisesList() {
		return this.childrenPanel.exercisesList;
	}

	@Override
	public HasValue<TableSection> getTable() {
		return this.insurancePolicyForm.getTable();
	}

	@Override
	public HasValue<String> getInsuredObjectFilter() {
		return this.insurancePolicyForm.getInsuredObjectsField();
	}

	@Override
	public HasValue<String> getExerciseFilter() {
		return this.insurancePolicyForm.getExercisesField();
	}
	
	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}
	
}
