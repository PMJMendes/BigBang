package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.OperationsToolBar;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter.Action;

public class CreateInsurancePolicyView extends View implements CreateInsurancePolicyViewPresenter.Display {

	protected ClientFormView clientForm;
	protected FormView<InsurancePolicy> insurancePolicyForm;
	
	protected ActionInvokedEventHandler<Action> actionHandler;
	
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
		
		OperationsToolBar toolbar = new OperationsToolBar();
		MenuItem createItem = new MenuItem("Guardar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CREATE_POLICY));
			}
		});
		toolbar.addItem(createItem);

		VerticalPanel insurancePolicyWrapper = new VerticalPanel();
		wrapper.add(insurancePolicyWrapper);
		ListHeader insurancePolicyHeader = new ListHeader("Apólice");
		insurancePolicyWrapper.add(insurancePolicyHeader);
		insurancePolicyHeader.setHeight("30px");
		insurancePolicyWrapper.setSize("100%", "100%");
		
		insurancePolicyForm = new FormView<InsurancePolicy>() {
			
			@Override
			public void setInfo(InsurancePolicy info) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public InsurancePolicy getInfo() {
				// TODO Auto-generated method stub
				return null;
			}
		};
//		insurancePolicyForm = new InsurancePolicyForm(){
//			@Override
//			public void onSubLineChanged(String subLineId) {
//				if(actionHandler != null){
//					actionHandler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.MODALITY_CHANGED));
//				}
//			}
//		};
//		insurancePolicyForm.setSize("100%", "100%");
//		insurancePolicyForm.setReadOnly(false);
//		insurancePolicyForm.setForNew();
		insurancePolicyWrapper.add(toolbar);
		insurancePolicyWrapper.add(insurancePolicyForm);
		insurancePolicyWrapper.setCellHeight(insurancePolicyForm, "100%");
	}
	
	@Override
	protected void initializeView() {}

	@Override
	public TableSection getCurrentTableSection(){
		return null; //this.insurancePolicyForm.getTable().getData();
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

	@Override
	public String getInsuredObjectFilter() {
		return null; // this.insurancePolicyForm.getTable().getInsuredObjectFilterValue();
	}

	@Override
	public String getExerciseFilter() {
		return null; // this.insurancePolicyForm.getTable().getExerciseFilterValue();
	}
	
}
