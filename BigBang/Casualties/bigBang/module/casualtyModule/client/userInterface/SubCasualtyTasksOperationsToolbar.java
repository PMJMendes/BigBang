package bigBang.module.casualtyModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyTasksOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem close, rejectClose;
	private MenuItem goToProcess;
	
	public SubCasualtyTasksOperationsToolbar(){
		hideAll();
		
		close = new MenuItem("Encerrar", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(close);
		
		rejectClose = new MenuItem("Rejeitar Encerramento", new Command() {
			
			@Override
			public void execute() {
				onRejectClose();
			}
		});
		addItem(rejectClose);
		
		goToProcess = new MenuItem("Navegar até Sub-Sinistro", new Command() {
			
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
	
	public abstract void onRejectClose();
	
	public void allowClose(boolean allow){
		close.setVisible(allow);
	}
	
	public void allowRejectClose(boolean allow) {
		rejectClose.setVisible(allow);
	}

	public void setGoToProcessVisible() {
		goToProcess.setVisible(true);		
	}

}
