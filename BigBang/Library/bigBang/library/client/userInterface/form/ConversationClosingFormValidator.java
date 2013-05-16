package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ConversationClosingFormValidator extends
		FormValidator<ConversationClosingForm> {

	public ConversationClosingFormValidator(ConversationClosingForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateMotive();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateMotive(){
		return validateGuid(form.motive, false);
	}

}
