package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class DASRequestCancellationFormValidator extends
		FormValidator<DASRequestCancellationForm> {

	public DASRequestCancellationFormValidator(DASRequestCancellationForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateMotive();
		
		return new Result(valid,  this.validationMessages);
	}

	private boolean validateMotive() {
		return validateGuid(form.motive, false);
	}

}
