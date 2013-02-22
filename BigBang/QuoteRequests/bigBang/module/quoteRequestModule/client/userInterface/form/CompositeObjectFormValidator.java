package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CompositeObjectFormValidator extends
		FormValidator<QuoteRequestObjectForm> {

	public CompositeObjectFormValidator(QuoteRequestObjectForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		return new Result(valid, validationMessages);
	}

}
