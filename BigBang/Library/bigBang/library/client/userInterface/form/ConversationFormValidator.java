package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ConversationFormValidator extends
		FormValidator<ConversationForm> {

	public ConversationFormValidator(ConversationForm form) {
		super(form);
	}

	@Override
	protected Result validateImpl() {
		boolean valid = true;
		valid &= validateString(form.subject, 1, 250, false);
		valid &= validateGuid(form.requestType, false);
		valid &= validateNumber(form.replyLimit, false);
		return new Result(valid, validationMessages);
	}

}
