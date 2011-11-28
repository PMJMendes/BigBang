package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RadioButtonFormField extends FormField<String> {

	protected HorizontalPanel wrapper;
	protected HorizontalPanel radioWrapper;
	protected Map<RadioButton, String> radioButtons;
	protected String id;
	protected ValueChangeHandler<Boolean> valueChangedHandler;

	public RadioButtonFormField(String label,FieldValidator<String> validator) {
		this();
		setLabel(label);
		setValidator(validator);
	}

	public RadioButtonFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}

	public RadioButtonFormField(String label) {
		this();
		setLabel(label);
	}

	public RadioButtonFormField(){
		super();
		this.id = Math.random()+"";
		
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);
		mainWrapper.add(this.label);
		
		radioButtons = new HashMap<RadioButton, String>();
		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		radioWrapper = new HorizontalPanel();
		wrapper.add(radioWrapper);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
		
		this.valueChangedHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				String value = radioButtons.get(event.getSource());
				setValue(value, true);
			}
		};
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
		wrapper.setCellWidth(this.label, width);
	}	

	public void addOption(String value, String description) {
		RadioButton radio = new RadioButton(this.id, description);
		radio.addValueChangeHandler(this.valueChangedHandler);
		this.radioButtons.put(radio, value);
		radioWrapper.add(radio);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		for(RadioButton r : this.radioButtons.keySet()){
			r.setEnabled(!readOnly);
		}
		mandatoryIndicatorLabel.setVisible(!readOnly && this.isMandatory());
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		if(value == null){
			clear();
		}else{
			for(RadioButton r : this.radioButtons.keySet()){
				r.setValue(radioButtons.get(r).equalsIgnoreCase(value));
			}
		}
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}

	@Override
	public String getValue() {
		for(RadioButton r : this.radioButtons.keySet()) {
			if(r.getValue())
				return radioButtons.get(r);
		}
		return null;
	}

	@Override
	public boolean isReadOnly() {
		for(RadioButton r : this.radioButtons.keySet()) {
			return !r.isEnabled();
		}
		return true;
	}

	@Override
	public void clear() {
		for(RadioButton r : this.radioButtons.keySet()) {
			r.setValue(false);
		}
	}

	@Override
	public void setFieldWidth(String width) {
		// TODO Auto-generated method stub
		//super.setFieldWidth(width);
	}

	@Override
	public void setInvalid(boolean invalid) {
		for(RadioButton r : this.radioButtons.keySet()){
			if(invalid)
				r.addStyleName("invalidFormField");
			else
				r.removeStyleName("invalidFormField");
		}
		FieldValidator<?> validator = this.validator;
		String message =  validator == null ? null : validator.getErrorMessage();
		this.errorMessageLabel.setText(message == null ? "Valor inválido" : message);
		this.errorMessageLabel.setVisible(invalid);
	}

}
