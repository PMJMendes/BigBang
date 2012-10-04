package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;


public class ViewExpenseInfoRequestView extends ViewInfoOrDocumentRequestView<Expense>{

	public ViewExpenseInfoRequestView(){
		super(new ExpenseForm());
	}
	
}
