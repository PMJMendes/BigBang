package bigBang.module.casualtyModule.client.userInterface;

import bigBang.library.client.FormValidator;

public class RelapseFormValidator extends FormValidator<RelapseForm> {

	public RelapseFormValidator(RelapseForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateDate();
		valid &= validateDescription();
		
		return new Result(valid, validationMessages);
	}

	private boolean validateDescription() {
		return validateString(form.label, 1, 250, false);
	}

	private boolean validateDate() {
		return validateDate(form.date, false);
	}

}
