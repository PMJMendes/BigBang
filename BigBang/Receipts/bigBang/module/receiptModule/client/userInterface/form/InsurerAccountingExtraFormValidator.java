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
		valid &= validateValue();
		valid &= validateDescription();
		valid &= validateIsCommission();
		valid &= validateIsTax();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateValue() {
		return validateNumber(form.value, false);
	}

	private boolean validateDescription() {
		if ( form.value.getValue() == null )
		{
			form.description.setWarning(form.description.getValue() != null);
			return validateString(form.description, 0, 250, true);
		}
		return validateString(form.description, 1, 250, false);
	}

	private boolean validateIsCommission() {
		return form.isCommission.getValue() != null;
	}

	private boolean validateIsTax() {
		return form.isTax.getValue() != null;
	}

}
