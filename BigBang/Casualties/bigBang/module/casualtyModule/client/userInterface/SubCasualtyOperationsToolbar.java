package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem delete;
	
	public SubCasualtyOperationsToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.ADMIN, true);
		
		delete = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, delete);
	}
	
	public abstract void onDelete();

	public void allowEdit(boolean allow) {
		this.setEditionAvailable(allow);
	}
	
	public void allowDelete(boolean allow){
		this.delete.setEnabled(allow);
	}
	
}
