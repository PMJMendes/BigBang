package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.library.client.FormValidator;

public class ExternalRequestContinuationFormValidator extends
		FormValidator<ExternalRequestContinuationForm> {

	public ExternalRequestContinuationFormValidator(
			ExternalRequestContinuationForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateReplyLimit();
		valid &= validateMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}

	private boolean validateMessage() {
		IncomingMessage message = form.message.getValue();
		boolean valid = message != null;
		form.message.setInvalid(!valid);
		return valid;
	}

}
