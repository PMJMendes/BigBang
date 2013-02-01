package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.FormValidator;

public class TotalLossFileFormValidator extends
		FormValidator<TotalLossFileForm> {

	public TotalLossFileFormValidator(TotalLossFileForm form) {
		super(form);
	}

	@Override
	protected bigBang.library.client.FormValidator.Result validateImpl() {
		boolean valid = true;

		valid &= validateSalvageType();
		valid &= validateCapital();
		valid &= validateDeductible();
		valid &= validateSettlement();
		valid &= validateSalvageValue();
		valid &= validateSalvageBuyer();
	
		return new Result(valid, validationMessages);
	}

	private boolean validateSalvageType() {
		return validateGuid(form.salvageType, true);
	}

	private boolean validateCapital() {
		return validateNumber(form.capital, 0.0, null,true);
	}

	private boolean validateDeductible() {
		return validateNumber(form.deductible, 0.0, null,true);
	}

	private boolean validateSettlement() {
		return validateNumber(form.settlement, 0.0, null,true);
	}

	private boolean validateSalvageValue() {
		return validateNumber(form.salvageValue, 0.0, null,true);
	}

	private boolean validateSalvageBuyer() {
		return validateString(form.salvageBuyer, 0, 250, true);
	}

}
