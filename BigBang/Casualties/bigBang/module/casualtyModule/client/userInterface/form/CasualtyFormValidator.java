package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CasualtyFormValidator extends FormValidator<CasualtyForm> {

	public CasualtyFormValidator(CasualtyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateNumber();
		valid &= validateDate();
		valid &= validateDescription();
		valid &= validateNotes();
		valid &= validCaseStudy();

		return new Result(valid, this.validationMessages);
	}

	private boolean validCaseStudy() {
		return form.caseStudy.getValue() != null;
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateDescription() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateDate() {
		return validateDate(form.date, false);
	}

	private boolean validateNumber() {
		return validateString(form.number, 0, 250, true);
	}

}
