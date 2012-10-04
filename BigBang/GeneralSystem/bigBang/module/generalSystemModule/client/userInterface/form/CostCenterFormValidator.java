package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CostCenterFormValidator extends FormValidator<CostCenterForm> {

	public CostCenterFormValidator(CostCenterForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateCode();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName() {
		return validateString(form.nameField, 1, 250, false);
	}
	
	private boolean validateCode(){
		return validateString(form.codeField, 1, 250, false);
	}

}
