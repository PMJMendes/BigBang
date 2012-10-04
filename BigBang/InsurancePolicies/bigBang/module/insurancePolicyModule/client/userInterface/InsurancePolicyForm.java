package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;

public class InsurancePolicyForm extends InsurancePolicyHeaderForm{
	
	PolicyNotesFormSection notesSection = new PolicyNotesFormSection();
	
	public InsurancePolicyForm() {
		super();
		this.addSection(notesSection);
		this.setHeaderFormVisible(false);
	}
	
	@Override
	public void setValue(InsurancePolicy value) {
		super.setValue(value);
		if(value == null){
			notesSection.clear();
			return;
		}
		notesSection.setValue(value.notes);
	}
	
	public InsurancePolicy getValue() {
		InsurancePolicy value = super.getValue();
		
		value.notes = notesSection.getValue();
		
		return value;
	};

}
