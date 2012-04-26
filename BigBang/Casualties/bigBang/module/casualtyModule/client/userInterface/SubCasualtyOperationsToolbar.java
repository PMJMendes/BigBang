package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyOperationsToolbar extends BigBangOperationsToolBar {

	protected MenuItem delete;
	protected MenuItem infoOrDocumentRequest;
	protected MenuItem insurerInfoRequest;
	
	public SubCasualtyOperationsToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.ADMIN, true);
		showItem(SUB_MENU.REQUESTS, true);
		
		delete = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, delete);
		
		infoOrDocumentRequest = new MenuItem("Criação de Pedido de Informação à Seguradora", new Command() {
			
			@Override
			public void execute() {
				insurerInfoRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, infoOrDocumentRequest);
		
		insurerInfoRequest = new MenuItem("Criação de Pedido de Informação Externo da Seguradora", new Command() {
			
			@Override
			public void execute() {
				externalInfoRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, insurerInfoRequest);
	}
	
	protected abstract void externalInfoRequest();

	protected abstract void insurerInfoRequest();

	public abstract void onDelete();

	public void allowEdit(boolean allow) {
		this.setEditionAvailable(allow);
	}
	
	public void allowDelete(boolean allow){
		this.delete.setEnabled(allow);
	}

	public void allowInfoOrDocumentRequest(boolean hasPermission) {
			infoOrDocumentRequest.setEnabled(hasPermission);
	}

	public void allowInsurerInfoRequest(boolean hasPermission) {
		insurerInfoRequest.setEnabled(hasPermission);
		
	}
	
}
