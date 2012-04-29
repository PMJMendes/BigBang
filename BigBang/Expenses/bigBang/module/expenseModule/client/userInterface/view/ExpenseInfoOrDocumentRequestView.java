package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.ExpenseForm;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;

public class ExpenseInfoOrDocumentRequestView extends InfoOrDocumentRequestView<Expense>{
	
	public ExpenseInfoOrDocumentRequestView(){
		super(new ExpenseForm());
	}

}
