package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt.PaymentInfo.Payment;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSelectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSelectionView;

public class PaymentSection extends FormViewSection {

	protected Payment value;
	public Button removeButton;

	protected ExpandableListBoxFormField type;
	protected NumericTextBoxFormField paymentValue;
	protected ExpandableListBoxFormField bank;
	protected TextBoxFormField chequeOrTransferNumber;
	protected ExpandableSelectionFormField otherReceiptId;
	protected CheckBoxFormField markOtherAsPayed;

	public PaymentSection() {
		super("Pagamento");

		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.RECEIPT_PAYMENT_TYPES, "Tipo de Pagamento");
		paymentValue = new NumericTextBoxFormField("Valor");
		paymentValue.setFieldWidth("175px");
		bank = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.BANK, "Banco");
		chequeOrTransferNumber = new TextBoxFormField("Número de Cheque ou Transferência");

		ReceiptSelectionView selectionView = (ReceiptSelectionView) GWT.create(ReceiptSelectionView.class); 
		ReceiptSelectionViewPresenter selectionPanel = new ReceiptSelectionViewPresenter(selectionView); 
		selectionPanel.setOperationId(BigBangConstants.OperationIds.ReceiptProcess.MARK_FOR_PAYMENT);
		selectionPanel.go();

		otherReceiptId = new ExpandableSelectionFormField(BigBangConstants.EntityIds.RECEIPT, "Recibo de Compensação", selectionPanel);
		markOtherAsPayed = new CheckBoxFormField("Marcar Recibo de Compensação como pago");

		addFormFieldGroup(new FormField[]{
				type, 
				paymentValue
		}, true);

		removeButton = new Button("Remover");
		removeButton.getElement().getStyle().setProperty("marginTop", "20px");
		removeButton.getElement().getStyle().setProperty("marginRight", "100%");

		type.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String typeValue = event.getValue();

				if(typeValue == null || typeValue.isEmpty()) {
					setForTypeNone();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.BANK_TRANSFER)){
					setForTypeBankTransfer();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.CASH)){
					setForTypeCash();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.CHEQUE)){
					setForTypeCheque();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.COMPENSATION)){
					setForTypeCompensation();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.CURRENT_ACCOUNT)){
					setForTypeCurrentAccount();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.DIRECT_TO_INSURER)){
					setForTypeDirectToInsurer();
				}else if(typeValue.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.FROM_THE_INSURER)){
					setForTypeFromTheInsurer();
				}
			}
		});
		
		setForTypeNone();
	}

	public Payment getPayment() {
		Payment result = this.value;

		if(result != null) {
			result.bankId = bank.getValue();
			result.chequeOrTransferNumber = chequeOrTransferNumber.getValue();
			result.markOtherAsPayed = markOtherAsPayed.getValue();
			result.otherReceiptId = otherReceiptId.getValue();
			result.paymentTypeId = type.getValue();
			result.value = paymentValue.getValue();
		}

		return result;
	}

	public void setPayment(Payment payment){
		this.value = payment;

		if(this.value == null) {
			payment = new Payment();
		}
		type.setValue(payment.paymentTypeId, true);
		paymentValue.setValue(payment.value);
		bank.setValue(payment.bankId);
		chequeOrTransferNumber.setValue(payment.chequeOrTransferNumber);
		otherReceiptId.setValue(payment.otherReceiptId);
		markOtherAsPayed.setValue(payment.markOtherAsPayed);
	}

	protected void setForTypeCheque(){
		setForTypeNone();
		addFormFieldGroup(new FormField[]{
				bank, chequeOrTransferNumber
		}, false);
		addWidget(removeButton, false);
	}

	protected void setForTypeCurrentAccount(){
		setForTypeNone();
		addWidget(removeButton, false);
	}

	protected void setForTypeCash() {
		setForTypeNone();
		addWidget(removeButton, false);
	}

	protected void setForTypeDirectToInsurer(){
		setForTypeNone();
		addWidget(removeButton, false);
	}

	protected void setForTypeBankTransfer(){
		setForTypeNone();
		addFormFieldGroup(new FormField[]{
				bank, chequeOrTransferNumber
		}, false);
		addWidget(removeButton, false);
	}

	protected void setForTypeCompensation(){
		setForTypeNone();
		addFormFieldGroup(new FormField[]{
				otherReceiptId, markOtherAsPayed
		}, false);
		addWidget(removeButton, false);
	}

	protected void setForTypeFromTheInsurer(){
		setForTypeNone();
		addWidget(removeButton, false);
	}

	protected void setForTypeNone(){
		unregisterFormField(bank);
		unregisterFormField(chequeOrTransferNumber);
		unregisterFormField(markOtherAsPayed);
		unregisterFormField(otherReceiptId);
		addWidget(removeButton, false);
	}

}
