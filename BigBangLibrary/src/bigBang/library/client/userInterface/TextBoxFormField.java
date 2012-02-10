package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TextBoxFormField extends FormField<String> {

	protected boolean hasDummyValue = false;
	protected HorizontalPanel wrapper;

	public TextBoxFormField(String label,FieldValidator<String> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	public TextBoxFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}

	public TextBoxFormField(String label) {
		this();
		setLabel(label);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");

		}else{
			this.label.setText(label);
		}
	}

	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);
	}

	public TextBoxFormField(){
		super();

		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		mainWrapper.add(this.label);

		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.field = new TextBox();
		wrapper.add((Widget) this.field);
		wrapper.setCellWidth((Widget)this.field, "100%");
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);

		setFieldWidth("400px");
	}

	@Override
	public void setFieldWidth(String width) {
		super.setFieldWidth(width);

		if(width.equals("100%")){
			this.wrapper.setWidth("100%");
		}
		else
			this.wrapper.setWidth("");


	};

	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		TextBox field = ((TextBox)this.field);
		if(field.isReadOnly() != readOnly){
			if(readOnly){
				if(field.getValue().equals("")){
					field.setValue("-");
					hasDummyValue = true;
				}
			}else{
				if(hasDummyValue){
					field.setValue("");
					hasDummyValue = false;
				}
			}
		}
		field.setReadOnly(readOnly);
		field.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
		field.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readOnly&& this.isMandatory());
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		hasDummyValue = false;
		super.setValue(value, fireEvents);
	}

	@Override
	public boolean isReadOnly() {
		return ((TextBox)this.field).isReadOnly();
	}

	@Override
	public void clear() {
		TextBox field = ((TextBox)this.field);
		field.setValue(field.isReadOnly() ? "-" : "");
		if(field.isReadOnly())
			hasDummyValue = true;
	}

	@Override
	public String getValue() {
		String value = super.getValue();
		if((value != null && value.isEmpty()) || (value != null && value.equals("-"))){
			value = null;
		}
		return value;
	}

	public void setTextAligment(TextAlignment alignment){
		((TextBox)this.field).setAlignment(alignment);
	}

}
