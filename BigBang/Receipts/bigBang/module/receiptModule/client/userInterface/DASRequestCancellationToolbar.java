package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class DASRequestCancellationToolbar extends BigBangOperationsToolBar{

	public DASRequestCancellationToolbar(){
		MenuItem cancelRequestItem = new MenuItem("Cancelar pedido de DAS", new Command() {

			@Override
			public void execute() {
				onCancelDASRequestRequest();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(cancelRequestItem);
		addSeparator();
		addItem(cancelItem);
	}

	public abstract void onCancelDASRequestRequest();

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
