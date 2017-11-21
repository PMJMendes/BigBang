package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
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
		valid &= validateInsurerRequests();
		valid &= validateFraming();
		valid &= validateString(form.notes, 0, 4000, true);
		valid &= validateString(form.internalNotes, 0, 4000, true);

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
			if (section.getItem().deleted != true) {
				valid &= validateDetailSection(section);
			}
		}
		
		return valid;
	}
	
	/**
	 * Iterates and validates the insurer requests 
	 */
	private boolean validateInsurerRequests() {
		boolean valid = true;
		for(SubCasualtyInsurerRequestSection section : form.subCasualtyInsurerRequestSections){
			if (section.getRequest().deleted != true) {
				valid &= validateInsurerRequestSection(section);
			}
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
		valid &= validateGuid(section.damageType, false);
		valid &= validateGuid(section.injuryCause, true);
		valid &= validateGuid(section.injuryType, true);
		valid &= validateGuid(section.injuredPart, true);
		valid &= (section.thirdParty.getValue() != null);
		valid &= validateString(section.notes, 0, 250, true);

		return valid;
	}
	
	/**
	 * Validates a insurer request
	 */
	private boolean validateInsurerRequestSection(SubCasualtyInsurerRequestSection section) {
		boolean valid = true;
		
		valid &= validateGuid(section.requestType, false);
		valid &= validateDate(section.requestDate, false);
		valid &= validateDate(section.acceptanceDate, true);
		
		if (section.conforms.getValue().booleanValue()) {
			valid &= validateDate(section.resendDate, true);
		} else {
			valid &= validateDate(section.clarificationDate, true);
			valid &= validateGuid(section.clarificationType, true);
		}
		
		// Checks for non null dates that should be null
		if (section.conforms.getValue().booleanValue()) {
			if (section.clarificationDate.getValueForValidation()!=null) {
				valid &= false;
				section.clarificationDate.setInvalid(true);
			}
			if (section.clarificationType.getValue()!=null) {
				valid &= false;
				section.clarificationType.setInvalid(true);
			} 
		} else {
			if (section.resendDate.getValueForValidation()!=null) {
				valid &= false;
				section.resendDate.setInvalid(true);
			} 
		}

		return valid;
	}	
	
	/**
	 * Validates the framing section. This validation does not contemplates nullable fields
	 * (this only occurs while marking the sub-casualty for closing) 
	 */
	private boolean validateFraming() {
		
		SubCasualtyFramingSection section = form.framingSection; 
		
		boolean valid = true;
		
		valid &= validateDate(section.analysisDate, true);
		valid &= validateString(section.validityNotes, 0, 250, true);
		valid &= validateString(section.generalExclusionsNotes, 0, 250, true);
		valid &= validateString(section.coverageRelevancyNotes, 0, 250, true);
		valid &= validateNumber(section.coverageValue, 0.0, null, true);
		valid &= validateString(section.coverageExclusionsNotes, 0, 250, true);
		valid &= validateNumber(section.franchise, 0.0, null, true);
		valid &= validateGuid(section.deductibleType, true);
		valid &= validateString(section.franchiseNotes, 0, 250, true);
		valid &= validateGuid(section.insurerEvaluation, true);
		valid &= validateString(section.insurerEvaluationNotes, 0, 250, true);
		valid &= validateGuid(section.expertEvaluation, true);
		valid &= validateString(section.expertEvaluationNotes, 0, 250, true);
		valid &= validateString(section.coverageNotes, 0, 250, true);
		valid &= validateString(section.declinedCasualtyNotes, 0, 250, true);
		valid &= validateString(section.declinedWarningNotes, 0, 250, true);
		valid &= validateHeadingFields(section);

		for(SubCasualtyFramingEntitySection entitySection : form.aditionalEntitiesSection){
			if (entitySection.getFramingEntity().deleted != true) {
				valid &= validateFramingEntitySection(entitySection);
			}
		}
		
		return valid;
	}

	private boolean validateHeadingFields(SubCasualtyFramingSection section) {

		boolean valid = true;
		
		valid &= validateNumber(section.baseSalary, 0.0, null, true);
		valid &= validateNumber(section.feedAllowance, 0.0, null, true);
		valid &= validateNumber(section.otherFees12, 0.0, null, true);
		valid &= validateNumber(section.otherFees14, 0.0, null, true);
		
		return valid;
	}

	/**
	 * Validates an aditional framing entity section. The user must define the entity
	 */
	private boolean validateFramingEntitySection(
			SubCasualtyFramingEntitySection entitySection) {

		boolean valid = true;
		
		valid &= validateGuid(entitySection.entityType, false);
		valid &= validateGuid(entitySection.evaluation, true);
		valid &= validateString(entitySection.notes, 0, 250, true);
		
		return valid;
	}
}
