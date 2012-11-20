package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CreateSubPolicyReceiptFormValidator extends
FormValidator<CreateSubPolicyReceiptForm> {

	public CreateSubPolicyReceiptFormValidator(CreateSubPolicyReceiptForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateMaturityDate();
		valid &= validateEndDate();
		valid &= validateLimitDate();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateMaturityDate() {
		return validateDate(form.from, false);
	}

	private boolean validateEndDate() {
		return validateDate(form.to, false);
	}

	private boolean validateLimitDate() {
		return validateDate(form.limitDate, false);
	}

}
