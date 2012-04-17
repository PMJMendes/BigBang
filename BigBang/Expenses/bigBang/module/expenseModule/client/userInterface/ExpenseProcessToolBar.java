package bigBang.module.expenseModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ExpenseProcessToolBar extends BigBangOperationsToolBar{

	
	
	//ADMIN
	protected MenuItem deleteExpense;
	
	public ExpenseProcessToolBar() {
		
		deleteExpense = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteExpense);
	}
	
	protected abstract void onDeleteRequest();
		
	@Override
	public abstract void onEditRequest();

	@Override
	public abstract void onSaveRequest();

	@Override
	public abstract void onCancelRequest();

	public void allowDelete(boolean allow) {
		deleteExpense.setEnabled(allow);
	}
	

}
