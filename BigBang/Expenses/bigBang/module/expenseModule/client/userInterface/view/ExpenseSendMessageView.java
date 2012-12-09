package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

public class ExpenseSendMessageView extends SendMessageView<Expense>{
	
	public ExpenseSendMessageView(){
		super(new ExpenseForm());
	}
}
