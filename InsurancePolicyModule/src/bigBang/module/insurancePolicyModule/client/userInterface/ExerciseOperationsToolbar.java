package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ExerciseOperationsToolbar extends BigBangOperationsToolBar{
	
	private MenuItem deleteItem;
	
	public ExerciseOperationsToolbar(){
		hideAll();
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, this.deleteItem);
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.ADMIN, true);
	}

	public void allowEdit(boolean allow){
		this.editCancelMenuItem.setEnabled(allow);
		this.setSaveModeEnabled(false);
	}
	
	public void allowDelete(boolean allow) {
		this.deleteItem.setEnabled(allow);
	}
	
	public abstract void onDelete();

}
