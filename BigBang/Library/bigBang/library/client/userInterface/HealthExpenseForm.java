package bigBang.library.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HealthExpense;
import bigBang.library.client.userInterface.view.FormView;

public class HealthExpenseForm extends FormView<HealthExpense>{

	private ExpandableListBoxFormField manager;
	private TextBoxFormField settlement;
	private TextBoxFormField notes;
	private TextBoxFormField clientName;
	private DatePickerFormField expenseDate;
	private ExpandableListBoxFormField insuredObjectId;
	private ExpandableListBoxFormField coverageId;
	private TextBoxFormField value;
	private TextBoxFormField isOpen;
	
	
	public HealthExpenseForm() {
		
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		settlement = new TextBoxFormField("Valor acordado");
		settlement.setUnitsLabel("€");
		notes = new TextBoxFormField("Nota");
		clientName = new TextBoxFormField("Cliente"); //TODO TEM DE APARECER COMO EM APOLICE
		expenseDate = new DatePickerFormField("Data");
		insuredObjectId = new ExpandableListBoxFormField("Unidade de Risco");
		value = new TextBoxFormField("Valor");
		value.setUnitsLabel("€");
		isOpen = new TextBoxFormField("Estado");
		
	}
	
	@Override
	public HealthExpense getInfo() {
		
		HealthExpense newExpense = super.value;
		
		newExpense.managerId = manager.getValue();
		newExpense.settlement = settlement.getValue();
		newExpense.notes = notes.getValue();
		newExpense.clientName = clientName.getValue();
		newExpense.expenseDate = expenseDate.getStringValue();
		newExpense.insuredObjectId = insuredObjectId.getValue();
		newExpense.value = value.getValue();
		
		return newExpense;
	
	}

	@Override
	public void setInfo(HealthExpense info) {
		
		manager.setValue(info.managerId);
		settlement.setValue(info.settlement);
		
		//TODO;
		
	}

}
