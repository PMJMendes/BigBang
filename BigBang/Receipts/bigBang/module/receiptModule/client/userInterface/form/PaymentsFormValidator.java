package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormValidator;

public class PaymentsFormValidator extends FormValidator<PaymentsForm> {

	public PaymentsFormValidator(PaymentsForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid = validatePaymentSections();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validatePaymentSections() {
		boolean valid = true;
		for(PaymentSection section : form.sections){
			valid &= validatePaymentSection(section);
		}
		return valid;
	}

	private boolean validatePaymentSection(PaymentSection section) {
		boolean valid = true;

		valid &= validateType(section);
		valid &= validateValue(section);
		valid &= validateBank(section);
		valid &= validateChequeOrTransferNumber(section);
		valid &= validateOtherReceiptId(section);
		valid &= validateMarkOtherAsPaid(section);
		
		return valid;
	}

	private boolean validateType(PaymentSection section) {
		return validateGuid(section.type, false);
	}

	private boolean validateValue(PaymentSection section) {
		if ( form.sections.size() == 1 )
		{
			section.paymentValue.setWarning(section.paymentValue.getValue() == null);
			return validateNumber(section.paymentValue, true);
		}
		return validateNumber(section.paymentValue, false);
	}

	private boolean validateBank(PaymentSection section) {
		if(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.BANK_TRANSFER.equalsIgnoreCase(section.type.getValue()) ||
				BigBangConstants.OperationIds.ReceiptProcess.PaymentType.CHEQUE.equalsIgnoreCase(section.type.getValue())){
			return validateGuid(section.bank, true);
		}else{
			return true;
		}
	}

	private boolean validateChequeOrTransferNumber(PaymentSection section) {
		if(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.BANK_TRANSFER.equalsIgnoreCase(section.type.getValue()) ||
				BigBangConstants.OperationIds.ReceiptProcess.PaymentType.CHEQUE.equalsIgnoreCase(section.type.getValue())){
			return validateGuid(section.chequeOrTransferNumber, true);
		}else{
			return true;
		}
	}

	private boolean validateOtherReceiptId(PaymentSection section) {
		if(BigBangConstants.OperationIds.ReceiptProcess.PaymentType.COMPENSATION.equalsIgnoreCase(section.type.getValue())){
			return validateGuid(section.otherReceiptId, false);
		}else{
			return true;
		}
	}

	private boolean validateMarkOtherAsPaid(PaymentSection section) {
		return section.markOtherAsPayed.getValue() != null;
	}

}
