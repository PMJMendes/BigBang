package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class DeleteExpenseForm extends FormView<String>{

	protected TextAreaFormField reason;
	
	public DeleteExpenseForm(){
		
		addSection("Eliminar Despesa de Saúde");
		reason = new TextAreaFormField("Motivo");
		addFormField(reason);
		
		setValidator(new DeleteExpenseFormValidator(this));
	}

	@Override
	public String getInfo() {
		return reason.getValue();
	}

	@Override
	public void setInfo(String info) {
		reason.setValue(info);
	}
	
	

}
