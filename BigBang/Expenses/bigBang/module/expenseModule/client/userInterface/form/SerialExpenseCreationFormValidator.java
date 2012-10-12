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
		return form.belongsToPolicy != null;
	}

	private boolean validateInsuredObject() {
		return validateInsuredObjectInList() || validateInsuredObjectInText();
	}

	private boolean validateInsuredObjectInList() {
		if ( form.belongsToPolicy.getValue().equalsIgnoreCase("true") )
			return validateGuid(form.insuredObject, false);
		return true;
	}

	private boolean validateInsuredObjectInText() {
		if ( !form.belongsToPolicy.getValue().equalsIgnoreCase("true") )
			return validateString(form.insuredObjectName, 1, 250, false);
		return true;
	}

}
