package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class VoidDebitNoteFormValidator extends
		FormValidator<VoidDebitNoteForm> {

	public VoidDebitNoteFormValidator(VoidDebitNoteForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		valid &= validateGuid(form.motive, false);
		
		return new Result(valid, validationMessages);
	}

}
