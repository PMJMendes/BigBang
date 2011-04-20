package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HorizontalPanel;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.Tax;

public class TaxForm extends FormView<Tax> {
	
	private ExpandableListBoxFormField unit;
	private TextBoxFormField name, value;
	
	private Tax tax;
	
	public TaxForm() {
		name = new TextBoxFormField("Designação");
		value = new TextBoxFormField("Valor");
		unit = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ValueUnits, "Unidade");
		
		addSection("Informação Geral");
		
		addFormField(name);
		addFormField(value);
		addFormField(unit);
	}

	@Override
	public Tax getInfo() {
		tax.name = name.getValue();
		tax.value = Double.parseDouble(value.getValue());
		tax.currencyId = unit.getValue();
		return tax;
	}

	@Override
	public void setInfo(Tax info) {
		if(info == null)
			tax = new Tax();
	}
	
	@Override
	public void clearInfo() {
		this.tax = new Tax();
		super.clearInfo();
	}

}
