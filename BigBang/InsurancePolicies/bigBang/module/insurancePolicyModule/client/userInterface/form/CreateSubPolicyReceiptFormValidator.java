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
		valid &= validateDates();
		valid &= validateLimitDate();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateDates() {
		if(validateMaturityDate() && validateEndDate()){
			if(form.from.getValue().before(form.to.getValue())){
				form.from.setInvalid(false);
				form.to.setInvalid(false);
				return true;
			}else{
				form.from.setInvalid(true);
				form.to.setInvalid(true);
				return false;
			}
		}
		return false;
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
