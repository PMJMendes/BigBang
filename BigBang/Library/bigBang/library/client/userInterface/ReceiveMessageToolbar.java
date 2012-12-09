package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ReceiveMessageToolbar extends BigBangOperationsToolBar{

	private MenuItem cancelItem, sendItem;
	
	public ReceiveMessageToolbar(){
		hideAll();
		
		sendItem = new MenuItem("Receber Mensagem", new Command() {
			
			@Override
			public void execute() {
				onReceiveRequest();
			}
		});
		addItem(sendItem);
		
		addSeparator();
		
		cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancelItem);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public void allowReceive(boolean allow){
		this.sendItem.setEnabled(allow);
	}
	
	public void allowCancel(boolean allow) {
		this.cancelItem.setEnabled(allow);
	}
	
	@Override
	public abstract void onCancelRequest();

	public abstract void onReceiveRequest();
}
