package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SignatureRequestCancellationFormValidator extends
		FormValidator<SignatureRequestCancellationForm> {

	public SignatureRequestCancellationFormValidator(SignatureRequestCancellationForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateReason();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReason() {
		return validateGuid(form.motive, false);
	}
}
