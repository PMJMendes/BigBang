package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyInsuredObjectViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyInsuredObjectViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubPolicyInsuredObjectView extends View implements SubPolicyInsuredObjectViewPresenter.Display {

	private FormView<SubPolicy> subPolicyForm;
	private InsuredObjectForm objectForm;
	private InsuredObjectOperationsToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public SubPolicyInsuredObjectView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel subPolicyWrapper = new VerticalPanel();
		subPolicyWrapper.setSize("100%", "100%");
		ListHeader subPolicyHeader = new ListHeader("Apólice Adesão");
		subPolicyHeader.setHeight("30px");
		subPolicyWrapper.add(subPolicyHeader);
		
		subPolicyHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyInsuredObjectViewPresenter.Action>(Action.BACK));
			}
		}));
		
		subPolicyForm = new SubPolicyForm();
		subPolicyForm.setReadOnly(true);
		subPolicyWrapper.add(subPolicyForm);
		subPolicyWrapper.setCellHeight(subPolicyForm, "100%");
		wrapper.addWest(subPolicyWrapper, 600);
		
		VerticalPanel objectWrapper = new VerticalPanel();
		objectWrapper.setSize("100%", "100%");
		ListHeader objectHeader = new ListHeader("Unidade de Risco");
		objectHeader.setHeight("30px");
		objectWrapper.add(objectHeader);

		toolbar = new InsuredObjectOperationsToolbar() {
			
			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyInsuredObjectViewPresenter.Action>(Action.SAVE));
			}
			
			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyInsuredObjectViewPresenter.Action>(Action.EDIT));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyInsuredObjectViewPresenter.Action>(Action.CANCEL_EDIT));
			}
			
			@Override
			public void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyInsuredObjectViewPresenter.Action>(Action.DELETE));
			}
		};
		objectWrapper.add(toolbar);
		
		objectForm = new InsuredObjectForm();
//		objectForm.showTypeSection(false);
		objectForm.setReadOnly(true);
		objectWrapper.add(objectForm);
		objectWrapper.setCellHeight(objectForm, "100%");
		
		wrapper.add(objectWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectForm() {
		return null; //this.objectForm;
	}

	@Override
	public HasEditableValue<SubPolicy> getSubPolicyForm() {
		return this.subPolicyForm;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.allowEdit(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

}
