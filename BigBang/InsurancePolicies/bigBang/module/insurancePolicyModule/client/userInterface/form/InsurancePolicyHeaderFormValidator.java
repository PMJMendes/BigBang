package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InsurancePolicyHeaderFormValidator extends
		FormValidator<InsurancePolicyHeaderForm> {

	public InsurancePolicyHeaderFormValidator(InsurancePolicyHeaderForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validate() {
		boolean valid = true;
		valid &= validateManager();
		valid &= validateNumber();
		valid &= validateInsuranceAgency();
		valid &= validateMediator();
		valid &= validateMaturiryDate();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateDuration();
		valid &= validateFractioning();
		valid &= validatePremium();
		valid &= validateOperationalProfile();
		valid &= validateCoInsurance();
		valid &= validateCaseStudy();
				
		return new Result(valid, this.validationMessages);
	}

	private boolean validateManager() {
		return validateGuid(form.manager, false);
	}

	private boolean validateNumber() {
		return validateString(form.number, 0, 250, true);
	}

	private boolean validateInsuranceAgency() {
		return validateGuid(form.insuranceAgency, false);
	}

	private boolean validateMediator() {
		return validateGuid(form.mediator, true); //null means same as the client's
	}

	private boolean validateMaturiryDate() {
		return validateDate(form.maturityDate, false);
	}

	private boolean validateStartDate() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateEndDate() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateDuration() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateFractioning() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validatePremium() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateOperationalProfile() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateCoInsurance() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean validateCaseStudy() {
		// TODO Auto-generated method stub
		return false;
	}

}
