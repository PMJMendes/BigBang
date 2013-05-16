package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub.PolicyStatus;
import bigBang.library.client.CrossFormValidator;

public class PolicyCrossFormValidator extends CrossFormValidator {

	private InsurancePolicyHeaderFormValidator policyValidator;
	private InsuredObjectFormValidator objectValidator;
	private ExerciseSelectorFormValidator exerciseValidator;
	
	public PolicyCrossFormValidator(InsurancePolicyHeaderForm policyForm, InsuredObjectForm objectForm, ExerciseSelectorForm exerciseForm) {
		policyValidator = getNewPolicyValidator(policyForm);
		policyForm.setValidator(policyValidator);

		objectValidator = getNewObjectValidator(objectForm);
		objectForm.setValidator(objectValidator);

		exerciseValidator = getNewExerciseValidator(exerciseForm);
		exerciseForm.setValidator(exerciseValidator);
	}
	
	private InsurancePolicyHeaderFormValidator getNewPolicyValidator(InsurancePolicyHeaderForm form) {
		return new InsurancePolicyHeaderFormValidator(form) {
			@Override
			public bigBang.library.client.FormValidator.Result validateImpl() {
				Result result = super.validateImpl();
				result.valid &= validateExerciseDates();

				return result;
			}
		};
	}

	private InsuredObjectFormValidator getNewObjectValidator(InsuredObjectForm form) {
		return new InsuredObjectFormValidator(form) {
			@Override
			public bigBang.library.client.FormValidator.Result validateImpl() {
				Result result = super.validateImpl();
				result.valid &= validateInsuredObjectHeaderDates();
				result.valid &= validateExerciseDates();

				return result;
			}
		};
	}
	
	private ExerciseSelectorFormValidator getNewExerciseValidator(ExerciseSelectorForm form) {
		return new ExerciseSelectorFormValidator(form){
			@Override
			public bigBang.library.client.FormValidator.Result validateImpl() {
				Result result = super.validateImpl();
				result.valid &= validateExerciseDates();

				return result;
			}
		};
	}

	//Add cross-form validations here
	
	protected boolean validateInsuredObjectHeaderDates() {
		if ( (policyValidator != null) && (objectValidator != null) && (policyValidator.getForm() != null) ) {
			InsurancePolicy policy = policyValidator.getForm().getInfo();
			if(policy != null && policy.statusIcon != null && policy.statusIcon == PolicyStatus.PROVISIONAL) {
				if(objectValidator.getForm().inclusionDate.getValue() != null) {
					objectValidator.getForm().inclusionDate.setWarning(true);
				}
				if(objectValidator.getForm().exclusionDate.getValue() != null) {
					objectValidator.getForm().exclusionDate.setWarning(true);
				}
			}
		}
		return true;
	}

	protected boolean validateExerciseDates() {
		boolean valid = true;
		if ( (policyValidator != null) && (exerciseValidator != null) ) {
			if((policyValidator.getForm() != null) && (exerciseValidator.getForm() != null)) {
				if((exerciseValidator.getForm().startDate.getValue() != null) && (policyValidator.getForm().startDate.getValue() != null) &&
						(exerciseValidator.getForm().startDate.getValue().before(policyValidator.getForm().startDate.getValue()))) {
					valid = false;
					exerciseValidator.getForm().startDate.setInvalid(true);
				}
				if((exerciseValidator.getForm().endDate.getValue() != null) && (policyValidator.getForm().endDate.getValue() != null) &&
						(exerciseValidator.getForm().endDate.getValue().after(policyValidator.getForm().endDate.getValue()))) {
					valid = false;
					exerciseValidator.getForm().endDate.setInvalid(true);
				}
			}
		}
		return valid;
	}
	
}
