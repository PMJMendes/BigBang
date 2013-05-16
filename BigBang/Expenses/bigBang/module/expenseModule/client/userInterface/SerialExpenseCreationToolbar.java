package bigBang.module.expenseModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SerialExpenseCreationToolbar extends BigBangOperationsToolBar{

	protected MenuItem confirm, cancel;

	public SerialExpenseCreationToolbar(){
		hideAll();

		confirm = new MenuItem("Guardar", new Command() {

			@Override
			public void execute() {
				saveExpense();
			}
		});

		addItem(confirm);
		addSeparator();
	
		cancel = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
	
		addItem(cancel);
	}

	public abstract void saveExpense();
	
	public void setEnabled(boolean b) {
		confirm.setEnabled(b);
		cancel.setEnabled(b);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

}
