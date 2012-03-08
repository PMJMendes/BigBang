package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NegotiationOperationsToolBar extends BigBangOperationsToolBar{

	protected MenuItem deleteItem;
	
	public NegotiationOperationsToolBar(){
		
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);
	}
	
	public abstract void onDeleteRequest();
	
	public void allowDelete(boolean allow){
		deleteItem.setEnabled(allow);
	}
	
	public void allowEdit(boolean allow){
		editCancelMenuItem.setEnabled(allow);
	}

}
