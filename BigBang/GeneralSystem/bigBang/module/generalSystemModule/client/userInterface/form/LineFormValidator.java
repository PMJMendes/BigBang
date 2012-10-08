package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class LineFormValidator extends FormValidator<LineForm> {

	public LineFormValidator(LineForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateCategory();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName(){
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateCategory(){
		return validateGuid(form.category, false);
	}

}
