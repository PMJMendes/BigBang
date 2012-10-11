package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ExternalRequestContinuationFormValidator extends
		FormValidator<ExternalRequestContinuationForm> {

	public ExternalRequestContinuationFormValidator(
			ExternalRequestContinuationForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateReplyLimit();
		valid &= validateMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}

	private boolean validateMessage() {
		form.message.setWarning(form.message.getValue() == null);
		return true;
	}

}
