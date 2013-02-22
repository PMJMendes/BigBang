package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormValidator;

public class MediatorFormValidator extends FormValidator<MediatorForm> {

	public MediatorFormValidator(MediatorForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
		boolean valid = true;
		valid &= validateName();
		valid &= validateIspNumber();
		valid &= validateTaxNumber();
		valid &= validateCommissionProfile();
		valid &= validateCommissionPercentage();
		valid &= validateAddress();
		valid &= validateHasRetention();
		valid &= validateAcctCode();
		valid &= validateNib();
		
		return new Result(valid, this.validationMessages);
	}
	
	private boolean validateAcctCode() {
		return validateString(form.acctCode, 3, 3, true);
	}
	
	private boolean validateName(){
		return validateString(form.name, 1, 250, false);
	}
	
	private boolean validateIspNumber(){
		return validateString(form.ISPNumber, 1, 250, true);
	}
	
	private boolean validateTaxNumber(){
		return validateString(form.taxNumber, 1, 250, true);
	}
	
	private boolean validateCommissionProfile() {
		return validateGuid(form.comissionProfile, false);
	}
	
	private boolean validateCommissionPercentage(){
		return validateNumber(form.commissionPercentage,
				!BigBangConstants.TypifiedListValues.MEDIATOR_COMMISSIONING_PROFILE.PERCENTAGE.equalsIgnoreCase(form.comissionProfile.getValue()));
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
