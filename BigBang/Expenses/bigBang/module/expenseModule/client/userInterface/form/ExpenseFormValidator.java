package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class ExpenseFormValidator extends FormValidator<ExpenseForm> {

	public ExpenseFormValidator(ExpenseForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateExpenseNumber();
		valid &= validateCoverage();
		valid &= validateBelongsToPolicy();
		valid &= validateInsuredObject();
		valid &= validateExpenseDate();
		valid &= validateManager();
		valid &= validateValue();
		valid &= validateSettlement();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateExpenseNumber() {
		return validateString(form.number, 0, 250, true);
	}

	private boolean validateCoverage() {
		return validateGuid(form.coverageId, false);
	}

	private boolean validateBelongsToPolicy() {
		return form.belongsToPolicy.getValue() != null;
	}

	private boolean validateInsuredObject() {
		if(!validateBelongsToPolicy()){
			return false;
		}
		
		boolean valid = validateInsuredObjectInList();
		
		valid |= validateInsuredObjectInText();
		
		return valid;
	}

	private boolean validateInsuredObjectInList() {
		if (form.belongsToPolicy.getValue().equalsIgnoreCase("true") )
			return validateGuid(form.insuredObject, false);
		return true;
	}

	private boolean validateInsuredObjectInText() {
		if ( !form.belongsToPolicy.getValue().equalsIgnoreCase("true") )
			return validateString(form.insuredObjectName, 1, 250, false);
		return true;
	}

	private boolean validateExpenseDate() {
		return validateDate(form.expenseDate, false);
	}

	private boolean validateManager() {
		return validateGuid(form.manager, true);
	}

	private boolean validateValue() {
		return validateNumber(form.value, 0.0, null, false);
	}

	private boolean validateSettlement() {
		return validateNumber(form.settlement, 0.0, null, !form.tempIsManual);
	}

}
