package bigBang.module.expenseModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ExpenseDeleteToolbar extends BigBangOperationsToolBar{

	public ExpenseDeleteToolbar(){
		MenuItem deleteExpenseRequest = new MenuItem("Eliminar Despesa de Saúde", new Command() {
			
			@Override
			public void execute() {
				onDeleteExpenseRequest();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(deleteExpenseRequest);
		addSeparator();
		addItem(cancelItem);
	}
	
	@Override
	public void onEditRequest() {
		return;
		
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	@Override
	public abstract void onCancelRequest();
	
	public abstract void onDeleteExpenseRequest();

}
