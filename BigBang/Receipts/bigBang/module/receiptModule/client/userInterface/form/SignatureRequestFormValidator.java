package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SignatureRequestFormValidator extends
		FormValidator<SignatureRequestForm> {

	public SignatureRequestFormValidator(SignatureRequestForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateReplyLimit();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, 1.0, null, false);
	}

}
