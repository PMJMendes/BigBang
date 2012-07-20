package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class DASRequestRepetitionToolbar extends BigBangOperationsToolBar{
	
	public DASRequestRepetitionToolbar() {
		
		MenuItem createItem = new MenuItem("Repetir Pedido de DAS", new Command() {

			@Override
			public void execute() {
				onRepeatDASRequest();
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
	
	public abstract void onRepeatDASRequest();

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
