package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.Tax;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.TaxFormValidator;

public class TaxForm extends FormView<Tax> {
	
	private ExpandableListBoxFormField unit;
	private TextBoxFormField name, value;
	
	private Button saveButton;
	private Button deleteButton;
	
	private Tax tax;
	
	public TaxForm() {
		name = new TextBoxFormField("Designação", new TaxFormValidator.NameValidator());
		value = new TextBoxFormField("Valor", new TaxFormValidator.ValueValidator());
		unit = new ExpandableListBoxFormField(ModuleConstants.ListIDs.FieldTypes, "Tipo", new TaxFormValidator.UnitValidator());
		unit.setEditable(false);

		addSection("Informação Geral");
		
		addFormField(name);
		addFormField(value);
		addFormField(unit);
		
		saveButton = new Button("Guardar");
		deleteButton = new Button("Apagar");
		
		addButton(saveButton);
		addButton(deleteButton);
	}

	@Override
	public Tax getInfo() {
		//TODO: O tipo do campo defaultValue mudou para String, para também poder aceitar GUIDs e texto. JMMM.
		tax.name = name.getValue();
		tax.defaultValue = /*0*/null;
		try{
			tax.defaultValue = /*Double.parseDouble(*/value.getValue()/*)*/;
		}catch(NumberFormatException e){
			tax.defaultValue = /*0*/null;
		}
		tax.fieldTypeId = unit.getValue();
		return tax;
	}

	@Override
	public void setInfo(Tax info) {
		if(info == null)
			clearInfo();
		else
			tax = (Tax) info;
		this.name.setValue(tax.name);
		this.value.setValue(tax.defaultValue + "");
		this.unit.setValue(tax.fieldTypeId);
	}

	@Override
	public void clearInfo() {
		this.tax = new Tax();
		super.clearInfo();
	}
	
	public HasClickHandlers getSaveButton(){
		return saveButton;
	}
	
	public HasClickHandlers getDeleteButton(){
		return deleteButton;
	}

}
