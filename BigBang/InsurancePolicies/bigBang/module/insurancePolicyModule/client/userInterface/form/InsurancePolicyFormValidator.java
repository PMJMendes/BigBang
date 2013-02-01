package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InsurancePolicyFormValidator extends
FormValidator<InsurancePolicyForm> {
	
	private InsurancePolicyHeaderFormValidator parentValidator;

	public InsurancePolicyFormValidator(InsurancePolicyForm form) {
		super(form);
		parentValidator = new InsurancePolicyHeaderFormValidator(form);
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
		return validateString(form.notes, 0, 250, true);
	}
}
