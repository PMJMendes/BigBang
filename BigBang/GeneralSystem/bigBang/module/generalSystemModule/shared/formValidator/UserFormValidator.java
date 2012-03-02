package bigBang.module.generalSystemModule.shared.formValidator;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;

public class UserFormValidator implements FormValidator {

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
	
	public static final class PasswordValidator implements FieldValidator<String> {

		private String errorMessage;
		
		@Override
		public boolean isValid(String value) {
			boolean valid = value.length() >= 0;
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
			return false;
		}
		
	}
	
	public static final class UsernameValidator implements FieldValidator<String> {

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
	
	public static final class EmailValidator implements FieldValidator<String> {

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
	
	public static final class UserProfileValidator implements FieldValidator<String> {

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
	
	public static final class UserCostCenterValidator implements FieldValidator<String> {

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
