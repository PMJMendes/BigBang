package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CreateInsurancePolicyFormValidator extends
		FormValidator<CreateInsurancePolicyForm> {

	public CreateInsurancePolicyFormValidator(CreateInsurancePolicyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateCategory();
		valid &= validateLine();
		valid &= validateSubLine();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateCategory() {
		return validateGuid(form.category, false);
	}

	private boolean validateLine() {
		return validateGuid(form.line, false);
	}

	private boolean validateSubLine() {
		return validateGuid(form.subLine, false);
	}

}
