package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ExternalInfoRequestFormValidator extends
		FormValidator<ExternalInfoRequestForm> {

	public ExternalInfoRequestFormValidator(ExternalInfoRequestForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateSubject();
		valid &= validateReplyLimit();
		valid &= validateIncomingMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateSubject() {
		return validateString(form.requestSubject, 1, 250, false);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}

	private boolean validateIncomingMessage() {
		form.messageFormField.setWarning(form.messageFormField.getValue() == null);
		return true;
	}

}
