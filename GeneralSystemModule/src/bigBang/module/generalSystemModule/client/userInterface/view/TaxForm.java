package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Tax;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.TaxFormValidator;

public class TaxForm extends FormView<Tax> {
	
	private ExpandableListBoxFormField type;
	private TextBoxFormField name, unitsLabel, defaultValue, columnOrder;
	private CheckBoxFormField variesByObject, variesByExercise, mandatory;
	private ExpandableListBoxFormField refersToEntityId;
	
	private Tax tax;
	
	public TaxForm() {
		name = new TextBoxFormField("Designação", new TaxFormValidator.NameValidator());
		name.setFieldWidth("300px");
		unitsLabel = new TextBoxFormField("Unidade");
		unitsLabel.setFieldWidth("150px");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldTypes, "Tipo", new TaxFormValidator.UnitValidator());
		refersToEntityId = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldValues, "Refere-se a"); //TODO PROLLY WRONG LIST
		variesByObject = new CheckBoxFormField("Varia por unidade de risco");
		variesByExercise = new CheckBoxFormField("Varia por exercício");
		mandatory = new CheckBoxFormField("Obrigatório");
		defaultValue = new TextBoxFormField("Valor por defeito");
		defaultValue.setFieldWidth("300px");
		columnOrder = new TextBoxFormField("Índice da coluna");
		columnOrder.setFieldWidth("100px");
		
		addSection("Detalhes do imposto/coeficiente");
		addFormFieldGroup(new FormField<?>[]{
				name,
				unitsLabel,
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				variesByExercise,
				variesByObject,
				mandatory
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				variesByExercise,
				variesByObject,
				mandatory
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				type,
				refersToEntityId
		}, true);
	
		addFormFieldGroup(new FormField<?>[]{
				defaultValue,
				columnOrder
		}, true);
		
	}

	@Override
	public Tax getInfo() {
		//TODO: O tipo do campo defaultValue mudou para String, para também poder aceitar GUIDs e texto. JMMM.
//		tax.name = name.getValue();
//		tax.defaultValue = /*0*/null;
//		try{
//			tax.defaultValue = /*Double.parseDouble(*/value.getValue()/*)*/;
//		}catch(NumberFormatException e){
//			tax.defaultValue = /*0*/null;
//		}
//		tax.fieldTypeId = type.getValue();
//		return tax;
		return null;
	}

	@Override
	public void setInfo(Tax info) {
//		if(info == null)
//			clearInfo();
//		else
//			tax = (Tax) info;
//		this.name.setValue(tax.name);
//		this.value.setValue(tax.defaultValue + "");
//		this.type.setValue(tax.fieldTypeId);
	}

	@Override
	public void clearInfo() {
		this.tax = new Tax();
		super.clearInfo();
	}


}
