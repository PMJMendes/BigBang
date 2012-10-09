package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyCreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyCreateReceiptViewPresenter.Action;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public class SubPolicyCreateReceiptView extends View implements SubPolicyCreateReceiptViewPresenter.Display {

	private ReceiptForm form;
	private SubPolicyForm ownerForm;
	private ActionInvokedEventHandler<Action> handler;
	private BigBangOperationsToolBar toolbar;
	
	public SubPolicyCreateReceiptView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		ListHeader formHeader = new ListHeader("Recibo");
		formHeader.setHeight("30px");
		formWrapper.add(formHeader);
		
		this.toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				return;
			}

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SubPolicyCreateReceiptViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SubPolicyCreateReceiptViewPresenter.Action>(Action.CANCEL));
			}
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.setSaveModeEnabled(true);
		
		form = new ReceiptForm();
		form.setReadOnly(false);
		
		formWrapper.add(toolbar);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");		
		
		VerticalPanel ownerFormWrapper = new VerticalPanel();
		ownerFormWrapper.setSize("100%", "100%");
		
		ListHeader ownerFormHeader = new ListHeader("Apólice Adesão");
		ownerFormHeader.setHeight("30px");
		ownerFormWrapper.add(ownerFormHeader);
		
		ownerForm = new SubPolicyForm();
		ownerForm.setReadOnly(true);
		ownerFormWrapper.add(ownerForm);
		ownerFormWrapper.setCellHeight(ownerForm, "100%");
		
		wrapper.addWest(ownerFormWrapper, 600);
		wrapper.add(formWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Receipt> getForm() {
		return this.form;
	}

	@Override
	public HasValue<SubPolicy> getParentForm() {
		return this.ownerForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

}
