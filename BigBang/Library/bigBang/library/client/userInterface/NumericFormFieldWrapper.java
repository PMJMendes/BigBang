package bigBang.library.client.userInterface;

import bigBang.library.client.FormField;

public class NumericFormFieldWrapper extends FormField<String>{

	private NumericTextBoxFormField field;

	public NumericFormFieldWrapper(String title) {
		super();
		field = new NumericTextBoxFormField(title, false);
		initWidget(field);
	}

	public NumericFormFieldWrapper() {
		this("");
	}

	@Override
	public void clear() {
		field.clear();
	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		field.setReadOnlyInternal(readonly);
	}
	
	@Override
	public void setEditable(boolean editable) {
		field.setEditable(editable);
	}

	@Override
	public boolean isReadOnly() {
		return field.isReadOnly();
	}

	@Override
	public void focus() {
		field.focus();
	}

	@Override
	public void setLabelWidth(String width) {
		field.setLabelWidth(width);
	}

	@Override
	public void setValue(String value, boolean fireEvents){
		field.setStringValue(value, fireEvents);
	}
	
	@Override
	public void setValue(String value) {
		field.setStringValue(value, true);
	}

	@Override
	public String getValue() {
		return field.getStringValue();
	}

	@Override
	public void setFieldWidth(String width) {
		field.setFieldWidth(width);
	}

	@Override
	public void setLabelText(String labelText) {
		field.setLabelText(labelText);
	}
	
	@Override
	public void setUnitsLabel(String label) {
		field.setUnitsLabel(label);
	}
	
	
	
}
