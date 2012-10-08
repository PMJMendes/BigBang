package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.InsurancePolicy;

public class InsurancePolicyForm extends InsurancePolicyHeaderForm{
	
	PolicyNotesFormSection notesSection = new PolicyNotesFormSection();
	
	public InsurancePolicyForm() {
		super();
		this.addSection(notesSection);
		this.setHeaderFormVisible(false);
	}
	
	@Override
	public void setInfo(InsurancePolicy value) {
		super.setInfo(value);
		if(value == null){
			notesSection.clear();
			return;
		}
		notesSection.setValue(value.notes);
	}
	
	@Override
	public InsurancePolicy getInfo() {
		InsurancePolicy value = super.getInfo();
		
		value.notes = notesSection.getValue();
		
		return value;
	};

}
