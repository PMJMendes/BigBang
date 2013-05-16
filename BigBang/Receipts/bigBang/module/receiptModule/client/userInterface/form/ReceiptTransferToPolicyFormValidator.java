package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReceiptTransferToPolicyFormValidator extends
		FormValidator<ReceiptTransferToPolicyForm> {

	public ReceiptTransferToPolicyFormValidator(ReceiptTransferToPolicyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validatePolicy();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validatePolicy() {
		return validateGuid(form.policy, false);
	}

}
