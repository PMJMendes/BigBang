package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReceiveMessageFormValidator extends
		FormValidator<ReceiveMessageForm> {

	public ReceiveMessageFormValidator(ReceiveMessageForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateType();
		valid &= validateSubject();
		valid &= validateReplyLimit();
		valid &= validateIncomingMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateSubject() {
		return validateString(form.subject, 0, 250, true);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, form.replyLimit.isReadOnly());
	}

	private boolean validateIncomingMessage() {
		form.messageFormField.setWarning(form.messageFormField.getValue() == null);
		return true;
	}
	
	private boolean validateType() {
		return validateGuid(form.requestType, false);
	}

}
