package bigBang.library.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ExternalRequestClosingFormValidator extends
		FormValidator<ExternalRequestClosingForm> {

	public ExternalRequestClosingFormValidator(ExternalRequestClosingForm form) {
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
