package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class DefaultActionsToolbar extends BigBangOperationsToolBar{
	
	protected MenuItem sendMessage;
	protected MenuItem receiveMessage;
	protected MenuItem close;
	
	public DefaultActionsToolbar() {
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.REQUESTS, true);
		showItem(SUB_MENU.ADMIN, true);
		
		sendMessage = new MenuItem("Enviar Mensagem", new Command(){
			
			@Override
			public void execute(){
				onSendMessage();
			}
			
		});
		
		addItem(SUB_MENU.REQUESTS, sendMessage);
		
		receiveMessage = new MenuItem("Receber Mensagem", new Command(){
			
			@Override
			public void execute(){
				onReceiveMessage();
			}
			
		});
		
		addItem(SUB_MENU.REQUESTS, receiveMessage);
		
		close = new MenuItem("Fechar Processo", new Command(){
			
			@Override
			public void execute() {
				onClose();
			}
			
		});
		
		addItem(SUB_MENU.ADMIN, close);
		
	}

	protected abstract void onSendMessage();
	
	protected abstract void onReceiveMessage();
	
	protected abstract void onClose();

	@Override
	public abstract void onEditRequest();

	@Override
	public abstract void onSaveRequest();

	@Override
	public abstract void onCancelRequest();

	public void allowEdit(boolean b) {
		editCancelMenuItem.setEnabled(b);
	}

	public void allowSendMessage(boolean hasPermission) {
		sendMessage.setEnabled(hasPermission);
	}

	public void allowReceiveMessage(boolean hasPermission) {
		receiveMessage.setEnabled(hasPermission);
	}

	public void allowClose(boolean hasPermission) {
		close.setEnabled(hasPermission);
	}

}
