package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubCasualtyReopenFormValidator extends
		FormValidator<SubCasualtyReopenForm> {

	public SubCasualtyReopenFormValidator(SubCasualtyReopenForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateGuid(form.subCasualties, false);
		valid &= validateGuid(form.reason, false);
		
		return new Result(valid, validationMessages);
		
	}

	

}
