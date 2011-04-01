package bigBang.module.generalSystemModule.shared.formValidator;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;

public class CostCenterFormValidator implements FormValidator {

	public static final class NameValidator implements FieldValidator<String> {

		private String errorMessage;
		
		@Override
		public boolean isValid(String value) {
			boolean valid = value.length() > 0;
			if(!valid)
				errorMessage = MANDATORY_FIELD_MESSAGE;
			return valid;
		}

		@Override
		public String getErrorMessage() {
			return this.errorMessage;
		}

		@Override
		public boolean isMandatory() {
			return true;
		}
		
	}
	
	public static final class CodeValidator implements FieldValidator<String> {

		private String errorMessage;
		
		@Override
		public boolean isValid(String value) {
			boolean valid = value.length() > 0;
			if(!valid)
				errorMessage = MANDATORY_FIELD_MESSAGE;
			return valid;
		}

		@Override
		public String getErrorMessage() {
			return this.errorMessage;
		}

		@Override
		public boolean isMandatory() {
			return true;
		}
		
	}
	
}
