package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormValidator;

public class ReceiptTransferToOwnerFormValidator extends
		FormValidator<ReceiptTransferToOwnerForm> {

	public ReceiptTransferToOwnerFormValidator(ReceiptTransferToOwnerForm form) {
		super(form);
	}

	@Override
	public bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;
		valid &= validateOwnerNumber();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateOwnerNumber() {
		if ( BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(form.referenceType.getValue()))
			return validatePolicyNumber();
		else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(form.referenceType.getValue()))
			return validateSubPolicyNumber();
		else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(form.referenceType.getValue()))
			return validateSubCasualtyNumber();

		return false;
	}

	private boolean validatePolicyNumber() {
		return validateGuid(form.policyReference, false);
	}

	private boolean validateSubPolicyNumber() {
		return validateGuid(form.subPolicyReference, false);
	}

	private boolean validateSubCasualtyNumber() {
		return validateGuid(form.subCasualtyReference, false);
	}

}
