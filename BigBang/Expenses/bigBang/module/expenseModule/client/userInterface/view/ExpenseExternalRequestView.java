package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.library.client.userInterface.view.ExternalRequestView;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

public class ExpenseExternalRequestView extends ExternalRequestView<Expense>{

	public ExpenseExternalRequestView(){
		super(new ExpenseForm());

		setParentHeaderTitle("Ficha da Despesa de Saúde");
	}

	@Override
	public void setParentHeaderTitle(String title) {
		ownerHeader.setText(title);
	}

	@Override
	protected void initializeView() {
		return;
	}

}
