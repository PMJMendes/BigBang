package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.DebitNoteBatch;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CreateSubPolicyReceiptForm extends FormView<DebitNoteBatch>{

	protected DatePickerFormField from, to, limitDate;

	public CreateSubPolicyReceiptForm() {
		addSection("Criar Notas de Débito (Saúde)");

		from = new DatePickerFormField("De");	
		to = new DatePickerFormField("Até");	
		limitDate = new DatePickerFormField("Data Limite");	

		addFormField(from);
		addFormField(to);
		addFormField(limitDate);

		setValidator(new CreateSubPolicyReceiptFormValidator(this));
	}

	@Override
	public DebitNoteBatch getInfo() {
		DebitNoteBatch batch = value;
		batch.maturityDate = from.getStringValue();
		batch.endDate = to.getStringValue();
		batch.limitDate = limitDate.getStringValue();

		return batch;

	}

	@Override
	public void setInfo(DebitNoteBatch info) {
		from.setValue(info.maturityDate);
		to.setValue(info.endDate);
		limitDate.setValue(info.limitDate);
	}

}
