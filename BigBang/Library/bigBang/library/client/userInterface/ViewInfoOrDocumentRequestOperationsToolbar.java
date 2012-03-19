package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class ViewInfoOrDocumentRequestOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem repeat, cancel, receiveResponse;
	
	public ViewInfoOrDocumentRequestOperationsToolbar(){
		hideAll();

		repeat = new MenuItem("Repetir Pedido", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
		});
		addItem(repeat);
		
		receiveResponse = new MenuItem("Receber Resposta", new Command() {
			
			@Override
			public void execute() {
				onReceiveResponse();
			}
		});
		addItem(receiveResponse);
		
		addSeparator();
		
		cancel = new MenuItem("Cancelar Pedido", new Command() {
			
			@Override
			public void execute() {
				onCancel();
			}
		});
		addItem(cancel);
	}
	
	public void allowRepeat(boolean allow){
		this.repeat.setEnabled(allow);
	}
	
	public void allowReceiveResponse(boolean allow) {
		this.receiveResponse.setEnabled(allow);
	}
	
	public void allowCancel(boolean allow) {
		this.cancel.setEnabled(allow);
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
	
	public abstract void onRepeat();
	
	public abstract void onCancel();
	
	public abstract void onReceiveResponse();

}
