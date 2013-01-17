package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class OtherEntityFormValidator extends
		FormValidator<OtherEntityForm> {

	public OtherEntityFormValidator(OtherEntityForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		
		boolean valid = true;
		
		valid &= validateName();
		valid &= validateType();
		valid &= validateNotes();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateType() {
		return validateGuid(form.type,false);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

}
