package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InsurancePolicyVoidFormValidator extends
		FormValidator<InsurancePolicyVoidForm> {

	public InsurancePolicyVoidFormValidator(InsurancePolicyVoidForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validate() {
		boolean valid = true;
		valid &= validateMotive();
		valid &= validateEffectiveDate();
		valid &= validateNotes();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateMotive() {
		return validateGuid(form.motive, false);
	}

	private boolean validateEffectiveDate() {
		return validateDate(form.effectiveDate, false);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

}
