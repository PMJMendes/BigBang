package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class DASRequestFormValidator extends FormValidator<DASRequestForm> {

	public DASRequestFormValidator(DASRequestForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateReplyLimit();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReplyLimit() {
		return validateNumber(form.replyLimit, false);
	}
	
}
