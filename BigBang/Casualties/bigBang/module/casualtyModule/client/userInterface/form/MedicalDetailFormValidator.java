package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class MedicalDetailFormValidator extends
FormValidator<MedicalDetailForm> {

	public MedicalDetailFormValidator(MedicalDetailForm form) {
		super(form);
	}


	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {

		boolean valid = true;

		valid &= validateStartDate();
		valid &= validateDisabilityType();
		valid &= validateLocation();
		valid &= validatePercentage();
		valid &= validateEndDate();
		valid &= validateBenefits();

		return new Result(valid, validationMessages);

	}

	private boolean validatePercentage() {
		return validateNumber(form.disabilityPercent, 0.0, 100.0, true);
	}


	private boolean validateStartDate() {
		return validateDate(form.startDate, false);	
	}


	private boolean validateDisabilityType() {
		return 	validateGuid(form.disabilityType, false);

	}


	private boolean validateLocation() {
		return validateString(form.disabilityLocation, 0, 250, true);
	}

	private boolean validateEndDate() {
		return validateDate(form.endDate, true);
	}


	private boolean validateBenefits() {
		return validateNumber(form.benefits, true);
	}


}
