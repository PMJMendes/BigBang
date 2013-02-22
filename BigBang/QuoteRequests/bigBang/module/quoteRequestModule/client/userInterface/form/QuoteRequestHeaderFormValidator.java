package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class QuoteRequestHeaderFormValidator extends
		FormValidator<QuoteRequestHeaderForm> {

	public QuoteRequestHeaderFormValidator(QuoteRequestHeaderForm form) {
		super(form);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		return new Result(valid, validationMessages);
	}

}
