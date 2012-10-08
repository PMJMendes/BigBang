package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class InsuranceAgencyFormValidator extends
		FormValidator<InsuranceAgencyForm> {

	public InsuranceAgencyFormValidator(InsuranceAgencyForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateAcronym();
		valid &= validateIspNumber();
		valid &= validateOwnMediatorCode();
		valid &= validateTaxNumber();
		valid &= validateNib();
		valid &= validateAddress();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateName(){
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateAcronym() {
		return validateString(form.acronym, 1, 250, false);
	}
	
	private boolean validateIspNumber(){
		return validateString(form.ISPNumber, 1, 250, false);
	}

	private boolean validateOwnMediatorCode(){
		if(form.ownMediatorCodeList == null || form.ownMediatorCodeList.size() == 0) {
			return false;
		}else{
			return validateString(form.ownMediatorCodeList.get(0), 0, 250, true);
		}
	}
	
	private boolean validateTaxNumber(){
		return validateString(form.taxNumber, 1, 250, false);
	}
	
	private boolean validateNib(){
		return validateString(form.NIB, 1, 250, false);
	}
	
	private boolean validateAddress(){
		return validateAddress(form.address, false);
	}
	
}
