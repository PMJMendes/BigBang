package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class NegotiationFormValidator extends FormValidator<NegotiationForm> {

	public NegotiationFormValidator(NegotiationForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateGuid(form.company, false);
		valid &= validateGuid(form.manager, false);

		return new Result(valid, validationMessages);
	}

}
