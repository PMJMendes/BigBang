package bigBang.module.tasksModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class MailOrganizerToolbar extends BigBangOperationsToolBar{

	protected MenuItem confirm, cancel;
	
	public MailOrganizerToolbar() {
		hideAll();
		
		confirm = new MenuItem("Guardar", new Command(){

			@Override
			public void execute() {
				saveDocument();
			}
			
		});
		
		addItem(confirm);
		addSeparator();
		
		cancel = new MenuItem("Cancelar", new Command(){

			@Override
			public void execute() {
				onCancelRequest();
			}
			
		});
		addItem(cancel);
		
	}
	
	protected abstract void saveDocument();

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
	
	
	public void setEnabled(boolean b) {
		confirm.setEnabled(b);
		cancel.setEnabled(b);		
	}

}
