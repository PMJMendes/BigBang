package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CoverageExerciseDetailsFormValidator extends
		FormValidator<CoverageExerciseDetailsForm> {

	public CoverageExerciseDetailsFormValidator(CoverageExerciseDetailsForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		
		return new Result(valid, this.validationMessages);
	}

}
