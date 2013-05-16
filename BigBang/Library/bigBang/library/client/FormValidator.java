package bigBang.library.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.regexp.shared.RegExp;

import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.OutgoingMessageFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public abstract class FormValidator<T extends FormView<?>> {

	/**
	 * The result of the form validation
	 */
	public static class Result {
		public Collection<String> messages;
		public boolean valid = false;

		public Result(boolean valid, Collection<String> messages){
			this.messages = messages;
			this.valid = valid;
		}
	}

	protected T form;
	protected Collection<String> validationMessages;

	public FormValidator(T form) {
		this.form = form;
		this.validationMessages = new ArrayList<String>();
		validateImpl();
	}

	public T getForm(){
		return this.form;
	}
	
	public Result validate() {
		this.validationMessages.clear();
		return this.validateImpl();
	}

	protected abstract Result validateImpl();

	public boolean validateString(FormField<String> field, int minChar, int maxChar, boolean allowsNull){
		field.setMandatory(!allowsNull);
		if(field instanceof TextAreaFormField) {
			((TextAreaFormField)(field)).setMaxCharacters(maxChar, null);
		}
		if(field instanceof TextBoxFormField) {
			((TextBoxFormField)(field)).setMaxCharacters(maxChar);
		}

		String text = field.getValue();
		if(text == null) {
			field.setInvalid(!allowsNull);
			return allowsNull;
		}else{
			int length = text.length();
			boolean result = length >= minChar && length <= maxChar;
			field.setInvalid(!result);
			return result;
		}
	}

	public boolean validateGuid(FormField<String> field, boolean allowsNull) {
		field.setMandatory(!allowsNull);

		String guid = field.getValue();
		if(guid == null) {
			boolean result = allowsNull;
			field.setInvalid(!result);
			return result;
		}else{
			RegExp regexp = RegExp.compile("^(\\{){0,1}[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}(\\}){0,1}$");
			boolean result = regexp.exec(guid) != null;
			field.setInvalid(!result);
			return result;
		}
	}

	public boolean validateNumber(FormField<Double> field, boolean allowsNull){
		field.setMandatory(!allowsNull);
		boolean valid = !(field.getValue() == null && !allowsNull);
		field.setInvalid(!valid);
		return valid; 
	}

	public boolean validateNumber(FormField<Double> field, Double minValue, Double maxValue, boolean allowsNull){
		field.setMandatory(!allowsNull);

		boolean result = true;
		
		Double value = field.getValue();
		if(value == null) {
			result &= allowsNull;
			field.setInvalid(!result);
			return result;
		}else{
			if(minValue != null) {
				result &= minValue <= value;
				field.setInvalid(!result);
			}
			if(maxValue != null) {
				result &= maxValue >= value;
				field.setInvalid(!result);
			}
			return result;
		}
	}

	public boolean validateAddress(FormField<Address> field, boolean allowsNull){
		field.setMandatory(!allowsNull);
		Address address = field.getValue();
		return !(address == null && !allowsNull);
		//TODO Validate inner Address object
	}

	public boolean validateDate(DatePickerFormField field, boolean allowsNull){
		field.setMandatory(!allowsNull);
		Date value = null;
		try{
			value = field.getValueForValidation();
		}catch (Exception e) {
			field.setInvalid(true);
			return false;
		}

		if(value == null) {
			boolean result = allowsNull;
			field.setInvalid(!result);
			return result;
		}else{
			field.setInvalid(false);
			return true;
		}
	}

	public boolean validateOutgoingMessage(OutgoingMessageFormField field, boolean allowsNull) {
		field.setMandatory(!allowsNull);
		OutgoingMessage message = field.getValue();

		if(message == null) {
			boolean result = allowsNull;
			field.setInvalid(!result);
			return result;
		}else{
			boolean valid = true;
			valid &= message.subject != null && !message.subject.isEmpty();
			valid &= message.text != null && !message.text.isEmpty();
			//TODO verificar forwards
			return valid;
		}
	}
	
}
