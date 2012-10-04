package bigBang.module.casualtyModule.client.userInterface.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

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
import bigBang.module.casualtyModule.client.resources.Resources;

public class CasualtyForm extends FormView<Casualty> {

	protected TextBoxFormField number;
	protected NavigationFormField client;
	protected DatePickerFormField date;
	protected TextAreaFormField description;
	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField status;
	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;
	protected Image statusIcon;

	public CasualtyForm(){
		number = new TextBoxFormField("Número de Processo");
		number.setEditable(false);
		client = new NavigationFormField("Cliente");
		client.setEditable(false);
		date = new DatePickerFormField("Data do Sinistro");
		date.setMandatory(true);
		description = new TextAreaFormField();
		description.setFieldWidth("600px");
		description.setFieldHeight("250px");

		notes = new TextAreaFormField();
		notes.setFieldWidth("600px");
		notes.setFieldHeight("250px");

		caseStudy = new CheckBoxFormField("Case Study");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Sinistro");
		manager.allowEdition(false);
		status = new TextBoxFormField("Estado");
		status.setEditable(false);
		status.setFieldWidth("100%");
		statusIcon = new Image();
		status.add(statusIcon);

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

		setForEdit();
	}

	public void setForCreate(){
		manager.setEditable(true);
		manager.setReadOnlyInternal(isReadOnly());
	}

	public void setForEdit(){
		manager.setEditable(false);
		manager.setReadOnlyInternal(isReadOnly());
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
			if(info.id != null){
				status.setValue(info.isOpen ? "Aberto" : "Fechado");
				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(info.isOpen ? resources.activeCasualtyIcon() : resources.inactiveCasualtyIcon());
			}else{
				status.setValue(null);
				statusIcon.setVisible(false);
			}
		}
	}

}

