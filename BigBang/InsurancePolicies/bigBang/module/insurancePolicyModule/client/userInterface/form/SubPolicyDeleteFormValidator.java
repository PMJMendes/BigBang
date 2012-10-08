package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubPolicyDeleteFormValidator extends
		FormValidator<SubPolicyDeleteForm> {

	public SubPolicyDeleteFormValidator(SubPolicyDeleteForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateReason();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReason() {
		return validateString(form.reason, 0, 250, true);
	}

}
