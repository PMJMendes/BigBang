package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CancelInfoOrDocumentRequestFormValidator extends
		FormValidator<CancelInfoOrDocumentRequestForm> {

	public CancelInfoOrDocumentRequestFormValidator(
			CancelInfoOrDocumentRequestForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateMotive();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateMotive(){
		return validateGuid(form.motive, false);
	}

}
