package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InsurerAccountingExtraFormValidator extends
		FormValidator<InsurerAccountingExtraForm> {

	public InsurerAccountingExtraFormValidator(InsurerAccountingExtraForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		//TODO
		
		return new Result(valid, this.validationMessages);
	}

}
