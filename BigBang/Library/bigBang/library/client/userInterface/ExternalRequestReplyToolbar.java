package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ExternalRequestReplyToolbar extends BigBangOperationsToolBar {

	protected MenuItem send, cancel;
	
	public ExternalRequestReplyToolbar() {
		hideAll();
		
		send = new MenuItem("Enviar", new Command() {
			
			@Override
			public void execute() {
				onSend();
			}
		});
		addItem(send);
		
		addSeparator();
		
		cancel = new MenuItem("Cancelar", new Command() {
			
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

	public abstract void onSend();

}
