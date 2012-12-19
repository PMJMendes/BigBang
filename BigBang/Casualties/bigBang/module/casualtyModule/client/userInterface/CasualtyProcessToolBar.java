package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CasualtyProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem subCasualty;

	//DATA
	protected MenuItem transferManager;
	
	//REQUESTS
	protected MenuItem sendMessage;
	
	//EXECUTE
	protected MenuItem reopenSubCasualty;
	
	//ADMIN
	protected MenuItem delete, close, reopen;

	private MenuItem receiveMessage;


	public CasualtyProcessToolBar(){

		//CREATE
		subCasualty = new MenuItem("Sub-Sinistro", new Command() {

			@Override
			public void execute() {
				onCreateSubCasualty();
			}
		});
		addItem(SUB_MENU.CREATE, subCasualty);
		
		//DATA
		transferManager = new MenuItem("Transferência de Gestor", new Command() {

			@Override
			public void execute() {
				onTransferManager();
			}
		});
		addItem(SUB_MENU.DATA, transferManager);

		//REQUESTS
		sendMessage = new MenuItem("Enviar Mensagem", new Command() {
			
			@Override
			public void execute() {
				onSendMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, sendMessage);
		
		receiveMessage = new MenuItem("Receber Mensagem", new Command() {
			
			@Override
			public void execute() {
				onReceiveMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, receiveMessage);
		//ADMIN
		close = new MenuItem("Encerrar", new Command() {

			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(SUB_MENU.ADMIN, close);

		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, delete);
		
		adminSubMenu.addSeparator();
		
		reopen = new MenuItem("Reabrir", new Command(){

			@Override
			public void execute() {
				onReopen();
			}
			
		});
		addItem(SUB_MENU.ADMIN, reopen);
		
		reopenSubCasualty = new MenuItem("Reabrir Sub-Sinistro", new Command(){

			@Override
			public void execute() {
				onSubCasualtyReopen();
			}
			
		});
		addItem(SUB_MENU.EXECUTE, reopenSubCasualty);

		
	}

	public abstract void onSubCasualtyReopen();

	public abstract void onReopen();

	public abstract void onReceiveMessage();

	public abstract void onSendMessage();

	public abstract void onCreateSubCasualty();

	public abstract void onClose();

	public abstract void onDelete();	

	public abstract void onTransferManager();


	public void allowCreateSubCasualty(boolean allow){
		this.subCasualty.setEnabled(allow);
	}

	public void allowDelete(boolean allow){
		this.delete.setEnabled(allow);
	}

	public void allowClose(boolean allow){
		this.close.setEnabled(allow);
	}

	public void allowManagerTransfer(boolean allow){
		this.transferManager.setEnabled(allow);
	}

	public void allowSendMessage(boolean hasPermission) {
		sendMessage.setEnabled(hasPermission);
	}

	public void allowReceiveMessage(boolean b) {
		receiveMessage.setEnabled(b);
	}

	public void allowReopen(boolean b) {
		reopen.setEnabled(b);
	}

	public void allowReopenSubCasualty(boolean hasPermission) {
		reopenSubCasualty.setEnabled(hasPermission);
	}

}
