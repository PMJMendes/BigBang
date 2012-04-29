package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ReceiptTasksOperationsToolbar extends BigBangOperationsToolBar{

	protected MenuItem createDAS, markUnnecessary;
	
	public ReceiptTasksOperationsToolbar(){
		hideAll();
		
		createDAS = new MenuItem("Criar Pedido de DAS", new Command() {
			
			@Override
			public void execute() {
				onCreateDASRequest();
			}
		});
		addItem(createDAS);
		
		markUnnecessary = new MenuItem("Marcar DAS como Desnecessário", new Command() {
			
			@Override
			public void execute() {
				onMarkDASUnnecessary();
			}
		});
		addItem(markUnnecessary);
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
	
	public abstract void onCreateDASRequest();
	
	public abstract void onMarkDASUnnecessary();
	
	public void allowCreateDASRequest(boolean allow) {
		createDAS.setVisible(allow);
	}
	
	public void allowMarkDASUnnecessary(boolean allow) {
		markUnnecessary.setVisible(allow);
	}

}
