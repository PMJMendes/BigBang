package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
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
	private boolean firstTime = true;
	VerticalPanel fieldWrapper;
	
	
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
		wrapper.setSize("100%", "100%");
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		this.label = new Label();
		wrapper.add(this.label);
		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		label.getElement().getStyle().setFontSize(11, Unit.PX);
		this.field = new RichTextArea();
		this.field.setSize("400px", "300px");
		
		toolbar = new RichTextToolbar(this.field);
		
		fieldWrapper = new VerticalPanel();
		fieldWrapper.setSize("100%", "100%");
		fieldWrapper.add(toolbar);
		
		addAttachHandler(new Handler() {
			

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(firstTime && event.isAttached()){
					fieldWrapper.add(field);
					fieldWrapper.setSize("100%", "100%");
					fieldWrapper.setCellHeight(field, "100%");
					fieldWrapper.setSize("100%", "100%");
					fieldWrapper.setCellWidth(field, "100%");
				}
				firstTime = false;
			}
		});
		
		
		textAndMandatory = new HorizontalPanel();
		textAndMandatory.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textAndMandatory.add(fieldWrapper);
		textAndMandatory.setWidth("100%");
		textAndMandatory.setCellWidth(fieldWrapper, "100%");
		textAndMandatory.add(mandatoryIndicatorLabel);
		textAndMandatory.setWidth("100%");
		textAndMandatory.setCellWidth(fieldWrapper, "100%");
		wrapper.add(textAndMandatory);
		wrapper.setCellWidth(textAndMandatory, "100%");
		wrapper.add(errorMessageLabel);
		setFieldWidth("400px");
	}

	@Override
	protected void setReadOnlyInternal(boolean readOnly) {
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
		toolbar.setVisible(!readOnly);
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
