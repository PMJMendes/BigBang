package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class SendMessageToolbar extends
		BigBangOperationsToolBar {

	private MenuItem cancelItem, sendItem;
	
	public SendMessageToolbar(){
		hideAll();
		
		sendItem = new MenuItem("Enviar Mensagem", new Command() {
			
			@Override
			public void execute() {
				onSendRequest();
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

	public void allowSend(boolean allow){
		this.sendItem.setEnabled(allow);
	}
	
	public void allowCancel(boolean allow) {
		this.cancelItem.setEnabled(allow);
	}
	
	@Override
	public abstract void onCancelRequest();

	public abstract void onSendRequest();
	
}
