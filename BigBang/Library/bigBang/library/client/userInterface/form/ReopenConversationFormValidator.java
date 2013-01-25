package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReopenConversationFormValidator extends
		FormValidator<ReopenConversationForm> {

	public ReopenConversationFormValidator(ReopenConversationForm form) {
		
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateNumber(form.days, 1.0, null, false);
		valid &= validateGuid(form.direction, false);
		
		return new Result(valid, validationMessages);
	}

}
