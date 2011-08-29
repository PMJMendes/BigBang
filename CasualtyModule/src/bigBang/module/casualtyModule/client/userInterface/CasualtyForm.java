package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.Casualty;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CasualtyForm extends FormView<Casualty> {

	protected TextBoxFormField number;
	protected TextBoxFormField clientName;
	protected TextBoxFormField clientNumber;
	protected DatePickerFormField date;
	protected TextAreaFormField description;
	protected ExpandableListBoxFormField cause;
	protected CheckBoxFormField caseStudy;
	protected ExpandableListBoxFormField manager;
	
	public CasualtyForm(){
		number = new TextBoxFormField("Número");
		clientName = new TextBoxFormField("Nome");
		clientNumber = new TextBoxFormField("Número");
		date = new DatePickerFormField("Data");
		description = new TextAreaFormField("Descrição");
		cause = new ExpandableListBoxFormField("Causa");
		caseStudy = new CheckBoxFormField("Estudo de Caso");
		manager = new ExpandableListBoxFormField("Gestor");
		
		addSection("Informação Geral");
		addFormField(number);
		addFormField(date);
		addFormField(cause);
		addFormField(manager);
		addFormField(caseStudy);
		addFormField(description);
		
		addSection("Cliente");
		addFormField(clientName);
		addFormField(clientNumber);
	}
	
	@Override
	public Casualty getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(Casualty info) {
		// TODO Auto-generated method stub
		
	}

}
