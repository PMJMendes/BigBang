package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class VoidDebitNoteToolbar extends BigBangOperationsToolBar{

	public VoidDebitNoteToolbar() {
		MenuItem cancelRequestItem = new MenuItem("Anular Nota de Débito", new Command() {

			@Override
			public void execute() {
				onVoidDebitNote();
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

	protected abstract void onVoidDebitNote();
	
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
