package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SendMessageFormValidator extends
		FormValidator<SendMessageForm> {

	public SendMessageFormValidator(SendMessageForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateContactFrom();
		valid &= validateType();
		valid &= validateTo();
		valid &= validateReplyLimit();
		valid &= validateForwardReply();
		valid &= validateInternalCC();
		valid &= validateExternalCC();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateContactFrom() {
		return validateGuid(form.contactsFrom, false);
	}

	private boolean validateType() {
		return validateGuid(form.requestType, false);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, form.replyLimit.isReadOnly());
	}

	private boolean validateTo() {
		return validateGuid(form.to, false);
	}

	private boolean validateForwardReply() {
		return true;
	}

	private boolean validateInternalCC() {
		return true;
	}

	private boolean validateExternalCC() {
		return true;
	}

}
