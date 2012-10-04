package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.DebitNote;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CreateDebitNoteForm extends FormView<DebitNote> {

	protected NumericTextBoxFormField noteValue;
	protected DatePickerFormField maturityDate;
	
	public CreateDebitNoteForm(){
		noteValue = new NumericTextBoxFormField("Valor", true);
		noteValue.setUnitsLabel("€");
		noteValue.setFieldWidth("175px");
		maturityDate = new DatePickerFormField("Data de Vencimento");
		
		addSection("Informação Geral");
		addFormField(noteValue);
		addFormField(maturityDate);
		
		setValidator(new CreateDebitNoteFormValidator(this));
	}

	@Override
	public DebitNote getInfo() {
		DebitNote result = getValue();
		if(result == null) {
			result = new DebitNote();
		}
		result.value = noteValue.getValue();
		result.maturityDate = maturityDate.getStringValue();
		return result;
	}

	@Override
	public void setInfo(DebitNote info) {
		if(info == null){
			setInfo(new DebitNote());
		}else{
			noteValue.setValue(info.value);
			maturityDate.setValue(info.maturityDate);
		}
	}

}
