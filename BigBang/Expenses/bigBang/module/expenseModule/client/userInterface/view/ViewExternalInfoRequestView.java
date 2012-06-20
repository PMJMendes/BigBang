package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.ViewExternalRequestView;
import bigBang.module.expenseModule.client.userInterface.ExpenseForm;

public class ViewExternalInfoRequestView extends ViewExternalRequestView<Expense>{

	public ViewExternalInfoRequestView() {
		super(new ExpenseForm());
	}

}
