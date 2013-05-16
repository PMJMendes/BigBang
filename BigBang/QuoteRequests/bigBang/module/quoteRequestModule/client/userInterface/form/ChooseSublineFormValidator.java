package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ChooseSublineFormValidator extends
		FormValidator<ChooseSublineForm> {

	public ChooseSublineFormValidator(ChooseSublineForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateGuid(form.category, false);
		valid &= validateGuid(form.line, false);
		valid &= validateGuid(form.subLine, false);
		
		return new Result(valid, validationMessages);
	}

}
