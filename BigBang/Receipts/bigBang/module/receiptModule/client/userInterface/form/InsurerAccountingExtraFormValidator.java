package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.FormValidator;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.module.receiptModule.client.userInterface.form.InsurerAccountingExtraForm.InsurerAccountingExtraFormItemSection;

public class InsurerAccountingExtraFormValidator extends
FormValidator<InsurerAccountingExtraForm> {

	public InsurerAccountingExtraFormValidator(InsurerAccountingExtraForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateItems();
		return new Result(valid, this.validationMessages);
	}

	private boolean validateItems() {

		boolean valid = true;
		
		for(int i = 0; i<form.extras.size(); i++){
			InsurerAccountingExtraFormItemSection extra = form.extras.get(i);
			valid &= validateGuid(extra.insurerId, false);
			valid &= validateNumber(extra.value, 0.0, null, false);
			valid &= validateString(extra.description, 1, 250, false);
			valid &= validateInsurer(extra.insurerId, i);
		}

		return valid;
	}

	private boolean validateInsurer(ExpandableListBoxFormField insurerId, int curr) {
		
		if(insurerId.getValue() == null){
			return false;
		}
		
		for(int i = curr+1; i<form.extras.size(); i++){
			if(insurerId.getValue().equalsIgnoreCase(form.extras.get(i).insurerId.getValue())){
				form.extras.get(i).insurerId.setInvalid(true);
				return false;
			}
		}
		
		return true;
	
	}

}
