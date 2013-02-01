package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubCasualtyMarkForClosingFormValidator extends
		FormValidator<SubCasualtyMarkForClosingForm> {

	public SubCasualtyMarkForClosingFormValidator(
			SubCasualtyMarkForClosingForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateRevisor();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateRevisor() {
		return validateGuid(form.revisor, false);
	}

}
