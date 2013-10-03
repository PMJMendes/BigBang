package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class ConversationTasksOperationsToolbar extends BigBangOperationsToolBar{
	protected OperationsToolBar sendSubMenu;
	protected MenuItem sendItem;

	protected MenuItem goToProcess;
	private MenuItem closeMenuItem;
	private MenuItem receiveMenuItem;
	private MenuItem repeatMenuItem;
	private MenuItem newMenuItem;
	protected MenuItem replyMenuItem;
	protected MenuItem replyAllMenuItem;
	protected MenuItem forwardMenuItem;
	
	protected ConversationTasksOperationsToolbar(){
		hideAll();

		this.sendSubMenu = new OperationsToolBar(true);
		sendItem = new BigBangMenuItem("Enviar", this.sendSubMenu);
		insertItem(sendItem, 0);
		
		newMenuItem = new MenuItem("Nova", new Command() {
			
			@Override
			public void execute() {
				onNew();
			}
			
		});
		sendSubMenu.addItem(newMenuItem);

		sendSubMenu.addSeparator();

		replyMenuItem = new MenuItem("Responder", new Command() {
			
			@Override
			public void execute() {
				onReply();
			}
		});
		sendSubMenu.addItem(replyMenuItem);

		replyAllMenuItem = new MenuItem("Responder a Todos", new Command() {
			
			@Override
			public void execute() {
				onReplyAll();
			}
		});
		sendSubMenu.addItem(replyAllMenuItem);

		forwardMenuItem = new MenuItem("Reencaminhar", new Command() {
			
			@Override
			public void execute() {
				onForward();
			}
		});
		sendSubMenu.addItem(forwardMenuItem);

		sendSubMenu.addSeparator();
		
		repeatMenuItem = new MenuItem("Repetir", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
			
		});
		sendSubMenu.addItem(repeatMenuItem);
		
		receiveMenuItem = new MenuItem("Receber", new Command() {
			
			@Override
			public void execute() {
				onReceive();
			}
			
		});
		addItem(receiveMenuItem);
		
		closeMenuItem = new MenuItem("Fechar troca de mensagens", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
			
		});
		addItem(closeMenuItem);
		
		goToProcess = new MenuItem("Navegar para processo auxiliar", new Command() {

			@Override
			public void execute() {
				onGoToProcess();
			}
			
		});
		addItem(goToProcess);
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
	
	protected abstract void onGoToProcess();
	protected abstract void onClose();
	protected abstract void onRepeat();
	protected abstract void onNew();
	protected abstract void onReply();
	protected abstract void onReplyAll();
	protected abstract void onForward();
	protected abstract void onReceive();
	
	
	public void allowClose(boolean b) {
		closeMenuItem.setEnabled(b);
	}
	public void allowReceive(boolean b) {
		receiveMenuItem.setEnabled(b);
	}
	public void allowRepeat(boolean b) {
		repeatMenuItem.setEnabled(b);
	}
	public void allowNew(boolean b) {
		newMenuItem.setEnabled(b);
	}
	public void allowReply(boolean b) {
		replyMenuItem.setEnabled(b);
	}
	public void allowReplyAll(boolean b) {
		replyAllMenuItem.setEnabled(b);
	}
	public void allowForward(boolean b) {
		forwardMenuItem.setEnabled(b);
	}

	@Override
	public void lockAll() {
		super.lockAll();
		goToProcess.setEnabled(true);
	}

}
