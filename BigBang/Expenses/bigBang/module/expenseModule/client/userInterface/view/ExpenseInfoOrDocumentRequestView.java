package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

public class ExpenseInfoOrDocumentRequestView extends InfoOrDocumentRequestView<Expense>{
	
	public ExpenseInfoOrDocumentRequestView(){
		super(new ExpenseForm());
	}

}
