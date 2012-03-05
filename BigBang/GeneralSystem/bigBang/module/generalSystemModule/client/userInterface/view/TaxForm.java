package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.Date;

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
	private TextBoxFormField name, unitsLabel, columnOrder;
	private FormField<?> defaultValue;
	private CheckBoxFormField variesByObject, variesByExercise, mandatory;
	private ExpandableListBoxFormField refersToEntityId;

	private Tax tax;
	private boolean readOnly;
	private String coverageId;

	public TaxForm() {
		this.setSize("410px", "290px");
		name = new TextBoxFormField("Designação", new TaxFormValidator.NameValidator());
		name.setFieldWidth("360px");
		unitsLabel = new TextBoxFormField("Unidade");
		unitsLabel.setFieldWidth("150px");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldTypes, "Tipo", new TaxFormValidator.UnitValidator());
		refersToEntityId = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ObjectIds, "Refere-se a");
		variesByObject = new CheckBoxFormField("Varia por unidade de risco");
		variesByExercise = new CheckBoxFormField("Varia por exercício");
		mandatory = new CheckBoxFormField("Obrigatório");
		columnOrder = new TextBoxFormField("Índice da coluna");
		columnOrder.setFieldWidth("100px");
		defaultValue = new RadioButtonFormField("Valor por defeito");

		addSection("Detalhes do campo");
		addFormFieldGroup(new FormField<?>[]{
				name,
				unitsLabel,
		}, false);
		
		addFormFieldGroup(new FormField<?>[]{variesByExercise}, true);
		addFormFieldGroup(new FormField<?>[]{variesByObject}, true);
		addFormField(mandatory, false);
		
		addFormFieldGroup(new FormField<?>[]{
				type,
				refersToEntityId
		}, true);
		
		addFormField(columnOrder, false);
		addFormField(defaultValue, false);
		
		type.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				refersToEntityId.setReadOnly(true);
				refersToEntityId.setValue(null);
				if(event.getValue() == null){
					defaultValue = new RadioButtonFormField("Valor por defeito");
					return;
				}

					defaultValue.removeFromParent();
				
				if(event.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.DateType)){
					defaultValue = new DatePickerFormField("Valor por defeito");	
				}
				else if(event.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.BooleanType)){
					defaultValue = new RadioButtonFormField("Valor por defeito");
					((RadioButtonFormField)defaultValue).addOption("1", "Sim");
					((RadioButtonFormField)defaultValue).addOption("0", "Não");
					((RadioButtonFormField)defaultValue).addOption("", "Não definido");
					
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ListType)){
					defaultValue = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldValues+"/"+BigBangConstants.EntityIds.TAX, "Valor por defeito");
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.NumericType)){
					defaultValue = new TextBoxFormField("Valor por defeito");
					defaultValue.setFieldWidth("150px");
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
					defaultValue = new ExpandableListBoxFormField(refersToEntityId.getValue(), "Valor por defeito");
					refersToEntityId.setReadOnly(false);
				}else {
					//TEXT TYPE
					defaultValue = new TextBoxFormField("Valor por defeito");
					defaultValue.setFieldWidth("150px");
				}
				
				TaxForm.this.addFormField(defaultValue, false);
				defaultValue.setReadOnly(readOnly);
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
		variesByExercise.setValue(info.variesByExercise);
		variesByObject.setValue(info.variesByObject);
		mandatory.setValue(info.mandatory);
		refersToEntityId.setValue(info.refersToEntityId);
		unitsLabel.setValue(info.unitsLabel);
		columnOrder.setValue(""+info.columnOrder);
		defaultValue.removeFromParent();
		
		refersToEntityId.setReadOnly(true);
		
		if(info.fieldTypeId == null){
			defaultValue = new RadioButtonFormField("Valor por defeito");
		}
		
		
		else if(info.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.DateType)){
			defaultValue = new DatePickerFormField("Valor por defeito");	
			((DatePickerFormField)defaultValue).setValue(info.defaultValue);
		}
		else if(info.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.BooleanType)){
			defaultValue = new RadioButtonFormField("Valor por defeito");
			((RadioButtonFormField)defaultValue).addOption("1", "Sim");
			((RadioButtonFormField)defaultValue).addOption("0", "Não");
			((RadioButtonFormField)defaultValue).addOption("", "Não definido");
			((RadioButtonFormField)defaultValue).setValue(info.defaultValue);
		}else if(info.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ListType)){
			defaultValue = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldValues+"/"+BigBangConstants.EntityIds.TAX, "Valor por defeito");
			((ExpandableListBoxFormField)defaultValue).setValue(info.defaultValue);
		}else if(info.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.NumericType)){
			defaultValue = new TextBoxFormField("Valor por defeito");
			defaultValue.setFieldWidth("150px");
			((TextBoxFormField)defaultValue).setValue(info.defaultValue);
		}else if(info.fieldTypeId.equalsIgnoreCase(ModuleConstants.PolicyFieldTypes.ReferenceType)){
			defaultValue = new ExpandableListBoxFormField(refersToEntityId.getValue(), "Valor por defeito");
			((ExpandableListBoxFormField)defaultValue).setValue(info.defaultValue);
		}else {
			//TEXT TYPE
			defaultValue = new TextBoxFormField("Valor por defeito");
			((TextBoxFormField)defaultValue).setValue(info.defaultValue);
			defaultValue.setFieldWidth("150px");
		}
		addFormField(defaultValue, false);
		defaultValue.setReadOnly(true);

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
