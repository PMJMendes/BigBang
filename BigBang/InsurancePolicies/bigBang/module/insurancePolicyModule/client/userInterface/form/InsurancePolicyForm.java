package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.TextAreaFormField;

public class InsurancePolicyForm extends InsurancePolicyHeaderForm{
	
	protected TextAreaFormField notes;
	
	public InsurancePolicyForm() {
		super();
		addSection("Notas");
		notes = new TextAreaFormField("Notas");
		addFormField(notes);
		this.setHeaderFormVisible(false);
		
		setValidator(new InsurancePolicyFormValidator(this));
	}
	
	@Override
	public void setInfo(InsurancePolicy value) {
		super.setInfo(value);
		if(value == null){
			notes.clear();
			return;
		}
		notes.setValue(value.notes);
	}
	
	@Override
	public InsurancePolicy getInfo() {
		InsurancePolicy value = super.getValue();
		
		value.notes = notes.getValue();
		
		return value;
	};

}
