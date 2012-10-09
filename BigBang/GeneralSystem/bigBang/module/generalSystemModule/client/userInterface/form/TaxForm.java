package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.shared.Tax;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class TaxForm extends FormView<Tax> {

	protected ExpandableListBoxFormField type;
	protected TextBoxFormField name, unitsLabel, tag;
	protected FormField<?> defaultValue;
	protected CheckBoxFormField variesByObject, variesByExercise, mandatory, visible;
	protected ExpandableListBoxFormField refersToEntityId;
	protected NumericTextBoxFormField columnOrder;
	
	private Tax tax;
	private String coverageId;

	public TaxForm() {

		name = new TextBoxFormField("Designação");
		name.setFieldWidth("360px");
		name.setLabelWidth("429px");
		unitsLabel = new TextBoxFormField("Unidade");
		unitsLabel.setFieldWidth("150px");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldTypes, "Tipo");
		type.allowEdition(false);
		refersToEntityId = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ObjectIds, "Refere-se a");
		refersToEntityId.allowEdition(false);
		refersToEntityId.setEditable(false);
		variesByObject = new CheckBoxFormField("Varia por unidade de risco");
		variesByExercise = new CheckBoxFormField("Varia por exercício");
		mandatory = new CheckBoxFormField("Obrigatório");
		visible = new CheckBoxFormField("Visível");
		visible.setValue(true);
		tag = new TextBoxFormField("Tag");
		tag.setFieldWidth("275px");
		columnOrder = new NumericTextBoxFormField("Índice da coluna", false);
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
		},false);

		addFormFieldGroup(new FormField<?>[]{visible,columnOrder, tag}, true);
		addFormFieldGroup(new FormField<?>[]{mandatory, variesByExercise, variesByObject}, true);
		
		type.setLabelWidth("429px");

		type.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				prepareDefaultValue(event.getValue(), null);
			}
		});

		refersToEntityId.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				prepareDefaultValue(type.getValue(), null);
			}
		});
		
		addFormField(defaultValue);
		setValidator(new TaxFormValidator(this));
	}

	@Override
	public Tax getInfo() {
		Tax newTax = tax;
		tax.coverageId = coverageId;
		newTax.columnOrder = columnOrder.getValue() == null ? 0 : columnOrder.getValue().intValue();
		newTax.fieldTypeId = type.getValue();

		if(newTax.fieldTypeId != null){
			newTax.defaultValue = newTax.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.DateType) ? ((DatePickerFormField)defaultValue).getStringValue() : (String)defaultValue.getValue();
		}
		newTax.mandatory = mandatory.getValue();
		newTax.name = name.getValue();
		newTax.refersToEntityId = refersToEntityId.getValue();
		newTax.unitsLabel = unitsLabel.getValue();
		newTax.variesByExercise = variesByExercise.getValue();
		newTax.variesByObject = variesByObject.getValue();

		return newTax;
	}

	@Override
	public void setInfo(Tax info) {
		tax = info;
		name.setValue(info.name);
		type.setValue(info.fieldTypeId, false);
		refersToEntityId.setValue(info.refersToEntityId, false);
		prepareDefaultValue(info.fieldTypeId, info.defaultValue);
		variesByExercise.setValue(info.variesByExercise);
		variesByObject.setValue(info.variesByObject);
		mandatory.setValue(info.mandatory);
		unitsLabel.setValue(info.unitsLabel);
		columnOrder.setValue(new Double(info.columnOrder));
	}

	@SuppressWarnings("unchecked")
	private void prepareDefaultValue(String fieldTypeId, final String newDefaultValue){
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
			String fieldId = this.value == null ? "" : this.value.id == null ? "" : this.value.id;
			HasParameters parameters = new HasParameters();
			parameters.setParameter("name", "Valor por defeito");
			defaultValue = MutableSelectionFormFieldFactory.getFormField(ModuleConstants.ListIDs.FieldValues+"/"+fieldId, parameters);
			((FormField<String>)defaultValue).setValue(newDefaultValue);
		}else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.NumericType)){
			defaultValue = new TextBoxFormField("Valor por defeito");
			defaultValue.setFieldWidth("150px");
			((TextBoxFormField)defaultValue).setValue(newDefaultValue);
		}else if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
			HasParameters parameters = new HasParameters();
			parameters.setParameter("name", "Valor por defeito");
			defaultValue = MutableSelectionFormFieldFactory.getFormField(refersToEntityId.getValue(), parameters);
			((FormField<String>)defaultValue).setValue(newDefaultValue);
		}else {
			//TEXT TYPE
			defaultValue = new TextBoxFormField("Valor por defeito");
			((TextBoxFormField)defaultValue).setValue(newDefaultValue);
			defaultValue.setFieldWidth("150px");
		}
		addFormFieldGroup(new FormField<?>[]{defaultValue}, false);
		defaultValue.setReadOnly(isReadOnly());

		System.out.println(type.getValue() + " " + ModuleConstants.PolicyFieldTypes.ReferenceType);
		
		if(fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
			refersToEntityId.setEditable(true);
			refersToEntityId.setReadOnly(isReadOnly());
		}else{
			refersToEntityId.setEditable(false);
			refersToEntityId.setValue(null);
		}
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
