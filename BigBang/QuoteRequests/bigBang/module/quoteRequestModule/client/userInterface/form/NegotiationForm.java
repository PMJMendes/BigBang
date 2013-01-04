package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationForm extends FormView<Negotiation>{
	
	ExpandableListBoxFormField company;
	ExpandableListBoxFormField manager;
	DatePickerFormField endDate;
	TextAreaFormField notes;
	boolean isInsurancePolicy;
	
	
	public NegotiationForm(){
		
		addSection("Detalhes da Negociação");
		
		company = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		company.allowEdition(false);
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Negociação");
		manager.allowEdition(false);
		endDate = new DatePickerFormField("Data Limite");
		notes = new TextAreaFormField("Notas");
		
		notes.setFieldHeight("75px");
		notes.setFieldWidth("475px");
		
		manager.setEditable(false);
		
		addFormField(company);
		addFormField(manager);
		addFormField(endDate);
		addFormField(notes); 
		
		}
	
	public void setInsurancePolicyLocked(boolean b){
		company.setEditable(!b);
	}
	
	@Override
	public Negotiation getInfo() {
		
		if(value == null){
			value = new Negotiation();
		}
		Negotiation newNeg = value;
		newNeg.companyId = company.getValue();
		newNeg.managerId = manager.getValue();
		newNeg.limitDate = endDate.getStringValue();
		newNeg.notes = notes.getValue();
		return newNeg;
		
	}
	@Override
	public void setInfo(Negotiation info) {
		
		super.value = info;
		company.setValue(info.companyId);
		manager.setValue(info.managerId);
		endDate.setValue(info.limitDate);
		notes.setValue(info.notes);
		
	}
 

}
