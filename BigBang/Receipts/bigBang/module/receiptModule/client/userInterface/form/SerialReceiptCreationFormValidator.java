package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SerialReceiptCreationFormValidator extends
		FormValidator<SerialReceiptCreationForm> {

	public SerialReceiptCreationFormValidator(SerialReceiptCreationForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateReceiptNumber();
		valid &= validatePolicyNumber();
		valid &= validateType();
		valid &= validateTotalPremium();
		valid &= validateSalesPremium();
		valid &= validateCommission();
		valid &= validateRetro();
		valid &= validateFat();
		valid &= validateIssueDate();
		valid &= validateCoverageStart();
		valid &= validateCoverageEnd();
		valid &= validateDescription();
		valid &= validateNotes();
		valid &= validateBonusMalusOption();
		valid &= validateBonusMalusValue();
		valid &= validateDueDate();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateReceiptNumber() {
		return validateString(form.receiptNumber, 1, 250, false);
	}
	
	private boolean validatePolicyNumber() {
		return validateString(form.policyNumber, 1, 250, false);
	}

	private boolean validateType() {
		return validateGuid(form.type, false);
	}

	private boolean validateTotalPremium() {
		return validateNumber(form.totalPremium, false);
	}

	private boolean validateSalesPremium() {
		return validateNumber(form.salesPremium, true);
	}

	private boolean validateCommission() {
		return validateNumber(form.commission, true);
	}

	private boolean validateRetro() {
		return validateNumber(form.retro, true);
	}

	private boolean validateFat() {
		return validateNumber(form.fat, true);
	}

	private boolean validateIssueDate() {
		return validateDate(form.issueDate, true);
	}

	private boolean validateCoverageStart() {
		return validateDate(form.coverageStart, true); ///TODO
	}

	private boolean validateCoverageEnd() {
		return validateDate(form.coverageEnd, true); ///TODO

	}

	private boolean validateDueDate() {
		return validateDate(form.dueDate, true);
	}

	private boolean validateDescription() {
		return validateString(form.description, 0, 250, true);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateBonusMalusOption() {
		return true;
	}

	private boolean validateBonusMalusValue() {
		if("Bonus".equalsIgnoreCase(form.bonusMalusOption.getValue())) {
			return validateNumber(form.bonusMalusValue, false);
		}else if("Malus".equalsIgnoreCase(form.bonusMalusOption.getValue())) {
			return validateNumber(form.bonusMalusValue, false);
		}else{
			return true;
		}
	}
}
