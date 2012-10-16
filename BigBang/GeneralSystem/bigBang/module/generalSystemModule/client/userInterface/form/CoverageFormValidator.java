package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class CoverageFormValidator extends FormValidator<CoverageForm> {
	
	public CoverageFormValidator(CoverageForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateIsMandatory();
		valid &= validateIsHeader();
		valid &= validateOrder();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName(){
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateIsMandatory(){
		return form.isMandatory.getValue() != null; 
	}
	
	private boolean validateIsHeader(){
		return form.isHeader.getValue() != null;
	}
	
	private boolean validateOrder(){
		return validateNumber(form.order, true);
	}

}
