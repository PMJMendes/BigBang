package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.FieldContainer.ExtraField;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.view.FormViewSection;

public class ExtraFieldsSection extends FormViewSection implements HasValue<ExtraField[]>{

	private GenericFormField[] formFields;
	private ExtraField[] value;
	private Coverage[] coverages;

	public ExtraFieldsSection() {
		super("Detalhes Adicionais");
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ExtraField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ExtraField[] getValue() {
		ExtraField[] fields = value;

		for(int i = 0; i<fields.length; i++){
			fields[i].value = formFields[i].getValue();
		}

		return fields;	}

	@Override
	public void setValue(ExtraField[] value) {
		setValue(value, true);		
	}

	@Override
	public void setValue(ExtraField[] value, boolean fireEvents) {
		
		this.clear();
		
		if(coverages == null){
			return;
		}

		Label temp = new Label();
		int tempIndex = value[0].coverageIndex;
		temp.setText(coverages[tempIndex].coverageName);
		addWidget(temp, false);
		this.value = value;
		formFields = new GenericFormField[value.length];

		for(int i = 0; i<formFields.length; i++){
			if(tempIndex != value[i].coverageIndex){
				temp = new Label();
				tempIndex = value[i].coverageIndex;
				temp.setText(coverages[tempIndex].coverageName);
				addWidget(temp, false);
			}
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
			formFields[i].setLabel(value[i].fieldName);
			formFields[i].setUnitsLabel(value[i].unitsLabel);
			formFields[i].setReadOnly(value[i].readOnly);

			addFormField(formFields[i], (i < formFields.length - 1 && value[i+1].coverageIndex == tempIndex));
		}

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}

	}

	public void setCoveragesExtraFields(Coverage[] coverages){
		this.coverages = coverages;
	}

}
