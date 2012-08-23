package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.event.ContentChangedEvent;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaFormField extends FormField<String> {

	protected boolean hasDummyValue = false;
	protected VerticalPanel wrapper;
	protected HorizontalPanel textAndMandatory;
	protected int max;
	protected Label remainCharsNum;


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
		}else
			this.label.setText(label);
	}

	@Override
	public void setLabelWidth(String width) {
		wrapper.setCellWidth(this.label, width);
	}

	public TextAreaFormField(){
		super();
		sinkEvents(Event.ONPASTE);
		wrapper = new VerticalPanel();
		initWidget(wrapper);

		textAndMandatory = new HorizontalPanel();
		this.label = new Label();
		wrapper.add(this.label);
		this.field = new TextArea();
		textAndMandatory.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		textAndMandatory.add((Widget) this.field);
		textAndMandatory.add(mandatoryIndicatorLabel);
		wrapper.add(textAndMandatory);
		wrapper.add(errorMessageLabel);

		setMaxCharacters(250, null);

		setFieldWidth("400px");
		setFieldHeight("100px");
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		fireEvent(new ContentChangedEvent());
		switch (DOM.eventGetType(event)) {
		case Event.ONPASTE: {
			if(max > 0){
				textAreaContentChanged(max, remainCharsNum);
			}
			break;
		}
		}

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
		field.setEnabled(!readOnly);
		field.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
		field.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readOnly && this.isMandatory());

	}

	public void setFieldHeight(String height){
		((UIObject) this.field).setHeight(height);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		if((value == null || value.isEmpty()) && isReadOnly()){
			field.setValue("-");
			hasDummyValue = true;
		}
		else {
			hasDummyValue = false;
			super.setValue(value, fireEvents);
		}
	}

	@Override
	public String getValue() {
		String value = super.getValue();
		if(value != null && value.isEmpty()|| (value != null && value.equals("-"))){
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

	public void setMaxCharacters(final int max, final Label remainCharsNum){

		this.max = max;
		this.remainCharsNum = remainCharsNum;

		((TextArea)this.field).addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireEvent(new ContentChangedEvent());
				textAreaContentChanged(max, remainCharsNum);
			}
		});


		((TextArea)this.field).addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				textAreaContentChanged(max, remainCharsNum);
				fireEvent(new ContentChangedEvent());
			}
		});

		((TextArea)this.field).addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				textAreaContentChanged(max, remainCharsNum);
				fireEvent(new ContentChangedEvent());
			}
		});


	}

	protected void textAreaContentChanged(int max, Label remainCharsNum) {	

		int counter = new Integer(((TextArea)this.field).getText().length()).intValue();
		int charsRemaining = max - counter;
		if(charsRemaining < 0)
			charsRemaining = 0;

		if(remainCharsNum != null) {
			remainCharsNum.setText("" + charsRemaining);
		}

		if (charsRemaining <= 0){
			((TextArea)this.field).setText(((TextArea)this.field).getText().substring(0, max));
		}
	}

	public TextArea getNativeField(){
		return (TextArea)this.field;
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
	}

	@Override
	public void focus() {
		((TextArea)this.field).getElement().focus();
	}
}
