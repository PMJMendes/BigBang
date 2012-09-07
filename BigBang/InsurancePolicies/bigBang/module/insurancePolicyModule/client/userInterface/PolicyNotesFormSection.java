package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class PolicyNotesFormSection extends FormViewSection implements HasValue<String>{

	TextAreaFormField area;
	
	public PolicyNotesFormSection() {
		super("Notas");
		area = new TextAreaFormField();
		addFormField(area);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		return area.getValue();
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		area.setValue(value);
		
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

}
