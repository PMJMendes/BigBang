package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubLineFormValidator extends FormValidator<SubLineForm> {

	public SubLineFormValidator(SubLineForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateDescription();
		valid &= validateObjectType();
		valid &= validatePeriodType();
		valid &= validatePercentage();
		valid &= validateIsLife();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateIsLife() {
		return form.isLife.getValue() != null;
	}

	private boolean validatePercentage() {
		return validateNumber(form.percentage, true);
	}

	private boolean validatePeriodType() {
		return validateGuid(form.periodType, false);
	}

	private boolean validateObjectType() {
		return validateGuid(form.type, false);
	}

	private boolean validateDescription() {
		return validateString(form.description, 0, 250, true);
	}

}
