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
		valid &= validateDetails();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateDetails() {
		boolean valid = true;
		for(SubCasualtyItemSection section : form.subCasualtyItemSections){
			valid &= validateDetailSection(section);
		}
		
		return valid;
	}

	private boolean validateDetailSection(SubCasualtyItemSection section) {
		boolean valid = true;
		
		valid &= validateGuid(section.coverage, true);
		valid &= validateNumber(section.damages, 1.0, null, true);
		valid &= validateNumber(section.settlement, 1.0, null, true);
		valid &= validateNumber(section.deductible, 1.0, null, true);
		valid &= validateNumber(section.itemValue, 1.0, null, true);
		valid &= validateGuid(section.damageType, true);
		
		return valid;
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
