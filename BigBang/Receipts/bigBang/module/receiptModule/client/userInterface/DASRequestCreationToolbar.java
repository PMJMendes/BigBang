package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class DASRequestCreationToolbar extends BigBangOperationsToolBar{
	
	public DASRequestCreationToolbar() {
		
		MenuItem createItem = new MenuItem("Criar Pedido de DAS", new Command() {

			@Override
			public void execute() {
				onCreateDASRequest();
			}
		});

		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});

		hideAll();
		addItem(createItem);
		addSeparator();
		addItem(cancelItem);
		
	}
	
	public abstract void onCreateDASRequest();

	@Override
	public void onEditRequest() {
	return;
	}

	@Override
	public void onSaveRequest() {
	return;
	}

	@Override
	public abstract void onCancelRequest();

}
