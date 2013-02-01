package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.receiptModule.client.userInterface.form.PaymentsForm;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.presenter.MarkForPaymentViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MarkForPaymentViewPresenter.Action;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.PaymentInfo.Payment;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public class MarkForPaymentView extends View implements MarkForPaymentViewPresenter.Display {

	protected ReceiptForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected PaymentsForm paymentsForm;
	protected Button backButton;

	public MarkForPaymentView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel paymentsWrapper = new VerticalPanel();
		paymentsWrapper.setSize("100%", "100%");
		wrapper.addEast(paymentsWrapper, 650);

		ListHeader paymentsHeader = new ListHeader("Cobranças");
		paymentsHeader.setHeight("30px");
		paymentsWrapper.add(paymentsHeader);
		
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == paymentsForm.newButton) {
					actionHandler.onActionInvoked(new ActionInvokedEvent<MarkForPaymentViewPresenter.Action>(Action.ADD_PAYMENT));
				}else if(event.getSource() == paymentsForm.confirmButton) {
					actionHandler.onActionInvoked(new ActionInvokedEvent<MarkForPaymentViewPresenter.Action>(Action.MARK_FOR_PAYMENT));
				}
			}

		};

		paymentsForm = new PaymentsForm();
		paymentsForm.setSize("100%", "100%");
		paymentsWrapper.add(paymentsForm);
		paymentsWrapper.setCellHeight(paymentsForm, "100%");
		
		paymentsForm.newButton.addClickHandler(clickHandler);
		paymentsForm.confirmButton.addClickHandler(clickHandler);
		paymentsForm.setReadOnly(false);

		VerticalPanel receiptWrapper = new VerticalPanel();
		receiptWrapper.setSize("100%", "100%");
		wrapper.add(receiptWrapper);

		ListHeader receiptHeader = new ListHeader("Recibo");
		receiptHeader.setHeight("30px");
		receiptWrapper.add(receiptHeader);

		backButton = new Button("Voltar");
		receiptHeader.setLeftWidget(backButton);
		
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MarkForPaymentViewPresenter.Action>(Action.CANCEL));
			}
		});
		
		form = new ReceiptForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);
		receiptWrapper.add(form);
		receiptWrapper.setCellHeight(form, "100%");

	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<Receipt> getForm() {
		return this.form;
	}

	@Override
	public HasEditableValue<Payment[]> getPaymentsHolder() {
		return this.paymentsForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void allowMarkForPayment(boolean allow) {
		this.paymentsForm.confirmButton.setEnabled(allow);
	}
	
	@Override
	public void allowNewPayment(boolean allow) {
		this.paymentsForm.newButton.setEnabled(allow);
	}

	@Override
	public void addPayment(Payment payment) {
		this.paymentsForm.addPayment(payment);
	}

}
