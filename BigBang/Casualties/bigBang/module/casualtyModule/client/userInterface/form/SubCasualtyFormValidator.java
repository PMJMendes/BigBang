package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
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
		valid &= validatePresentInPolicy();
		valid &= validateInsuredObject();
		valid &= validateDetails();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateCasualtyNumber() {
		return form.casualty.getValue() != null;
	}

	private boolean validatePolicyAndSubPolicyNumber() {
		if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(form.referenceType.getValue()))
			return validateSubPolicyNumber();
		else
			return validatePolicyNumber();
	}

	private boolean validatePolicyNumber() {
		return validateGuid(form.policyReference, false);
	}

	private boolean validateSubPolicyNumber() {
		return validateGuid(form.subPolicyReference, false);
	}

	private boolean validateProcessNumber() {
		return validateString(form.number, 0, 250, true);
	}

	private boolean validateHasJudicialProcess() {
		return form.hasJudicial != null;
	}
	
	private boolean validateInsuredObject() {
		if(!validatePresentInPolicy()){
			return false;
		}
		
		boolean valid = validateInsuredObjectInList();
		
		valid |= validateInsuredObjectInText();
		
		return valid;
	}

	private boolean validatePresentInPolicy() {
		return form.belongsToPolicy.getValue() != null;
	}

	private boolean validateInsuredObjectInList() {
		if (form.belongsToPolicy.getValue().equalsIgnoreCase("true") )
			return validateGuid(form.insuredObject, false);
		return true;
	}

	private boolean validateInsuredObjectInText() {
		if (!form.belongsToPolicy.getValue().equalsIgnoreCase("true"))
			return validateString(form.insuredObjectName, 1, 250, false);
		return true;
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
		valid &= validateNumber(section.damages, 0.0, null, true);
		valid &= validateNumber(section.settlement, 0.0, null, true);
		valid &= validateNumber(section.deductible, 0.0, null, true);
		valid &= validateNumber(section.itemValue, 0.0, null, true);
		valid &= validateGuid(section.damageType, true);

		return valid;
	}

}
