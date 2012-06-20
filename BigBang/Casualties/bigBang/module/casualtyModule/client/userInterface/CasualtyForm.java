package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CasualtyForm extends FormView<Casualty> {

	protected TextBoxFormField number;
	protected NavigationFormField client;
	protected DatePickerFormField date;
	protected TextAreaFormField description;
	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField status;
	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;
	
	public CasualtyForm(){
		number = new TextBoxFormField("Número de Processo");
		number.setEditable(false);
		client = new NavigationFormField("Cliente");
		client.setEditable(false);
		date = new DatePickerFormField("Data");
		description = new TextAreaFormField();
		description.setFieldWidth("600px");
		description.setFieldHeight("250px");
		
		notes = new TextAreaFormField();
		notes.setFieldWidth("600px");
		notes.setFieldHeight("250px");
		
		caseStudy = new CheckBoxFormField("Case Study");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		manager.setEditable(false);
		manager.allowEdition(false);
		status = new TextBoxFormField("Estado");
		status.setEditable(false);
		status.setFieldWidth("175px");
		
		addSection("Informação Geral");
		addFormField(client);
		addFormField(number);
		addFormField(status, true);
		addFormField(manager, true);
		addFormField(date, true);
		addFormField(caseStudy);
		
		addSection("Descrição");
		addFormField(description);
		
		addSection("Notas Internas");
		addFormField(notes);
		
	}

	@Override
	public Casualty getInfo() {
		Casualty result = value;
		
		if(result != null) {
			result.processNumber = number.getValue();
			result.caseStudy = caseStudy.getValue();
			result.casualtyDate = date.getStringValue();
			result.description = description.getValue();
			result.internalNotes = notes.getValue();
			result.managerId = manager.getValue();
		}
		
		return result;
	}

	@Override
	public void setInfo(Casualty info) {
		if(info == null) {
			setInfo(new Casualty());
		}else{
			number.setValue(info.processNumber);
		
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "client");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("clientid", info.clientId);
			client.setValue(item);
			client.setValueName("#" + info.clientNumber + " - " + info.clientName);
			
			date.setValue(info.casualtyDate);
			notes.setValue(info.internalNotes);
			description.setValue(info.description);
			caseStudy.setValue(info.caseStudy);
			manager.setValue(info.managerId);
			status.setValue(info.isOpen ? "Aberto" : "Fechado");
		}
	}

}

