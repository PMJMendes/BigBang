package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CompositeObjectFormValidator extends
		FormValidator<CompositeObjectForm> {

	public CompositeObjectFormValidator(CompositeObjectForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		return new Result(valid, validationMessages);
	}

}
