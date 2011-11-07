package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.OperationsToolBar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter.Action;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

public class CreateInsurancePolicyView extends View implements CreateInsurancePolicyViewPresenter.Display {

	protected ClientFormView clientForm;
	protected InsurancePolicyForm insurancePolicyForm;
	
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public CreateInsurancePolicyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		clientForm = new ClientFormView();
		clientForm.setSize("100%", "100%");
		clientForm.setReadOnly(true);
		insurancePolicyForm = new InsurancePolicyForm();
		insurancePolicyForm.setSize("100%", "100%");
		insurancePolicyForm.setReadOnly(false);
		insurancePolicyForm.allowManagerEdition(true);
		
		VerticalPanel insurancePolicyWrapper = new VerticalPanel();
		insurancePolicyWrapper.setSize("100%", "100%");
		
		OperationsToolBar toolbar = new OperationsToolBar();
		MenuItem createItem = new MenuItem("Guardar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CREATE_POLICY));
			}
		});
		toolbar.addItem(createItem);
		
		insurancePolicyWrapper.add(toolbar);
		insurancePolicyWrapper.add(insurancePolicyForm);
		insurancePolicyWrapper.setCellHeight(insurancePolicyForm, "100%");

		wrapper.addWest(clientForm, 600);
		wrapper.add(insurancePolicyWrapper);

		initWidget(wrapper);
	}
	
	@Override
	public boolean isInsurancePolicyFormValid() {
		return this.insurancePolicyForm.validate();
	}

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
	
}
