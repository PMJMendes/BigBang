package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.SubPolicy;

public class SubPolicyFormWithNotes extends SubPolicyForm{
	
	PolicyNotesFormSection notesSection = new PolicyNotesFormSection();

	public SubPolicyFormWithNotes() {
		super();
		this.addSection(notesSection);
		this.setHeaderFormVisible(false);
	}
	
	@Override
	public void setValue(SubPolicy value) {
		super.setValue(value);
		notesSection.setValue(value.notes);
	}
	
	public SubPolicy getValue() {
		SubPolicy value = super.getValue();
		
		value.notes = notesSection.getValue();
		
		return value;
	};

}

