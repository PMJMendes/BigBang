package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.definitions.shared.InsurancePolicyStub.PolicyStatus;
import bigBang.definitions.shared.SubPolicy;
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
		valid &= validateTotalPremium();
		valid &= validateFractioning();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateStartAndEndDate();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateClient() {
		return validateGuid(form.client, false);
	}

	private boolean validateSubPolicyNumber() {
		SubPolicy currentSubPolicy = form.getValue();
		if(currentSubPolicy != null && (currentSubPolicy.statusIcon == PolicyStatus.PROVISIONAL || currentSubPolicy.statusIcon == null)) {
			return validateString(form.number, 0, 250, true);
		}else{
			return validateString(form.number, 1, 250, false);
		}
	}

	private boolean validatePremium() {
		return validateNumber(form.premium, 0.0, null, true);
	}

	private boolean validateTotalPremium() {
		return validateNumber(form.totalPremium, 0.0, null, true);
	}
	
	private boolean validateFractioning() {
		return validateGuid(form.fractioning, false);
	}

	private boolean validateStartDate() {
		return validateDate(form.startDate, false);
	}

	private boolean validateEndDate() {
		return validateDate(form.endDate, true);
		// JMMM: Em princípio, apólices temporárias não têm subapólices.
		// Se tivessem, seria necessário ver a duração da apólice mãe para validar a obrigatoriedade do End Date.  
	}

	private boolean validateStartAndEndDate() {
		boolean validDates = validateEndDate() && validateStartDate();

		if(validDates){
			Date startDate = form.startDate.getValue();
			Date endDate = form.endDate.getValue();

			if(startDate != null && endDate != null){
				if(endDate.before(startDate)){
					form.startDate.setInvalid(true);
					form.endDate.setInvalid(true);
					return false;
				}
				else{
					return true;
				}

			}else{
				return true;
			}
		}else{
			return false;
		}
	}

}
