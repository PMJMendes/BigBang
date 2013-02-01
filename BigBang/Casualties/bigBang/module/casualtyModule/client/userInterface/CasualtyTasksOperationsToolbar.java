package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CasualtyTasksOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem close;
	private MenuItem goToProcess;

	protected CasualtyTasksOperationsToolbar(){
		hideAll();
		
		close = new MenuItem("Encerrar Processo", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(close);
		
		goToProcess = new MenuItem("Navegar até Sinistro", new Command() {
			
			@Override
			public void execute() {
				onGoToProcess();
			}
		});
		addItem(goToProcess);
	}
	
	protected abstract void onGoToProcess();
	
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	@Override
	public void onCancelRequest() {
		return;
	}

	public abstract void onClose();
	
	public void allowClose(boolean allow){
		close.setVisible(allow);
	}

	public void setGoToProcessVisible() {
		goToProcess.setVisible(true);
	}
	
}
