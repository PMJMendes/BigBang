package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.Tax;
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
		unit = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ValueUnits, "Unidade", new TaxFormValidator.UnitValidator());
		
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
		tax.name = name.getValue();
		tax.value = 0;
		try{
			tax.value = Double.parseDouble(value.getValue());
		}catch(NumberFormatException e){
			tax.value = 0;
		}
		tax.currencyId = unit.getValue();
		return tax;
	}

	@Override
	public void setInfo(Tax info) {
		if(info == null)
			clearInfo();
		else
			tax = (Tax) info;
		this.name.setValue(tax.name);
		this.value.setValue(tax.value + "");
		this.unit.setValue(tax.currencyId);
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
