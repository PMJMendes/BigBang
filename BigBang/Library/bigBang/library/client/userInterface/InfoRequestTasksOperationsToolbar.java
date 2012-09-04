package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class InfoRequestTasksOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem receiveResponse, repeat, cancel, goToProcess;
	
	public InfoRequestTasksOperationsToolbar(){
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
		
		goToProcess = new MenuItem("Navegar para Pedido de Informação", new Command() {
			
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

	public abstract void onReceiveResponse();
	
	public abstract void onRepeat();
	
	public void allowReceiveResponse(boolean allow) {
		receiveResponse.setVisible(allow);
	}
	
	public void allowRepeat(boolean allow) {
		repeat.setVisible(allow);
	}
	
	public void allowCancel(boolean allow) {
		cancel.setVisible(allow);
	}

	public void setGoToProcessVisible() {
		goToProcess.setVisible(true);
	}
}
