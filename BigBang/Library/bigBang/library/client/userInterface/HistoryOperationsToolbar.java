package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class HistoryOperationsToolbar extends BigBangOperationsToolBar {

	private MenuItem undoItem;
	private MenuItem navigateToAuxiliaryProcessItem;
	
	public HistoryOperationsToolbar(){
		hideAll();

		this.undoItem = new MenuItem("Reverter", new Command() {

			@Override
			public void execute() {
				onUndo();
			}
		});
		addItem(this.undoItem);

		this.navigateToAuxiliaryProcessItem = new MenuItem("Processo Auxiliar", new Command() {

			@Override
			public void execute() {
				onNavigateToAuxiliaryProcess();
			}
		});
		addItem(this.navigateToAuxiliaryProcessItem);
	}
	
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

	public void allowUndo(boolean allow){
		this.undoItem.setEnabled(allow);
	}
	
	public void allowNavigateToAuxiliaryProcess(boolean allow){
		this.navigateToAuxiliaryProcessItem.setEnabled(allow);
	}
	
	public abstract void onUndo();
	
	public abstract void onNavigateToAuxiliaryProcess();
	
}
