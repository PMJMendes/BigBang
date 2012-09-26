package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.FieldContainer.HeaderField;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.view.FormViewSection;

public class HeaderFieldsSection extends FormViewSection implements HasValue<HeaderField[]>{

	private GenericFormField[] formFields;
	private HeaderField[] value;

	public HeaderFieldsSection() {
		super("");
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<HeaderField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public HeaderField[] getValue() {
		if(value == null)
			return new HeaderField[0];
		
		HeaderField[] fields = value;

		for(int i = 0; i<fields.length; i++){
			fields[i].value = formFields[i].getValue();
		}

		return fields;
	}

	@Override
	public void setValue(HeaderField[] value) {
		setValue(value, true);
	}

	@Override
	public void setValue(HeaderField[] value, boolean fireEvents) {
		
		unregisterAllFormFields();
		this.clear();
		this.value = value;
		
		if(value == null || value.length == 0){
			this.setVisible(false);
			return;
		}
		
		formFields = new GenericFormField[value.length];

		for(int i = 0; i<formFields.length; i++){
			switch(value[i].type){
			case BOOLEAN:
				formFields[i] = new GenericFormField(TYPE.BOOLEAN);
				break;
			case DATE:
				formFields[i] = new GenericFormField(TYPE.DATE);
				break;
			case LIST:
				formFields[i] = new GenericFormField(TYPE.LIST);
				formFields[i].setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+value[i].fieldId);
				break;
			case NUMERIC:
				formFields[i] = new GenericFormField(TYPE.NUMBER);
				break;
			case REFERENCE:
				formFields[i] = new GenericFormField(TYPE.REFERENCE);
				formFields[i].setListId(value[i].refersToId);
				break;
			case TEXT:
				formFields[i] = new GenericFormField(TYPE.TEXT);
				break;
			}

			formFields[i].setValue(value[i].value);
			formFields[i].setReadOnly(true);
			formFields[i].setLabel(value[i].fieldName);
			formFields[i].setUnitsLabel(value[i].unitsLabel);
			formFields[i].setEditable(!value[i].readOnly);
			formFields[i].setReadOnly(this.readOnly);
			addFormField(formFields[i], true);
		}
		
		this.setVisible(true);

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}
}
