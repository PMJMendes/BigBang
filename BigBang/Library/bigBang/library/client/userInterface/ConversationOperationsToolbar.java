package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ConversationOperationsToolbar extends BigBangOperationsToolBar{
	
	protected MenuItem sendMessage;
	protected MenuItem repeatMessage;
	protected MenuItem receiveMessage;
	protected MenuItem closeConversation;
	
	public ConversationOperationsToolbar() {
		hideAll();
		
		editCancelMenuItem.setVisible(true);
		saveMenuItem.setVisible(true);
		addSeparator();
		
		sendMessage = new MenuItem("Enviar", new Command() {
			
			@Override
			public void execute() {
				onSendMessage();
			}
		});
		
		addItem(sendMessage);
		
		repeatMessage = new MenuItem("Repetir", new Command(){
			
			@Override
			public void execute() {
				onRepeatMessage();
			}
		});
		
		addItem(repeatMessage);
		
		receiveMessage = new MenuItem("Receber", new Command(){
			public void execute() {
				onReceiveMessage();
			};
		});
		
		addItem(receiveMessage);
		
		closeConversation = new MenuItem("Fechar Processo", new Command() {
			
			@Override
			public void execute() {
				onCloseConversation();
			}
		});
		addItem(closeConversation);
	}

	protected abstract void onSendMessage();
	
	protected abstract void onRepeatMessage();
	
	protected abstract void onReceiveMessage();
	
	protected abstract void onCloseConversation();

	public void allowEdit(boolean allow) {
		this.editCancelMenuItem.setEnabled(allow);
	}

	public void allowSendMessage(boolean hasPermission) {
		this.sendMessage.setEnabled(hasPermission);
	}

	public void allowReceiveMessage(boolean hasPermission) {
		this.receiveMessage.setEnabled(hasPermission);
	}

	public void allowRepeatMessage(boolean hasPermission) {
		this.repeatMessage.setEnabled(hasPermission);
	}

	public void allowClose(boolean hasPermission) {
		this.closeConversation.setEnabled(hasPermission);
	}

}
