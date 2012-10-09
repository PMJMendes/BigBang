package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CasualtyCloseFormValidator extends FormValidator<CasualtyCloseForm> {

	public CasualtyCloseFormValidator(CasualtyCloseForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateReason();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateReason() {
		return validateString(form.reason, 0, 256, true);	}

}
