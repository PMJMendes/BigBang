package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.library.client.FormField;
import bigBang.library.client.FormValidator;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class TaxFormValidator extends FormValidator<TaxForm> {

	public TaxFormValidator(TaxForm form) {
		super(form);
	}

	@Override
	public Result validateImpl() {
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
		return validateGuid(form.refersToEntityId,
				!ModuleConstants.PolicyFieldTypes.ReferenceType.equalsIgnoreCase(form.type.getValue()));
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

	@SuppressWarnings("unchecked")
	private boolean validateDefaultValue() {
		try {
			if(ModuleConstants.PolicyFieldTypes.DateType.equalsIgnoreCase(form.type.getValue())){
				return validateDate((DatePickerFormField)form.defaultValue, true);
			} else if(ModuleConstants.PolicyFieldTypes.BooleanType.equalsIgnoreCase(form.type.getValue())){
				return true;
			} else if(ModuleConstants.PolicyFieldTypes.ListType.equalsIgnoreCase(form.type.getValue())){
				return validateGuid((FormField<String>)form.defaultValue, true);
			} else if(ModuleConstants.PolicyFieldTypes.NumericType.equalsIgnoreCase(form.type.getValue())){
				return validateNumber((FormField<Double>)form.defaultValue, true);
			} else if(ModuleConstants.PolicyFieldTypes.ReferenceType.equalsIgnoreCase(form.type.getValue())){
				return validateGuid((FormField<String>)form.defaultValue, true);
			}else {
				return validateString((FormField<String>)form.defaultValue, 0, 250, true);
			}
		} catch (ClassCastException e) {
			return false;
		}
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
