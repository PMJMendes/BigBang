package bigBang.module.clientModule.shared;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;

public class ClientFormValidator implements FormValidator {

	public static final class ClientNameValidator implements FieldValidator {
		
		private static String errorMessage;
		
		public <T> boolean isValid(T value) {
			String name = (String) value;
			if(!(name.length() > 0 && name.length() < 100)){
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
	
}
