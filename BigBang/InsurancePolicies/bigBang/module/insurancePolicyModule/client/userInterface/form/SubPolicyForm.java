package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.TextAreaFormField;

public class SubPolicyForm extends SubPolicyHeaderForm{
	
	protected TextAreaFormField notesSection;

	public SubPolicyForm() {
		super();
		notesSection = new TextAreaFormField();
		addSection("Notas");
		addFormField(notesSection);
		this.setHeaderFormVisible(false);
		
		setValidator(new SubPolicyFormValidator(this));
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
		
		if(value != null){
			value.notes = notesSection.getValue();
		}
		
		return value;
	};

}

