package bigBang.module.insurancePolicyModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter.Action;

public class SubPolicyView extends View implements SubPolicyViewPresenter.Display {

	private ActionInvokedEventHandler<Action> actionHandler;
	private SubPolicyForm form;
	private InsurancePolicyForm policyForm;
	private SubPolicyOperationsToolbar toolbar;
	//	private SubPolicyChildrenPanel childrenPanel;

	public SubPolicyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		toolbar = new SubPolicyOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CANCEL_EDIT));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onIncludeInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.INCLUDE_INSURED_OBJECT));
			}

			@Override
			public void onIncludeInsuredObjectFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.INCLUDE_INSURED_OBJECT_FROM_CLIENT));
			}

			@Override
			public void onCreateInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_INSURED_OBJECT));
			}

			@Override
			public void onCreateInsuredObjectFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_INSURED_OBJECT_FROM_CLIENT));
			}

			@Override
			public void onExcludeInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.EXCLUDE_INSURED_OBJECT));
			}

			@Override
			public void onPerformCalculations() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.PERFORM_CALCULATIONS));
			}

			@Override
			public void onValidate() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.VALIDATE));
			}

			@Override
			public void onTransferToPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.TRANSFER_TO_POLICY));
			}

			@Override
			public void onCreateInfoOrDocumentRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_INFO_OR_DOCUMENT_REQUEST));
			}

			@Override
			public void onCreateReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_RECEIPT));
			}

			@Override
			public void onVoid() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.VOID));
			}

		};

		VerticalPanel policyFormWrapper = new VerticalPanel();
		policyFormWrapper.setSize("100%", "100%");

		policyForm = new InsurancePolicyForm() {

			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
		policyForm.setReadOnly(true);
		policyForm.setSize("100%", "100%");

		ListHeader policyFormHeader = new ListHeader("Apólice Principal");
		policyFormHeader.setHeight("30px");
		policyFormWrapper.add(policyFormHeader);
		policyFormWrapper.add(policyForm);
		policyFormWrapper.setCellHeight(policyForm, "100%");
		wrapper.addWest(policyFormWrapper, 600);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		form = new SubPolicyForm();
		form.setSize("100%", "100%");

		ListHeader formHeader = new ListHeader("Apólice Adesão");
		formHeader.setHeight("30px");
		formWrapper.add(formHeader);	
		formWrapper.add(toolbar);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		wrapper.add(formWrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<SubPolicy> getForm() {
		return this.form;
	}

	@Override
	public HasValue<InsurancePolicy> getParentPolicyForm() {
		return this.policyForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void confirmDelete(final ResponseHandler<Boolean> handler) {
		MessageBox.confirm("", "Pretende realmente eliminar a Apólice Adesão", new ConfirmationCallback() {

			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}

	//PERMISSIONS

	@Override
	public void allowEdit(boolean allow) {
		toolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowIncludeInsuredObject(boolean allow) {
		//TODO
	}

	@Override
	public void allowCreateInsuredObject(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowIncludeInsuredObjectFromClient(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowCreateInsuredObjectFromClient(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowExcludeInsuredObject(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowPerformCalculations(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowValidate(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowTransferToPolicy(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowCreateInfoOrDocumentRequest(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowVoid(boolean allow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowDelete(boolean allow) {
		toolbar.allowDelete(allow);
	}

}
