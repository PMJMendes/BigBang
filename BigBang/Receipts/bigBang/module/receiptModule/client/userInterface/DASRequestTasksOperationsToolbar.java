package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class DASRequestTasksOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem receiveResponse, repeat, cancel;
	
	public DASRequestTasksOperationsToolbar(){
		hideAll();
		
		receiveResponse = new MenuItem("Receber Resposta", new Command() {
			
			@Override
			public void execute() {
				onReceiveResponse();
			}
		});
		addItem(receiveResponse);
		
		repeat = new MenuItem("Repetir Pedido", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
		});
		addItem(repeat);
		
		cancel = new MenuItem("Cancelar Pedido", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancel);
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
	
	public abstract void onReceiveResponse();
	
	public abstract void onRepeat();
	
	public void allowCancel(boolean allow){
		cancel.setVisible(allow);
	}
	
	public void allowReceiveResponse(boolean allow) {
		receiveResponse.setVisible(allow);
	}
	
	public void allowRepeat(boolean allow){
		repeat.setVisible(allow);
	}

}
