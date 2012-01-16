package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuredObjectViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuredObjectViewPresenter.Action;

public class InsuredObjectView extends View implements InsuredObjectViewPresenter.Display {

	protected InsurancePolicyForm insurancePolicyForm;
	protected InsuredObjectForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public InsuredObjectView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel insurancePolicyFormWrapper = new VerticalPanel();
		insurancePolicyFormWrapper.setSize("100%", "100%");
				
		ListHeader insurancePolicyFormHeader = new ListHeader("Apólice");
		insurancePolicyFormHeader.setHeight("30px");
		insurancePolicyFormWrapper.add(insurancePolicyFormHeader);
		
		insurancePolicyForm = new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
		insurancePolicyForm.setSize("100%", "100%");
		insurancePolicyFormWrapper.add(insurancePolicyForm);
		insurancePolicyFormWrapper.setCellHeight(insurancePolicyForm, "100%");
		mainWrapper.addWest(insurancePolicyFormWrapper, 600);
		
		VerticalPanel objectFormWrapper = new VerticalPanel();
		objectFormWrapper.setSize("100%", "100%");
		
		ListHeader objectHeader = new ListHeader("Unidade de Risco");
		objectHeader.setHeight("30px");
		objectFormWrapper.add(objectHeader);

		InsuredObjectOperationsToolbar toolbar = new InsuredObjectOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.DELETE));
			}
		};
		
		objectFormWrapper.add(toolbar);

		form = new InsuredObjectForm();
		form.setSize("100%", "100%");
		objectFormWrapper.add(form);
		objectFormWrapper.setCellHeight(form, "100%");
		mainWrapper.add(objectFormWrapper);

		insurancePolicyForm.setReadOnly(true);
	}
	
	@Override
	protected void initializeView() {}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectForm() {
		return this.form;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getInsurancePolicyForm() {
		return this.insurancePolicyForm;
	}

	@Override
	public void setInsurancePolicy(InsurancePolicy policy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInsuredObject(InsuredObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}
	
}
