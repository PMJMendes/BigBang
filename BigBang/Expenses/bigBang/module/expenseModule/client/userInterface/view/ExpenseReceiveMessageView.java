package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

public class ExpenseReceiveMessageView extends ReceiveMessageView<Expense>{

	public ExpenseReceiveMessageView(){
		super(new ExpenseForm());
	}
}
