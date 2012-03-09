package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;

public abstract class NegotiationOperationsToolBar extends BigBangOperationsToolBar{

	protected MenuItem deleteItem;
	protected MenuItem cancelItem;
	
	public NegotiationOperationsToolBar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.DATA, true);
		showItem(SUB_MENU.ADMIN, true);
		
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);
		
		cancelItem = new MenuItem("Cancelar Negociação", new Command(){
			
			@Override
			public void execute() {
				onCancelNegotiationRequest();
				
			}
		});
		addItem(SUB_MENU.DATA, cancelItem);
	}
	
	public abstract void onDeleteRequest();
	public abstract void onCancelNegotiationRequest();
	
	public void allowDelete(boolean allow){
		deleteItem.setEnabled(allow);
	}
	
	public void allowEdit(boolean allow){
		editCancelMenuItem.setEnabled(allow);
	}
	
	public void allowCancelNegotiation(boolean allow){
		cancelItem.setEnabled(allow);
	}

}
