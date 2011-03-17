package bigBang.library.shared;

import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class FormField<T> extends View implements HasValue<T>, Validatable {

	protected HasValue<T> field;
	protected Label errorMessageLabel;
	protected FieldValidator validator;
	
	private HandlerRegistration handlerRegistration;
	
	public FormField(){
		errorMessageLabel = new Label();
		errorMessageLabel.setVisible(false);
	}
	
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		handlerRegistration = addHandler(handler, ValueChangeEvent.getType());
		return handlerRegistration;
	}
	
	public T getValue() {
		return field.getValue();
	}

	public void setValue(T value) {
		setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		field.setValue(value);
	}
	
	public void setValidator(FieldValidator validator) {
		this.validator = validator;
	}
	
	public void setFieldWidth(String width) {
		((Widget)this.field).setWidth(width);
	}
	
	public abstract void setReadOnly(boolean readonly);
	
	public abstract boolean isReadOnly();
	
	public <T> boolean validate() {
		if(validator == null){
			GWT.log("Validator for form field is null");
			return false;
		}
		boolean isValid = validator.isValid(getValue());
		setInvalid(!isValid);
		return isValid;
	}
	
	protected void setInvalid(boolean invalid){
		if(invalid)
			((Widget)field).addStyleName("invalidFormField");
		else
			((Widget)field).removeStyleName("invalidFormField");
		this.errorMessageLabel.setText(this.validator.getErrorMessage());
		this.errorMessageLabel.setVisible(invalid);
	}
	
	public String getErrorMessage(){
		return this.validator.getErrorMessage();
	}
	
	public boolean isMandatory(){
		return this.validator == null ? false : this.validator.isMandatory();
	}
}
