package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class UserFormValidator extends FormValidator<UserForm> {

	public UserFormValidator(UserForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateUserName();
		valid &= validatePassword();
		valid &= validateEmail();
		valid &= validateCostCenter();
		valid &= validateRole();
		valid &= validateDelegate();
		valid &= validatePrinter();
		valid &= validateMediator();
		valid &= validateTitle();
		valid &= validatePhone();

		return new Result(
				valid,
				this.validationMessages);
	}

	//Field validations
	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

	private boolean validateUserName() {
		return validateString(form.username, 1, 250, false);
	}
	
	private boolean validatePassword() {
		return validateString(form.password, 0, 250, true);
	} 

	private boolean validateEmail() {
		return validateString(form.email, 0, 250, true);
	}

	private boolean validateRole() {
		return validateGuid(form.role, false);
	}

	private boolean validateCostCenter() {
		return validateGuid(form.costCenter, false);
	}

	private boolean validateDelegate() {
		return validateGuid(form.delegate, true);
	}

	private boolean validatePrinter() {
		return validateString(form.printers, 0, 250, true);
	}

	private boolean validateMediator() {
		return validateGuid(form.mediator, true);
	} 

	private boolean validateTitle() {
		return validateString(form.title, 0, 250, true);
	} 

	private boolean validatePhone() {
		return validateString(form.phone, 0, 50, true);
	}

}
