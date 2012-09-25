package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.SubPolicy;

public class SubPolicyForm extends SubPolicyHeaderForm{
	
	PolicyNotesFormSection notesSection = new PolicyNotesFormSection();

	public SubPolicyForm() {
		super();
		this.addSection(notesSection);
		this.setHeaderFormVisible(false);
	}
	
	@Override
	public void setValue(SubPolicy value) {
		super.setValue(value);
		if(value == null){
			notesSection.clear();
			return;
		}
		notesSection.setValue(value.notes);

	}
	
	public SubPolicy getValue() {
		SubPolicy value = super.getValue();
		
		value.notes = notesSection.getValue();
		
		return value;
	};

}

