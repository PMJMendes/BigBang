package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Policy2;
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
	private InsuredObjectOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public InsuredObjectView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel insurancePolicyFormWrapper = new VerticalPanel();
		insurancePolicyFormWrapper.setSize("100%", "100%");
				
		ListHeader insurancePolicyFormHeader = new ListHeader("Apólice");
		insurancePolicyFormHeader.setHeight("30px");
		insurancePolicyFormHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.BACK));
			}
		}));
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

		this.toolbar = new InsuredObjectOperationsToolbar(){

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
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.CANCEL_EDIT));
			}

			@Override
			public void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuredObjectViewPresenter.Action>(Action.DELETE));
			}
		};
		
		objectFormWrapper.add(toolbar);

		form = new InsuredObjectForm();
		form.setSize("100%", "100%");
		objectFormWrapper.add(form);
		form.showTypeSection(false);
		objectFormWrapper.setCellHeight(form, "100%");
		mainWrapper.add(objectFormWrapper);

		insurancePolicyForm.setReadOnly(true);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectForm() {
		return this.form;
	}

	@Override
	public HasEditableValue<Policy2> getInsurancePolicyForm() {
		return this.insurancePolicyForm;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void allowEdit(boolean allow) {
		toolbar.allowEdit(allow);
	}
	
}
