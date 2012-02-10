package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.DebitNote;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CreateDebitNoteForm extends FormView<DebitNote> {

	private TextBoxFormField noteValue;
	private DatePickerFormField maturityDate;
	
	public CreateDebitNoteForm(){
		noteValue = new TextBoxFormField("Valor");
		maturityDate = new DatePickerFormField("Data de Vencimento");
		
		addSection("Informação Geral");
		addFormField(noteValue);
		addFormField(maturityDate);
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
