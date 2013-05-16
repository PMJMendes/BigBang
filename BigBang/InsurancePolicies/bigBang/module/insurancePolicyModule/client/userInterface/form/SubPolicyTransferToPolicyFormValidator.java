package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubPolicyTransferToPolicyFormValidator extends
		FormValidator<SubPolicyTransferToPolicyForm> {

	public SubPolicyTransferToPolicyFormValidator(
			SubPolicyTransferToPolicyForm form) {
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
