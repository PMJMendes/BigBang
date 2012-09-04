package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter.Action;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateReceiptView extends View implements CreateReceiptViewPresenter.Display {
	
	private ReceiptForm form;
	private InsurancePolicyForm ownerForm;
	private ActionInvokedEventHandler<Action> handler;
	private BigBangOperationsToolBar toolbar;
	
	public CreateReceiptView(){
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
				handler.onActionInvoked(new ActionInvokedEvent<CreateReceiptViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateReceiptViewPresenter.Action>(Action.CANCEL));
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
		
		ListHeader ownerFormHeader = new ListHeader("Apólice");
		ownerFormHeader.setHeight("30px");
		ownerFormWrapper.add(ownerFormHeader);
		
		ownerForm = new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
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
	public HasValue<InsurancePolicy> getParentForm() {
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
