package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReceiveAcceptanceFormValidator extends
FormValidator<ReceiveAcceptanceForm> {

	public ReceiveAcceptanceFormValidator(ReceiveAcceptanceForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateAcceptance();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateAcceptance() {
		return validateNumber(form.value, 0.0, null, false);
	}

}
