package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SerialExpenseCreationFormValidator extends FormValidator<SerialExpenseCreationForm> {

	public SerialExpenseCreationFormValidator(SerialExpenseCreationForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validatePolicyNumber();
		valid &= validateSubPolicyReference();
		valid &= validateManager();
		valid &= validateSettlement();
		valid &= validateNotes();
		valid &= validateExpenseDate();
		valid &= validateCoverageId();
		valid &= validateExpenseValue();
		valid &= validateBelongsToPolicy();
		valid &= validateInsuredObject();

		return new Result(valid, this.validationMessages);
	}

	private boolean validatePolicyNumber() {
		return validateString(form.policyNumber, 1, 250, false);	
	}

	private boolean validateSubPolicyReference() {
		form.noSubPolicy.setInvalid((form.subPolicyReference.getValue() == null) && !form.noSubPolicy.getValue());
		return validateGuid(form.subPolicyReference, form.noSubPolicy.getValue());
	}

	private boolean validateManager() {
		return validateGuid(form.manager, true);
	}

	private boolean validateSettlement() {
		return validateNumber(form.settlement, 0.0, null, true);
	}

	private boolean validateNotes() {
		return validateString(form.notes, 0, 250, true);
	}

	private boolean validateExpenseDate() {
		return validateDate(form.expenseDate, false);
	}

	private boolean validateCoverageId() {
		return validateGuid(form.coverageId, false);
	}

	private boolean validateExpenseValue() {
		return validateNumber(form.expenseValue, 0.0, null, false);
	}

	private boolean validateBelongsToPolicy() {
		return form.belongsToPolicy.getValue() != null;
	}

	private boolean validateInsuredObject() {
		boolean valid = false;

		valid |= validateInsuredObjectInList();
		valid |= validateInsuredObjectInText();

		return valid;
	}

	private boolean validateInsuredObjectInList() {
		if ( "true".equalsIgnoreCase(form.belongsToPolicy.getValue()) )
			return validateGuid(form.insuredObject, false);
		return true;
	}

	private boolean validateInsuredObjectInText() {
		if ( "false".equalsIgnoreCase(form.belongsToPolicy.getValue()) )
			return validateString(form.insuredObjectName, 1, 250, false);
		return true;
	}

}
