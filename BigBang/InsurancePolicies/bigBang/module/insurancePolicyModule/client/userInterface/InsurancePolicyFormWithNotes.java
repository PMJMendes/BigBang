package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.InsurancePolicy;

public class InsurancePolicyFormWithNotes extends InsurancePolicyForm{
	
	PolicyNotesFormSection notesSection = new PolicyNotesFormSection();
	
	public InsurancePolicyFormWithNotes() {
		super();
		this.addSection(notesSection);
		this.setHeaderFormVisible(false);
	}
	
	@Override
	public void setValue(InsurancePolicy value) {
		super.setValue(value);
		notesSection.setValue(value.notes);
	}
	
	public InsurancePolicy getValue() {
		InsurancePolicy value = super.getValue();
		
		value.notes = notesSection.getValue();
		
		return value;
	};

}
