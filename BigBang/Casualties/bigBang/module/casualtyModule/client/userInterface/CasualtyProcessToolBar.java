package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CasualtyProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem subCasualty;

	//DATA
	protected MenuItem transferManager;
	
	//ADMIN
	protected MenuItem delete, close;


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

		//		MenuBar executeSubMenu = new MenuBar(true);
		//		MenuItem executeMenuItem = new MenuItem("Sinistro", executeSubMenu);
		//		executeSubMenu.addItem("Pedir Informação ou Documento ao Cliente", new Command() {
		//
		//			@Override
		//			public void execute() {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});
		//		
		//		addItem(executeMenuItem);
		//		
		//		MenuBar casualtySubMenu = new MenuBar(true);
		//		MenuItem policyMenuItem = new MenuItem("Outras", casualtySubMenu);
		//		casualtySubMenu.addItem("Encerrar", new Command() {
		//
		//			@Override
		//			public void execute() {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});
		//		casualtySubMenu.addItem("Reabrir", new Command() {
		//
		//			@Override
		//			public void execute() {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});
		//		casualtySubMenu.addItem("Eliminar", new Command() {
		//
		//			@Override
		//			public void execute() {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});
		//		
		//		addItem(policyMenuItem);
	}

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

}
