package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NegotiationForm extends FormView<Negotiation>{
	
	ExpandableListBoxFormField company;
	ExpandableListBoxFormField manager;
	DatePickerFormField endDate;
	TextBoxFormField notes;
	
	public NegotiationForm(){
		
		addSection("Detalhes da Negociação");
		
		company = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		endDate = new DatePickerFormField("Data Limite");
		notes = new TextBoxFormField("Notas");
		
		VerticalPanel wrapper = new VerticalPanel();
		
		wrapper.add(company);
		wrapper.add(manager);
		wrapper.add(endDate);
		wrapper.add(notes); 
		
		
		SplitLayoutPanel split = new SplitLayoutPanel();
		
		split.add(wrapper);
		
		addWidget(split);
		}
	
	
	@Override
	public Negotiation getInfo() {
		
		Negotiation newNeg = getValue(); 
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
