package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormValidator;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class TaxFormValidator extends FormValidator<TaxForm> {

	public TaxFormValidator(TaxForm form) {
		super(form);
	}

	@Override
	public Result validate() {
		boolean valid = true;
		valid &= validateType();
		valid &= validateName();
		valid &= validateUnitsLabel();
		valid &= validateColumnOrder();
		valid &= validateTag();
		valid &= validateDefaultValue();
		valid &= validateVariesByObject();
		valid &= validateVariesByExercise();
		valid &= validateMandatory();
		valid &= validateVisible();
		valid &= validateRefersToEntity();
		
		return new Result(valid, this.validationMessages);
	}

	private boolean validateType() {
		return validateGuid(form.type, false);
	}

	private boolean validateRefersToEntity() {
		String type = form.type.getValue();
		if(type != null && type.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ListType)) {
			return validateGuid(form.refersToEntityId, false);
		}else if(form.refersToEntityId.getValue() != null){
			return false;
		}else{
			return true;
		}
	}

	private boolean validateVisible() {
		return form.visible.getValue() != null;
	}

	private boolean validateMandatory() {
		return form.mandatory.getValue() != null;
	}

	private boolean validateVariesByExercise() {
		return form.variesByExercise.getValue() != null;
	}

	private boolean validateVariesByObject() {
		return form.variesByObject.getValue() != null;
	}

	private boolean validateDefaultValue() {
		return true;
	}

	private boolean validateTag() {
		return validateString(form.tag, 0, 250, true);
	}

	private boolean validateColumnOrder() {
		return validateNumber(form.columnOrder, false);
	}

	private boolean validateUnitsLabel() {
		return validateString(form.unitsLabel, 0, 250, true);
	}

	private boolean validateName() {
		return validateString(form.name, 1, 250, false);
	}

}
