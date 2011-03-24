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
				errorMessage = "Deve preencher o nome para o centro de custo";
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
				errorMessage = "Deve preencher um código par ao centro de custo";
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
