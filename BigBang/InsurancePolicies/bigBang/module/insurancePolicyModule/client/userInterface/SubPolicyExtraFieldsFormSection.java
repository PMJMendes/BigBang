package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.view.FormViewSection;

public class SubPolicyExtraFieldsFormSection extends FormViewSection {

	protected Map<FieldContainer.ExtraField, GenericFormField> policyFields;

	public SubPolicyExtraFieldsFormSection() {
		super("Detalhes Adicionais");
		policyFields = new HashMap<FieldContainer.ExtraField, GenericFormField>();
	}
	
	public void setPolicyFields(FieldContainer.ExtraField[] fields, StructuredFieldContainer.Coverage[] coverages) {
//		clearFields();
//		if(fields != null) {
//			String coverageId = null;
//			
//			for(FieldContainer.ExtraField field : fields) {
//				GenericFormField formField = null;
//
//				switch(field.type) {
//				case TEXT:
//					formField = new GenericFormField(TYPE.TEXT);
//					break;
//				case LIST:
//					formField = new GenericFormField(TYPE.LIST);
//					formField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+field.fieldId);
//					break;
//				case REFERENCE:
//					formField = new GenericFormField(TYPE.REFERENCE);
//					formField.setListId(field.refersToId);
//					break;
//				case NUMERIC:
//					formField = new GenericFormField(TYPE.NUMBER);
//					break;
//				case BOOLEAN:
//					formField = new GenericFormField(TYPE.BOOLEAN);
//					break;
//				case DATE:
//					formField = new GenericFormField(TYPE.DATE);
//					break;
//				}
//
//				if(coverageId == null || !coverageId.equalsIgnoreCase(field.coverageId)){
//					coverageId = field.coverageId;
//					Label coverageDesig = new Label();
//
//					String coverageName = null;
//					for(StructuredFieldContainer.Coverage coverage : coverages) {
//						if(coverage.coverageId.equalsIgnoreCase(field.coverageId)) {
//							coverageName = coverage.coverageName;
//							break;
//						}
//					}
//					
//					coverageDesig.setText(coverageName);
//					coverageDesig.getElement().getStyle().setFontWeight(FontWeight.BOLD);
//					coverageDesig.getElement().getStyle().setProperty("clear", "both");
//					
//					addWidget(coverageDesig, false);
//				}
//				
//				formField.setFieldWidth("175px");
//				formField.setUnitsLabel(field.unitsLabel);
//				formField.setValue(field.value);
//				formField.setLabel(field.fieldName);
//				formField.setReadOnly(readOnly);
//				this.policyFields.put(field, formField);
//				addFormField(formField, true);
//			}
//		}else{
//			clearFields();
//		}
	}

	public FieldContainer.ExtraField[] getPolicyFields() {
		FieldContainer.ExtraField[] result = new FieldContainer.ExtraField[this.policyFields.size()];

		int i = 0;
		for(FieldContainer.ExtraField field : policyFields.keySet()) {
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
		this.clear();
	}

	public void disableCoverageFields(String coverageId) {
//		for(FieldContainer.ExtraField field : policyFields.keySet()) {
//			if(field.coverageId.equalsIgnoreCase(coverageId)){
//				GenericFormField formField = this.policyFields.get(field);
//				formField.clear();
//				formField.setEditable(false);
//			}
//		}
	}

	public void enableCoverageFields(String coverageId) {
//		for(FieldContainer.ExtraField field : policyFields.keySet()) {
//			if(field.coverageId.equalsIgnoreCase(coverageId)){
//				GenericFormField formField = this.policyFields.get(field);
//				formField.setValue(field.value);
//				formField.setEditable(true);
//				formField.setReadOnly(this.readOnly);
//			}
//		}
	}

}
