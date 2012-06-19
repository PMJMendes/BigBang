package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.TaxFormValidator;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class TaxForm extends FormView<Tax> {

	private ExpandableListBoxFormField type;
	private TextBoxFormField name, unitsLabel, columnOrder, tag;
	private FormField<?> defaultValue;
	private CheckBoxFormField variesByObject, variesByExercise, mandatory, visible;
	private ExpandableListBoxFormField refersToEntityId;

	private Tax tax;
	private boolean readOnly;
	private String coverageId;

	public TaxForm() {

		name = new TextBoxFormField("Designação", new TaxFormValidator.NameValidator());
		name.setFieldWidth("360px");
		name.setLabelWidth("429px");
		unitsLabel = new TextBoxFormField("Unidade");
		unitsLabel.setFieldWidth("150px");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldTypes, "Tipo", new TaxFormValidator.UnitValidator());
		type.allowEdition(false);
		refersToEntityId = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ObjectIds, "Refere-se a");
		refersToEntityId.allowEdition(false);
		variesByObject = new CheckBoxFormField("Varia por unidade de risco");
		variesByExercise = new CheckBoxFormField("Varia por exercício");
		mandatory = new CheckBoxFormField("Obrigatório");
		visible = new CheckBoxFormField("Visível");
		visible.setValue(true);
		tag = new TextBoxFormField("Tag");
		tag.setFieldWidth("360px");
		columnOrder = new TextBoxFormField("Índice da coluna");
		columnOrder.setFieldWidth("100px");
		defaultValue = new RadioButtonFormField("Valor por defeito");

		addSection("Detalhes do campo");
		addFormFieldGroup(new FormField<?>[]{
				name,
				unitsLabel,
		}, false);

		addFormFieldGroup(new FormField<?>[]{
				type,
				refersToEntityId,
				tag, 
		},false);

		addFormFieldGroup(new FormField<?>[]{variesByObject}, true);
		addFormFieldGroup(new FormField<?>[]{visible}, false);
		addFormFieldGroup(new FormField<?>[]{variesByExercise}, true);
		addFormFieldGroup(new FormField<?>[]{mandatory}, false);

		type.setLabelWidth("429px");

		addFormFieldGroup(new FormField<?>[]{columnOrder}, true);

		type.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				prepareDefaultValue(event.getValue(), null);
			}
		}); 
	}

	@Override
	public Tax getInfo() {

		Tax newTax = tax;
		tax.coverageId = coverageId;
		newTax.columnOrder = Integer.parseInt(columnOrder.getValue());
		newTax.fieldTypeId = type.getValue();
		if(newTax.fieldTypeId != null)
			newTax.defaultValue = newTax.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.DateType) ? ((DatePickerFormField)defaultValue).getStringValue() : (String)defaultValue.getValue();
			newTax.mandatory = mandatory.getValue();
			newTax.name = name.getValue();
			newTax.refersToEntityId = refersToEntityId.getValue();
			newTax.unitsLabel = unitsLabel.getValue();
			newTax.variesByExercise = variesByExercise.getValue();
			newTax.variesByObject = variesByObject.getValue();

			return newTax;
	}
	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		super.setReadOnly(readOnly);
		if(type != null && type.getValue() != null && !type.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
			refersToEntityId.setReadOnly(true);
		}
	};

	@Override
	public void setInfo(Tax info) {

		tax = info;
		name.setValue(info.name);
		type.setValue(info.fieldTypeId, false);
		prepareDefaultValue(info.fieldTypeId, info.defaultValue);
		variesByExercise.setValue(info.variesByExercise);
		variesByObject.setValue(info.variesByObject);
		mandatory.setValue(info.mandatory);
		refersToEntityId.setValue(info.refersToEntityId);
		unitsLabel.setValue(info.unitsLabel);
		columnOrder.setValue(""+info.columnOrder);

	}

	private void prepareDefaultValue(String fieldTypeId, String newDefaultValue){
		if(defaultValue.isAttached()){
			defaultValue.removeFromParent();
		}
		
		refersToEntityId.setReadOnly(true);

		if(fieldTypeId == null || fieldTypeId.isEmpty()){
			return;
		}


		if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.DateType)){
			defaultValue = new DatePickerFormField("Valor por defeito");	
			((DatePickerFormField)defaultValue).setValue(newDefaultValue);
		}
		else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.BooleanType)){
			defaultValue = new RadioButtonFormField("Valor por defeito");
			((RadioButtonFormField)defaultValue).addOption("1", "Sim");
			((RadioButtonFormField)defaultValue).addOption("0", "Não");
			((RadioButtonFormField)defaultValue).addOption("", "Não definido");
			((RadioButtonFormField)defaultValue).setValue(newDefaultValue);
		}else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ListType)){
			defaultValue = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldValues+"/"+BigBangConstants.EntityIds.TAX, "Valor por defeito");
			((ExpandableListBoxFormField)defaultValue).setValue(newDefaultValue);
		}else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.NumericType)){
			defaultValue = new TextBoxFormField("Valor por defeito");
			defaultValue.setFieldWidth("150px");
			((TextBoxFormField)defaultValue).setValue(newDefaultValue);
		}else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
			defaultValue = new ExpandableListBoxFormField(refersToEntityId.getValue(), "Valor por defeito");
			((ExpandableListBoxFormField)defaultValue).setValue(newDefaultValue);
		}else {
			//TEXT TYPE
			defaultValue = new TextBoxFormField("Valor por defeito");
			((TextBoxFormField)defaultValue).setValue(newDefaultValue);
			defaultValue.setFieldWidth("150px");
		}
		addFormFieldGroup(new FormField<?>[]{defaultValue}, false);
		defaultValue.setReadOnly(readOnly);
	}

	@Override
	public void clearInfo() {
		this.tax = new Tax();
		super.clearInfo();
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}


}
