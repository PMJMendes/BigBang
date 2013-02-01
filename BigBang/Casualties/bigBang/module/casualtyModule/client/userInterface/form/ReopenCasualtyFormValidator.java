package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ReopenCasualtyFormValidator extends
		FormValidator<ReopenCasualtyForm> {

	public ReopenCasualtyFormValidator(ReopenCasualtyForm form) {
		super(form);
	}
	
	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateGuid(form.reason, false);
		
		return new Result(valid, this.validationMessages);
	}

}
