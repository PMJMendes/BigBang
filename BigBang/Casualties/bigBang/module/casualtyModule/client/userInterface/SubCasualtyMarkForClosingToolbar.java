package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyMarkForClosingToolbar extends BigBangOperationsToolBar {

	protected MenuItem markForClosing;
	protected MenuItem cancel;
	
	public SubCasualtyMarkForClosingToolbar(){
		hideAll();
		
		markForClosing = new MenuItem("Efectuar Marcação", new Command() {
			
			@Override
			public void execute() {
				onMarkForClosing();
			}
		});
		addItem(markForClosing);
		
		addSeparator();
		
		cancel = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancel);
	}
	
	@Override
	public void onEditRequest(){
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	@Override
	public abstract void onCancelRequest();
	
	public abstract void onMarkForClosing();
	
	public void allowMarkForClosing(boolean allow){
		markForClosing.setEnabled(allow);
	}

}
