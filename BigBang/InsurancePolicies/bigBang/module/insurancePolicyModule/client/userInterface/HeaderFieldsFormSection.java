package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicyOLD.HeaderField;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.view.FormViewSection;

public class HeaderFieldsFormSection extends FormViewSection {

	protected Map<HeaderField, GenericFormField> policyFields;

	public HeaderFieldsFormSection() {
		super("Detalhes de Modalidade");
		policyFields = new HashMap<HeaderField, GenericFormField>();
	}
	
	public void setPolicyFields(HeaderField[] fields) {
		clearFields();
		if(fields != null) {
			for(HeaderField field : fields) {
				GenericFormField formField = null;

				switch(field.type) {
				case TEXT:
					formField = new GenericFormField(TYPE.TEXT);
					break;
				case LIST:
					formField = new GenericFormField(TYPE.LIST);
					formField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+field.fieldId);
					break;
				case REFERENCE:
					formField = new GenericFormField(TYPE.REFERENCE);
					formField.setListId(field.refersToId);
					break;
				case NUMERIC:
					formField = new GenericFormField(TYPE.NUMBER);
					break;
				case BOOLEAN:
					formField = new GenericFormField(TYPE.BOOLEAN);
					break;
				case DATE:
					formField = new GenericFormField(TYPE.DATE);
					break;
				}

				formField.setFieldWidth("175px");
				formField.setUnitsLabel(field.unitsLabel);
				formField.setValue(field.value);
				formField.setLabel(field.fieldName);
				formField.setReadOnly(readOnly);
				this.policyFields.put(field, formField);
				addFormField(formField, true);
			}
		}else{
			clearFields();
		}
	}

	public HeaderField[] getPolicyFields() {
		HeaderField[] result = new HeaderField[this.policyFields.size()];
		
		int i = 0;
		for(HeaderField field : policyFields.keySet()) {
			GenericFormField formField = this.policyFields.get(field);
			result[i] = field;
			result[i].value = formField.getValue();
			i++;
		}
		return result;
	}

	public void clearFields(){
		for(GenericFormField field : policyFields.values()) {
			unregisterFormField(field);
		}
		this.policyFields.clear();
	}

}
