package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ConversationOperationsToolbar extends BigBangOperationsToolBar{
	protected OperationsToolBar sendSubMenu;
	protected MenuItem sendItem;

	protected MenuItem emptyMessage;
	protected MenuItem replyMessage;
	protected MenuItem replyAllMessage;
	protected MenuItem forwardMessage;
	protected MenuItem repeatMessage;
	protected MenuItem receiveMessage;
	protected MenuItem closeConversation;
	protected MenuItem reopenConversation;
	
	public ConversationOperationsToolbar() {
		hideAll();

		editCancelMenuItem.setVisible(true);
		saveMenuItem.setVisible(true);
		adminMenuItem.setVisible(true);

		insertSeparator(2);

		this.sendSubMenu = new OperationsToolBar(true);
		sendItem = new BigBangMenuItem("Enviar", this.sendSubMenu);
		insertItem(sendItem, 3);

		emptyMessage = new MenuItem("Nova", new Command() {
			
			@Override
			public void execute() {
				onNewMessage();
			}
		});
		sendSubMenu.addItem(emptyMessage);

		sendSubMenu.addSeparator();

		replyMessage = new MenuItem("Responder", new Command() {
			
			@Override
			public void execute() {
				onMessageReply();
			}
		});
		sendSubMenu.addItem(replyMessage);

		replyAllMessage = new MenuItem("Responder a Todos", new Command() {
			
			@Override
			public void execute() {
				onMessageReplyAll();
			}
		});
		sendSubMenu.addItem(replyAllMessage);

		forwardMessage = new MenuItem("Reencaminhar", new Command() {
			
			@Override
			public void execute() {
				onForwardMessage();
			}
		});
		sendSubMenu.addItem(forwardMessage);

		sendSubMenu.addSeparator();

		repeatMessage = new MenuItem("Repetir", new Command(){
			
			@Override
			public void execute() {
				onRepeatMessage();
			}
		});
		sendSubMenu.addItem(repeatMessage);
		
		receiveMessage = new MenuItem("Receber", new Command(){
			public void execute() {
				onReceiveMessage();
			};
		});
		insertItem(receiveMessage, 4);
		
		closeConversation = new MenuItem("Fechar Troca de Mensagens", new Command() {
			
			@Override
			public void execute() {
				onCloseConversation();
			}
		});
		addItem(SUB_MENU.ADMIN, closeConversation);
		
		reopenConversation = new MenuItem("Reabrir", new Command(){
			
			@Override
			public void execute() {
				onReopenConversation();
			}
			
		});
		addItem(SUB_MENU.ADMIN, reopenConversation);
	}

	protected abstract void onReopenConversation();

	protected abstract void onNewMessage();
	
	protected abstract void onMessageReply();
	
	protected abstract void onMessageReplyAll();
	
	protected abstract void onForwardMessage();
	
	protected abstract void onRepeatMessage();
	
	protected abstract void onReceiveMessage();
	
	protected abstract void onCloseConversation();

	public void allowEdit(boolean allow) {
		this.editCancelMenuItem.setEnabled(allow);
	}

	public void allowNewMessage(boolean hasPermission) {
		this.emptyMessage.setEnabled(hasPermission);
	}

	public void allowReplyMessage(boolean hasPermission) {
		this.replyMessage.setEnabled(hasPermission);
	}

	public void allowReplyAllMessage(boolean hasPermission) {
		this.replyAllMessage.setEnabled(hasPermission);
	}

	public void allowForwardMessage(boolean hasPermission) {
		this.forwardMessage.setEnabled(hasPermission);
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

	public void allowReopen(boolean hasPermission) {
		reopenConversation.setEnabled(hasPermission);
	}

}
