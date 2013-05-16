package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SignatureRequestTasksOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem receive, repeat, cancel;
	
	public SignatureRequestTasksOperationsToolbar(){
		hideAll();
		
		receive = new MenuItem("Receber Resposta", new Command() {
			
			@Override
			public void execute() {
				onReceive();
			}
		});
		addItem(receive);
		
		repeat = new MenuItem("Repetir", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
		});
		addItem(repeat);
		
		cancel = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
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
	public abstract void onCancelRequest();
	
	public abstract void onReceive();
	
	public abstract void onRepeat();
	
	public void allowReceive(boolean allow) {
		this.receive.setVisible(allow);
	}
	
	public void allowRepeat(boolean allow) {
		this.repeat.setVisible(allow);
	}
	
	public void allowCancel(boolean allow){
		this.cancel.setVisible(allow);
	}

}
