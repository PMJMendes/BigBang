package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ExternalRequestReplyFormValidator extends
		FormValidator<ExternalRequestReplyForm> {

	public ExternalRequestReplyFormValidator(ExternalRequestReplyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateIsFinal();
		valid &= validateReplyLimit();
		valid &= validateMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateIsFinal() {
		return form.isFinal.getValue() != null;
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}

	private boolean validateMessage() {
		return validateOutgoingMessage(form.message, false);
	}

}
