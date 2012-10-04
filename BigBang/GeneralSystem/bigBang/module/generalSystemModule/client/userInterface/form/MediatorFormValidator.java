package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class MediatorFormValidator extends FormValidator<MediatorForm> {

	public MediatorFormValidator(MediatorForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateIspNumber();
		valid &= validateTaxNumber();
		valid &= validateCommissionProfile();
		valid &= validateCommissionPercentage();
		valid &= validateAddress();
		valid &= validateHasRetention();
		valid &= validateNib();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName(){
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateIspNumber(){
		return validateString(form.ISPNumber, 1, 250, false);
	}
	
	private boolean validateTaxNumber(){
		return validateString(form.taxNumber, 1, 250, false);
	}
	
	private boolean validateCommissionProfile() {
		return validateGuid(form.comissionProfile, false);
	}
	
	private boolean validateCommissionPercentage(){
		return validateNumber(form.commissionPercentage, true);
	}
	
	private boolean validateAddress(){
		return validateAddress(form.address, true);
	}
	
	private boolean validateHasRetention(){
		return form.hasRetention.getValue() != null;
	}
	
	private boolean validateNib(){
		return validateString(form.NIB, 0, 250, true);
	}
	
	
	

}
