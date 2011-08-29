package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InsurancePolicyForm extends FormView<InsurancePolicy> {

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected ExpandableListBoxFormField insuranceAgency;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	protected ExpandableListBoxFormField mediator;
	protected DatePickerFormField startDate;
	protected CheckBoxFormField temporary;
	protected DatePickerFormField endDate;
	//TODO dynamic fields
	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;
	
	public InsurancePolicyForm(){
		super();
		addSection("Apólice");
		number  = new TextBoxFormField("Número");
		manager = new ExpandableListBoxFormField("Gestor");
		mediator = new ExpandableListBoxFormField("Mediador");
		line = new ExpandableListBoxFormField("Ramo");
		subLine = new ExpandableListBoxFormField("Modalidade");
		startDate = new DatePickerFormField("Data de Início");
		temporary = new CheckBoxFormField("Temporária");
		endDate = new DatePickerFormField("Data de Fim");
		caseStudy = new CheckBoxFormField("Case Study");
		notes = new TextAreaFormField("Observações");

		addFormField(number);
		addFormField(manager);
		addFormField(mediator);
		addFormField(line);
		addFormField(subLine);
		addFormField(startDate);
		addFormField(temporary);
		addFormField(endDate);
		addFormField(temporary);
		addWidget(new Label("Acrescentar campos dinamicos"));
		addFormField(caseStudy);
		addFormField(notes);
		
		addSection("Cliente");
		
		
		addSection("Seguradora");
		
		
	}
	
	@Override
	public InsurancePolicy getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(InsurancePolicy info) {
		// TODO Auto-generated method stub
		
	}

}
