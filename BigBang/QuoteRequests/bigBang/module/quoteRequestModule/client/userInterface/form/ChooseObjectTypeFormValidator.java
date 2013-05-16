package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ChooseObjectTypeFormValidator extends
		FormValidator<ChooseObjectTypeForm> {

	public ChooseObjectTypeFormValidator(ChooseObjectTypeForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateGuid(form.objectType, false);
		
		return new Result(valid, validationMessages);
	}
}
