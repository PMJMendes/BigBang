package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.Message;
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
		valid &= validateText();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateText() {

		if(form.emailOrNote.getValue() != null){
			return (form.emailOrNote.getValue().equals(Message.Kind.EMAIL.toString()) ? validateString(form.text.subject, 1, 250, false) : true);
		}
		else
			return true;
	}

	private boolean validateContactFrom() {
		return validateGuid(form.contactsFrom, Message.Kind.NOTE.toString().equalsIgnoreCase(form.emailOrNote.getValue()));
	}

	private boolean validateType() {
		return validateGuid(form.requestType, false);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, form.replyLimit.isReadOnly());
	}

	private boolean validateTo() {
		return validateGuid(form.to, Message.Kind.NOTE.toString().equalsIgnoreCase(form.emailOrNote.getValue()));
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
