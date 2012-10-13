package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.CoInsurer;
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
		valid &= validateDuration();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateStartAndEndDate();
		valid &= validateFractioning();
		valid &= validatePremium();
		valid &= validateOperationalProfile();
		valid &= validateCaseStudy();
		valid &= validateCoInsurance();

		return new Result(valid, this.validationMessages);
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

	private boolean validateDuration() {
		return validateGuid(form.duration, false);
	}

	private boolean validateStartDate() {
		return validateDate(form.startDate, false);
	}

	private boolean validateEndDate() {
		if(BigBangConstants.TypifiedListValues.INSURANCE_POLICY_DURATION.TEMPORARY.equalsIgnoreCase(form.duration.getValue())){
			return validateDate(form.endDate, false);
		}else{
			return validateDate(form.endDate, true);
		}
	}

	private boolean validateStartAndEndDate() {
		if(validateEndDate() && validateStartDate()){
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

	private boolean validateFractioning() {
		return validateGuid(form.fractioning, false);
	}

	private boolean validatePremium() {
		return validateNumber(form.premium, true);
	}

	private boolean validateOperationalProfile() {
		return validateGuid(form.operationalProfile, true); //null means same as the client's
	}

	private boolean validateCaseStudy() {
		return form.caseStudy.getValue() != null;
	}

	private boolean validateCoInsurance() {
		boolean valid = true;
		CoInsurer[] value = form.coInsurers.getValue();
		if(value != null){
			double total = 0;
			for ( int i = 0; i < value.length; i++ )
			{
				if ( (value[i].percent == null) || (value[i].percent < 0) || (value[i].percent >= 100) )
				{
					valid = false;
					break;
				}
				total += value[i].percent;
				if ( total >= 100 )
				{
					valid = false;
					break;
				}
			}
			form.coInsurers.setInvalid(!valid);
			return valid;
		}
		return true;
	}

}
