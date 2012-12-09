package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

public class ExpenseConversationView extends ConversationView<Expense>{

	public ExpenseConversationView() {
		super(new ExpenseForm());
	}

}
