package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InfoOrDocumentRequestReplyFormValidator extends
		FormValidator<InfoOrDocumentRequestReplyForm> {

	public InfoOrDocumentRequestReplyFormValidator(
			InfoOrDocumentRequestReplyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validate() {
		boolean valid = true;
		
		return new Result(valid, this.validationMessages);
	}

}
