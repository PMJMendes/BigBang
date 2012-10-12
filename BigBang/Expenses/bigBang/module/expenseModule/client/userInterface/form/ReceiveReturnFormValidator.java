package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReceiveReturnFormValidator extends
		FormValidator<ReceiveReturnForm> {

	public ReceiveReturnFormValidator(ReceiveReturnForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateReason();
		
		return new Result(valid, this.validationMessages);
		
	}

	private boolean validateReason() {
		return validateString(form.reason, 0, 250, false);
	}

}
