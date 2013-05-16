package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class SubPolicyFormValidator extends FormValidator<SubPolicyForm> {

	SubPolicyHeaderFormValidator parentValidator;

	public SubPolicyFormValidator(SubPolicyForm form) {
		super(form);
		parentValidator = new SubPolicyHeaderFormValidator(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {

		if(parentValidator == null)
			return null;

		Result result = parentValidator.validate();

		result.valid &= validateNotes();
		result.messages.addAll(this.validationMessages);

		return result;
	}

	private boolean validateNotes() {
		return validateString(form.notesSection, 0, 250, true);
	}

}
