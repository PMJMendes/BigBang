package bigBang.module.casualtyModule.client.userInterface;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem delete, markForClosing, close, rejectClose;
	protected MenuItem internalInfoRequest;
	protected MenuItem externalInfoRequesT;
	protected MenuItem markNotificationSent;

	public SubCasualtyOperationsToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.REQUESTS, true);
		showItem(SUB_MENU.ADMIN, true);
		showItem(SUB_MENU.EXECUTE, true);

		markForClosing = new MenuItem("Marcar para Encerramento", new Command() {

			@Override
			public void execute() {
				onMarkForClosing();
			}
		});
		addItem(SUB_MENU.ADMIN, markForClosing);

		close = new MenuItem("Encerrar", new Command() {

			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(SUB_MENU.ADMIN, close);

		rejectClose = new MenuItem("Rejeitar Encerramento", new Command() {

			@Override
			public void execute() {
				onRejectClose();
			}
		});
		addItem(SUB_MENU.ADMIN, rejectClose);

		adminSubMenu.addSeparator();
				
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, delete);
		
		internalInfoRequest = new MenuItem("Criação de Pedido de Informação", new Command() {
			
			@Override
			public void execute() {
				onInsurerInfoRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, internalInfoRequest);
		
		externalInfoRequesT = new MenuItem("Criação de Pedido de Informação Externo", new Command() {
			
			@Override
			public void execute() {
				onExternalInfoRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, externalInfoRequesT);
		
		markNotificationSent = new MenuItem("Marcar Participação Enviada", new Command() {
			
			@Override
			public void execute() {
				onMarkNotificationSent();
			}
		});
		
		addItem(SUB_MENU.EXECUTE, markNotificationSent);
	}

	protected abstract void onMarkNotificationSent();

	protected abstract void onInsurerInfoRequest();
	
	protected abstract void onExternalInfoRequest();

	public abstract void onDelete();
	
	public abstract void onClose();
	
	public abstract void onRejectClose();
	
	public abstract void onMarkForClosing(); 

	public void allowEdit(boolean allow) {
		this.setEditionAvailable(allow);
	}

	public void allowDelete(boolean allow){
		this.delete.setEnabled(allow);
	}

	public void allowMarkForClosing(boolean allow) {
		this.markForClosing.setEnabled(allow);
	}

	public void allowClose(boolean allow) {
		this.close.setEnabled(allow);
	}

	public void allowRejectClose(boolean allow) {
		this.rejectClose.setEnabled(allow);
	}

	public void allowInfoOrDocumentRequest(boolean hasPermission) {
		internalInfoRequest.setEnabled(hasPermission);
	}

	public void allowInsurerInfoRequest(boolean hasPermission) {
		externalInfoRequesT.setEnabled(hasPermission);
	}
	
	public void allowMarkNotificationSent(boolean hasPermission){
		markNotificationSent.setEnabled(hasPermission);
	}

}
