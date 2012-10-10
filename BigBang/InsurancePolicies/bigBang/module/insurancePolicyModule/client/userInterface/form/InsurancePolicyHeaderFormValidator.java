package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub.PolicyStatus;
import bigBang.library.client.FormValidator;

public class InsurancePolicyHeaderFormValidator extends
FormValidator<InsurancePolicyHeaderForm> {

	public InsurancePolicyHeaderFormValidator(InsurancePolicyHeaderForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateManager();
		valid &= validateNumber();
		valid &= validateInsuranceAgency();
		valid &= validateMediator();
		valid &= validateMaturityDate();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateStartAndEndDate();
		valid &= validateDuration();
		valid &= validateFractioning();
		valid &= validatePremium();
		valid &= validateOperationalProfile();
		valid &= validateCoInsurance();
		valid &= validateCaseStudy();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateStartAndEndDate() {
		boolean validDates = validateEndDate() && validateStartDate();

		if(validDates){
			Date startDate = form.startDate.getValue();
			Date endDate = form.endDate.getValue();

			if(startDate != null && endDate != null){
				if(startDate.before(endDate)){
					return true;
				}
				else{
					form.startDate.setInvalid(true);
					form.endDate.setInvalid(true);
					return false;
				}

			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	private boolean validateManager() {
		return validateGuid(form.manager, true); //null means same as the client's
	}

	private boolean validateNumber() {
		InsurancePolicy currentPolicy = form.getValue();
		if(currentPolicy != null && (currentPolicy.statusIcon == PolicyStatus.PROVISIONAL || currentPolicy.statusIcon == null)) {
			return validateString(form.number, 0, 250, true);
		}else{
			return validateString(form.number, 1, 250, false);
		}
	}

	private boolean validateInsuranceAgency() {
		return validateGuid(form.insuranceAgency, false);
	}

	private boolean validateMediator() {
		return validateGuid(form.mediator, true); //null means same as the client's
	}

	private boolean validateMaturityDate() {
		if(BigBangConstants.TypifiedListValues.INSURANCE_POLICY_DURATION.TEMPORARY.equalsIgnoreCase(form.duration.getValue())) {
			return validateDate(form.maturityDate, true);
		}else{
			return validateDate(form.maturityDate, false);
		}
	}

	private boolean validateStartDate() {
		return validateDate(form.startDate, false);
	}

	private boolean validateEndDate() {
		if(validateDuration()){
			Date endDate = form.endDate.getValue();
			boolean valid = false;

			if(BigBangConstants.TypifiedListValues.INSURANCE_POLICY_DURATION.TEMPORARY.equalsIgnoreCase(form.duration.getValue())){
				valid = validateDate(form.endDate, false);
			}else{
				valid = validateDate(form.endDate, true);
			}

			if(valid) {
				if(endDate != null){
					return (validateStartDate() && form.startDate.getValue() != null);
				}else{
					return true;
				}
			}else{
				return false;
			}
		}else{
			form.endDate.setInvalid(true);
			return false;
		}
	}

	private boolean validateDuration() {
		return validateGuid(form.duration, false);
	}

	private boolean validateFractioning() {
		return validateGuid(form.fractioning, false);
	}

	private boolean validatePremium() {
		return validateNumber(form.premium, true);
	}

	private boolean validateOperationalProfile() {
		return validateGuid(form.operationalProfile, true);
	}

	private boolean validateCoInsurance() {
		return true; //TODO
	}

	private boolean validateCaseStudy() {
		return form.caseStudy.getValue() != null;
	}

}
