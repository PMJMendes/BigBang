package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class ConversationTasksOperationsToolbar extends BigBangOperationsToolBar{

	protected MenuItem goToProcess;
	private MenuItem closeMenuItem;
	private MenuItem receiveMenuItem;
	private MenuItem repeatMenuItem;
	private MenuItem sendMenuItem;
	
	protected ConversationTasksOperationsToolbar(){
		hideAll();
		
		sendMenuItem = new MenuItem("Enviar", new Command() {
			
			@Override
			public void execute() {
				onSend();
			}
			
		});
		
		addItem(sendMenuItem);
		
		repeatMenuItem = new MenuItem("Repetir", new Command() {
			
			@Override
			public void execute() {
				onRepeat();
			}
			
		});
		
		addItem(repeatMenuItem);
		
		receiveMenuItem = new MenuItem("Receber", new Command() {
			
			@Override
			public void execute() {
				onReceive();
			}
			
		});
		
		addItem(receiveMenuItem);
		
		closeMenuItem = new MenuItem("Fechar processo", new Command() {
			
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
	protected abstract void onSend();
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
	public void allowSend(boolean b) {
		sendMenuItem.setEnabled(b);
	}
	
	@Override
	public void lockAll() {
		super.lockAll();
		goToProcess.setEnabled(true);
	}

}
