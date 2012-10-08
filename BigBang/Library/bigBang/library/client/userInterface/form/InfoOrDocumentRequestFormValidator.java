package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InfoOrDocumentRequestFormValidator extends
		FormValidator<InfoOrDocumentRequestForm> {

	public InfoOrDocumentRequestFormValidator(InfoOrDocumentRequestForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateType();
		valid &= validateTo();
		valid &= validateReplyLimit();
		valid &= validateForwardReply();
		valid &= validateInternalCC();
		valid &= validateExternalCC();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateType() {
		return validateGuid(form.requestType, false);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}

	private boolean validateTo() {
		return validateString(form.to, 1, 250, false);
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
