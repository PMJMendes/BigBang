package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.library.client.FormValidator;

public class SubPolicyHeaderFormValidator extends
FormValidator<SubPolicyHeaderForm> {

	public SubPolicyHeaderFormValidator(SubPolicyHeaderForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateClient();
		valid &= validateSubPolicyNumber();
		valid &= validatePremium();
		valid &= validateFractioning();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateStartAndEndDate();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateEndDate() {
		return validateDate(form.endDate, true);
	}

	private boolean validateStartDate() {
		return validateDate(form.startDate, false);
	}

	private boolean validateFractioning() {
		return validateGuid(form.fractioning, false);
	}

	private boolean validatePremium() {
		return validateNumber(form.premium, 0.0, null, true);
	}

	private boolean validateSubPolicyNumber() {
		return validateString(form.number,0,250, true);
	}

	private boolean validateClient() {
		return validateGuid(form.client, false);
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

}
