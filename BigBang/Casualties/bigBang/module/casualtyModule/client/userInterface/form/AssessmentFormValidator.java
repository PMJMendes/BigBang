package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class AssessmentFormValidator extends FormValidator<AssessmentForm> {

	public AssessmentFormValidator(AssessmentForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateResult();
		
		return new Result(valid, validationMessages);
	}

	private boolean validateResult() {
		form.result.setInvalid(!form.result.isReadOnly() && form.result.getValue() == null);
		return form.result.isReadOnly() || form.result.getValue() != null;
	}

}
