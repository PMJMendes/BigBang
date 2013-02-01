package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CasualtyDeleteFormValidator extends
FormValidator<CasualtyDeleteForm> {

	public CasualtyDeleteFormValidator(CasualtyDeleteForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateReason();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateReason() {
		return validateString(form.reason, 1, 250, false);
	}

}
