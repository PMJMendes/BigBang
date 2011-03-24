package bigBang.module.generalSystemModule.shared.formValidator;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormValidator;
import bigBang.library.shared.Address;

public class MediatorFormValidator implements FormValidator {

	public static final class NameValidator implements FieldValidator<String> {

		private String errorMessage;
		
		@Override
		public boolean isValid(String value) {
			boolean result = value.length() > 0;
			if(!result)
				errorMessage = "Por favor preencha o nome do mediador";
			return result;
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
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
			boolean result = value.length() > 0;
			if(!result)
				errorMessage = "Por favor preencha o número do ISP";
			return result;
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
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
			boolean result = value.length() > 0;
			if(!result)
				errorMessage = "Por favor preencha o número de contribuinte";
			return result;
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
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
			return true; //TODO
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
		}

		@Override
		public boolean isMandatory() {
			return false;
		}
		
	}
	
	public static final class ComissionProfileValidator implements FieldValidator<String> {

		private String errorMessage;

		@Override
		public boolean isValid(String value) {
			boolean result = value.length() > 0;
			if(!result)
				errorMessage = "Por favor indique o perfil de comissionamento";
			return result; 
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
		}

		@Override
		public boolean isMandatory() {
			return true;
		}
		
	}
	
	public static final class AddressValidator implements FieldValidator<Address> {

		private String errorMessage;
		
		@Override
		public boolean isValid(Address value) {
			boolean result = value.street1.length() > 0; //TODO
			if(!result)
				errorMessage = "Por favor indique o perfil de comissionamento";
			return result; 
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
		}

		@Override
		public boolean isMandatory() {
			return true;
		}
		
	}
	
}
