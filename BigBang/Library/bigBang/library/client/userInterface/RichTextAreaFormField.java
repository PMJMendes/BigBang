package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;


public class RichTextAreaFormField extends FormField<String> {

	protected boolean hasDummyValue = false;
	protected VerticalPanel wrapper;
	protected HorizontalPanel textAndMandatory;
	protected RichTextArea field;
	private RichTextToolbar toolbar;
	
	
	public RichTextAreaFormField(String label,FieldValidator<String> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}
	
	public RichTextAreaFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}

	public RichTextAreaFormField(String label) {
		this();
		setLabel(label);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			wrapper.setCellWidth(this.label, "0px");
			this.label.setText("");
		}else
			this.label.setText(label);
	}
	
	public RichTextAreaFormField(){
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		this.label = new Label();
		wrapper.add(this.label);
		this.field = new RichTextArea();
		this.field.setSize("400px", "300px");
		
		toolbar = new RichTextToolbar(this.field);
		
		VerticalPanel fieldWrapper = new VerticalPanel();
		fieldWrapper.setSize("100%", "100%");
		fieldWrapper.add(toolbar);
		fieldWrapper.add(this.field);
		fieldWrapper.setCellHeight(this.field, "100%");
		textAndMandatory = new HorizontalPanel();
		textAndMandatory.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textAndMandatory.add(fieldWrapper);
		textAndMandatory.add(mandatoryIndicatorLabel);
		wrapper.add(textAndMandatory);
		wrapper.add(errorMessageLabel);
		setFieldWidth("400px");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		if(!field.isEnabled() != readOnly){
			if(readOnly){
				if(field.getHTML().equals("")){
					field.setHTML("-");
					hasDummyValue = true;
				}
			}else{
				if(hasDummyValue){
					field.setHTML("");
					hasDummyValue = false;
				}
			}
		}
		field.setEnabled(!readOnly);
		field.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
		field.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readOnly);
	}
	
	public void setFieldHeight(String height){
		this.field.setHeight(height);
	}
	
	@Override
	public void setFieldWidth(String width) {
		this.field.setWidth(width);
	}
	
	@Override
	public String getValue() {
		return this.field.getHTML();
	}
	
	@Override
	public void setValue(String value, boolean fireEvents) {
		hasDummyValue = false;
		field.setHTML(value);
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}
	
	@Override
	public boolean isReadOnly() {
		return !this.field.isEnabled();
	}

	@Override
	public void setInvalid(boolean invalid) {
		if(field != null){
			if(invalid)
				this.field.addStyleName("invalidFormField");
			else
				this.field.removeStyleName("invalidFormField");
		}
		FieldValidator<?> validator = this.validator;
		String message =  validator == null ? null : validator.getErrorMessage();
		this.errorMessageLabel.setText(message == null ? "Valor inválido" : message);
		this.errorMessageLabel.setVisible(invalid);
	}
	
	@Override
	public void clear() {
		RichTextArea field = this.field;
		field.setHTML(!field.isEnabled() ? "-" : "");
		if(!field.isEnabled())
			hasDummyValue = true;
	}
	
	@Override
	public void setLabelWidth(String width) {
		return;
	}

	public UIObject getNativeField() {
		return field;
	}

	public void showToolbar(boolean b) {
		toolbar.setVisible(b);
	}

	@Override
	public void focus() {
		field.getElement().focus();
	}

}
