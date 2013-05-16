package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class DeleteExpenseFormValidator extends
FormValidator<DeleteExpenseForm> {

	public DeleteExpenseFormValidator(DeleteExpenseForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateReason();

		return new Result(valid, this.validationMessages);
	}

	private boolean validateReason() {
		return validateString(form.reason, 1, 250, false);
	}

}
