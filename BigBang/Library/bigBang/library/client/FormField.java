package bigBang.library.client;

import bigBang.library.client.userInterface.view.View;
import bigBang.library.client.FieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class FormField<T> extends View implements HasValue<T>, Validatable {

	protected HasValue<T> field;
	protected Label errorMessageLabel;
	protected FieldValidator<T> validator;
	protected Label mandatoryIndicatorLabel;
	protected boolean editable = true;
	protected Label label;
	protected Label unitsLabel;

	protected HandlerRegistration handlerRegistration;

	@Override
	protected void initializeView() {
		this.label = new Label();
		label.setStyleName("bigBangFormFieldLabel");
		errorMessageLabel = new Label();
		errorMessageLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		errorMessageLabel.getElement().getStyle().setColor("#F00");
		errorMessageLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		errorMessageLabel.getElement().getStyle().setFontSize(12, Unit.PX);
		errorMessageLabel.setVisible(false);
		mandatoryIndicatorLabel = new Label("*");
		mandatoryIndicatorLabel.setVisible(false);
		unitsLabel = new Label("");
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		getElement().getStyle().setMargin(5, Unit.PX);
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
		this.setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		field.setValue(value, false);
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}

	public void setValidator(FieldValidator<T> validator) {
		this.validator = validator;
		mandatoryIndicatorLabel.setVisible(validator != null && validator.isMandatory());
	}

	public void setFieldWidth(String width) {
		((Widget)this.field).setWidth(width);
	}
	
	public void setReadOnly(boolean readonly){
		setReadOnlyInternal(readonly);
		if(readonly) {
			setInvalid(false);
			setWarning(false);
		}
	}

	public abstract void clear();

	protected abstract void setReadOnlyInternal(boolean readonly);

	public abstract boolean isReadOnly();

	public boolean validate() {
		if(validator == null){
			GWT.log("Validator for form field is null");
			return true;
		}
		boolean isValid = validator.isValid(getValue());
		setInvalid(!isValid);
		return isValid;
	}

	public void setInvalid(boolean invalid){
		if(invalid && !isReadOnly())
			this.addStyleName("invalidFormField");
		else
			this.removeStyleName("invalidFormField");
		//		FieldValidator<?> validator = this.validator; TODO
		//		String message =  validator == null ? null : validator.getErrorMessage();
		//		this.errorMessageLabel.setText(message == null ? "Valor inválido" : message);
		//		this.errorMessageLabel.setVisible(invalid);
	}

	public void setWarning(boolean invalid){
		if(invalid && !isReadOnly())
			this.addStyleName("warningFormField");
		else
			this.removeStyleName("warningFormField");
		//		FieldValidator<?> validator = this.validator; TODO
		//		String message =  validator == null ? null : validator.getErrorMessage();
		//		this.errorMessageLabel.setText(message == null ? "Valor inválido" : message);
		//		this.errorMessageLabel.setVisible(invalid);
	}

	public String getErrorMessage(){
		return this.validator.getErrorMessage();
	}

	public void setMandatory(final boolean mandatory) {
		setValidator(new FieldValidator<T>() {

			@Override
			public boolean isValid(T value) {
				return true;
			}

			@Override
			public String getErrorMessage() {
				return "";
			}

			@Override
			public boolean isMandatory() {
				return mandatory;
			}
		});
	}

	public boolean isMandatory(){
		return this.validator == null ? false : this.validator.isMandatory();
	}

	/**
	 * Sets whether or not this field can be edited at all by the user
	 * @param editable if true, the field can be edited
	 */
	public void setEditable(boolean editable) {
		if(!editable){
			setReadOnlyInternal(true);
		}
		this.editable = editable;
	}

	public void showLabel(boolean show) {
		this.label.setVisible(show);
		if(!show){
		}
	}

	public void setUnitsLabel(String label){
		unitsLabel.setText(label);
	}

	public void setLabelText(String labelText){
		this.label.setText(labelText);
	}

	public abstract void focus();

	public abstract void setLabelWidth(String width);

}
