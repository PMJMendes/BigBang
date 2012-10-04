package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CreateDebitNoteFormValidator extends
		FormValidator<CreateDebitNoteForm> {

	public CreateDebitNoteFormValidator(CreateDebitNoteForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validate() {
		boolean valid = true;
		valid &= validateValue();
		valid &= validateMaturityDate();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateValue() {
		return validateNumber(form.noteValue, false);
	}

	private boolean validateMaturityDate() {
		return validateDate(form.maturityDate, false);
	}

}
