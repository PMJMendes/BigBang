package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubCasualtyFormValidator extends FormValidator<SubCasualtyForm> {

	public SubCasualtyFormValidator(SubCasualtyForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateCasualtyNumber();
		valid &= validatePolicyAndSubPolicyNumber();
		valid &= validateProcessNumber();
		valid &= validateHasJudicialProcess();
		valid &= validateIsListOrTextInsuredObject();
		valid &= validateInsuredObjectInList();
		valid &= validateInsuredObjectInText();
		valid &= validatePresentInPolicy();

		return new Result(valid, this.validationMessages);
	}

	private boolean validatePresentInPolicy() {
		return form.belongsToPolicy != null;
	}

	private boolean validateInsuredObjectInText() {
		return validateString(form.insuredObjectName, 0, 250, true);
	}

	private boolean validateInsuredObjectInList() {
		return validateGuid(form.insuredObject, true);
	}

	private boolean validateIsListOrTextInsuredObject() {
		return validateInsuredObjectInList() || validateInsuredObjectInText();
	}

	private boolean validateHasJudicialProcess() {
		return form.hasJudicial != null;
	}

	private boolean validateProcessNumber() {
		return validateString(form.number, 0, 250, true);
	}

	private boolean validatePolicyAndSubPolicyNumber() {
		if(!(validatePolicyNumber() || validateSubPolicyNumber())){
			form.policyReference.setInvalid(true);
			form.subPolicyReference.setInvalid(true);
			return false;
		}
		else
			return true;
	}

	private boolean validateSubPolicyNumber() {
		return validateGuid(form.policyReference, false);
	}

	private boolean validatePolicyNumber() {
		return validateGuid(form.subPolicyReference, false);
	}

	private boolean validateCasualtyNumber() {
		return form.casualty.getValue() != null;
	}

}
