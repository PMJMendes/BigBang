package bigBang.module.generalSystemModule.shared.formValidator;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;
import bigBang.library.shared.validator.AddressFieldValidator;

public class InsuranceAgencyFormValidator implements FormValidator {

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
	
	public static final class AcronymValidator implements FieldValidator<String> {

		private String errorMessage;
		
		@Override
		public boolean isValid(String value) {
			boolean valid = value.length() > 0 && value.length() <= 3;
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
	
	public static final class ISPNumberValidator implements FieldValidator<String> {

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
	
	public static final class OwnMediatorCodeValidator implements FieldValidator<String> {

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
	
	public static final class TaxNumberValidator implements FieldValidator<String> {

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
	
	public static final class NIBValidator implements FieldValidator<String> {

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
	
	public static final class AddressValidator extends AddressFieldValidator {}
	
}
