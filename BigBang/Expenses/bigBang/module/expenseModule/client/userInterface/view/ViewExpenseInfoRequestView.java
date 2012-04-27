package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.ExpenseForm;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;


public class ViewExpenseInfoRequestView extends ViewInfoOrDocumentRequestView<Expense>{

	public ViewExpenseInfoRequestView(){
		super(new ExpenseForm());
	}
	
}
