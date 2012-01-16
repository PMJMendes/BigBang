package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaFormField extends FormField<String> {

	protected boolean hasDummyValue = false;
	protected HorizontalPanel wrapper;

	public TextAreaFormField(String label,FieldValidator<String> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	public TextAreaFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}

	public TextAreaFormField(String label) {
		this();
		setLabel(label);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");

		}else{
			this.label.setText(label + ":");
		}
	}

	@Override
	public void setLabelWidth(String width) {
		wrapper.setCellWidth(this.label, width);
	}

	public TextAreaFormField(){
		super();
		wrapper = new HorizontalPanel();
		initWidget(wrapper);

		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new TextArea();
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);

		setFieldWidth("400px");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		TextArea field = ((TextArea)this.field);
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
		mandatoryIndicatorLabel.setVisible(!readOnly);
	}

	public void setFieldHeight(String height){
		((UIObject) this.field).setHeight(height);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		hasDummyValue = false;
		super.setValue(value, fireEvents);
	}

	@Override
	public String getValue() {
		String value = super.getValue();
		if(value != null && value.isEmpty()){
			value = null;
		}
		return value;
	}

	@Override
	public boolean isReadOnly() {
		return ((TextArea)this.field).isReadOnly();
	}

	@Override
	public void clear() {
		TextArea field = ((TextArea)this.field);
		field.setValue(field.isReadOnly() ? "-" : "");
		if(field.isReadOnly())
			hasDummyValue = true;
	}

	//TODO MAKE THIS WORK
	public void setMaxCharacters(final int max){

		((TextArea)this.field).addHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {



				if(event.getCharCode() != KeyCodes.KEY_BACKSPACE && event.getCharCode() != KeyCodes.KEY_DELETE && field.getValue().length() > max){
					event.preventDefault();

				}

			}
		}, KeyPressEvent.getType());

	}

}
