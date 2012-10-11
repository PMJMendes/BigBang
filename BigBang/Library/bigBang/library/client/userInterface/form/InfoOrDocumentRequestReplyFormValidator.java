package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InfoOrDocumentRequestReplyFormValidator extends
		FormValidator<InfoOrDocumentRequestReplyForm> {

	public InfoOrDocumentRequestReplyFormValidator(
			InfoOrDocumentRequestReplyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateIncomingMessage();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateIncomingMessage() {
		form.incomingMessage.setWarning(form.incomingMessage.getValue() == null);
		return true;
	}

}
