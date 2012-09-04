package bigBang.module.clientModule.shared;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;

public class ClientFormValidator implements FormValidator {

	public static final class ClientNameValidator implements FieldValidator<String> {
		
		private static String errorMessage;
		
		public boolean isValid(String value) {
			String name = (String) value;
			if(!(name != null && name.length() > 0 && name.length() < 100)){
				errorMessage = "O nome do cliente não é válido";
				return false;
			}
			return true;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public boolean isMandatory() {
			return true;
		}
	}
	
	public static final class TaxNumberValidator implements FieldValidator<String> {

		@Override
		public boolean isValid(String value) {
			return true;
		}

		@Override
		public String getErrorMessage() {
			return null;
		}

		@Override
		public boolean isMandatory() {
			return false;
		}	
	}
	
	public static final class ClientGroupValidator implements FieldValidator<String> {

		@Override
		public boolean isValid(String value) {
			return true;
		}

		@Override
		public String getErrorMessage() {
			return null;
		}

		@Override
		public boolean isMandatory() {
			return false;
		}
		
	}
	
	public static final class NIBValidator implements FieldValidator<String> {

		@Override
		public boolean isValid(String value) {
			return true;
		}

		@Override
		public String getErrorMessage() {
			return null;
		}

		@Override
		public boolean isMandatory() {
			return false;
		}
		
	}
	
	public static final class MediatorValidator implements FieldValidator<String> {

		@Override
		public boolean isValid(String value) {
			return value != null && !value.isEmpty();
		}

		@Override
		public String getErrorMessage() {
			return MANDATORY_FIELD_MESSAGE;
		}

		@Override
		public boolean isMandatory() {
			return true;
		}

	}
	
	public static final class ProfileValidator implements FieldValidator<String> {

		@Override
		public boolean isValid(String value) {
			return value != null && !value.isEmpty();
		}

		@Override
		public String getErrorMessage() {
			return MANDATORY_FIELD_MESSAGE;
		}

		@Override
		public boolean isMandatory() {
			return false;
		}
		
	}
	
}
