package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub.PolicyStatus;
import bigBang.library.client.CrossFormValidator;

public class PolicyAndObjectCrossFormValidator extends CrossFormValidator {

	private InsurancePolicyHeaderFormValidator policyValidator;
	private InsuredObjectFormValidator objectValidator;
	private ExerciseSelectorFormValidator exerciseValidator;
	
	public PolicyAndObjectCrossFormValidator(InsurancePolicyHeaderForm policyForm, InsuredObjectForm objectForm, ExerciseSelectorForm exerciseForm) {
		exerciseValidator = getNewExerciseValidator(exerciseForm);
		policyValidator = getNewPolicyValidator(policyForm);
		objectValidator = getNewObjectValidator(objectForm);
		
		exerciseForm.setValidator(exerciseValidator);
		policyForm.setValidator(policyValidator);
		objectForm.setValidator(objectValidator);
	}
	
	private InsurancePolicyHeaderFormValidator getNewPolicyValidator(InsurancePolicyHeaderForm form) {
		return new InsurancePolicyHeaderFormValidator(form) {
			@Override
			public bigBang.library.client.FormValidator.Result validateImpl() {
				Result policyResult = super.validateImpl();
				Result exerciseResult = exerciseValidator.validateImpl();
				policyResult.messages.addAll(exerciseResult.messages);

				return new Result(policyResult.valid && exerciseResult.valid, policyResult.messages);
			}
		};
	}

	private InsuredObjectFormValidator getNewObjectValidator(InsuredObjectForm form) {
		return new InsuredObjectFormValidator(form) {
			@Override
			public bigBang.library.client.FormValidator.Result validateImpl() {
				Result result = super.validateImpl();
				result.valid &= validateInsuredObjectHeaderDates();
				
				return result;
			}
		};
	}
	
	private ExerciseSelectorFormValidator getNewExerciseValidator(ExerciseSelectorForm form) {
		return new ExerciseSelectorFormValidator(form){
			@Override
			public bigBang.library.client.FormValidator.Result validate() {
				Result result = super.validateImpl();
				result.valid &= validateExerciseDates();
				
				return result;
			}
		};
	}

	//Add cross-form validations here
	
	protected boolean validateInsuredObjectHeaderDates() {
		InsurancePolicy policy = policyValidator.getForm().getInfo();
		if(policy != null && policy.statusIcon != null && policy.statusIcon == PolicyStatus.PROVISIONAL) {
			if(objectValidator.getForm().inclusionDate.getValue() != null) {
				objectValidator.getForm().inclusionDate.setWarning(true);
			}
			if(objectValidator.getForm().exclusionDate.getValue() != null) {
				objectValidator.getForm().exclusionDate.setWarning(true);
			}
		}
		return true;
	}

	protected boolean validateExerciseDates(){
		InsurancePolicy policy = policyValidator.getForm().getInfo();
		//TODO
		return true;
	}
	
}
