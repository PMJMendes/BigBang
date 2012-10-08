package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class TransferInsurancePolicyManagerFormValidator extends FormValidator<TransferInsurancePolicyManagerForm> {

	public TransferInsurancePolicyManagerFormValidator(
			TransferInsurancePolicyManagerForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateManager();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateManager() {
		return validateGuid(form.manager, false);
	}

}
