package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Date;

import bigBang.library.client.FormValidator;

public class ExerciseSelectorFormValidator extends
FormValidator<ExerciseSelectorForm> {

	public ExerciseSelectorFormValidator(ExerciseSelectorForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateExercises();
		valid &= validateStartDate();
		valid &= validateEndDate();
		valid &= validateStartDateAndEndDate();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateExercises() {
		return true;
	}

	private boolean validateStartDate() {
		if(form.exercises.getValue() != null) {
			return validateDate(form.startDate, false);
		}else{
			return true;
		}
	}

	private boolean validateEndDate() {
		if(form.exercises.getValue() != null) {
			return validateDate(form.endDate, false);
		}else{
			return true;
		}
	}

	private boolean validateStartDateAndEndDate(){
		if(form.exercises.getValue() != null) {
			boolean validDates = validateStartDate() && validateEndDate();
			if(validDates) {
				Date startDate = form.startDate.getValue();
				Date endDate = form.endDate.getValue();
				if(startDate != null && endDate != null) {
					if(startDate.before(endDate)) {
						return true;
					}else{
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
		}else{
			return true;
		}
	}

}
