package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsurancePolicyCreateSubPolicyReceiptToolbar extends BigBangOperationsToolBar{

	private MenuItem createDebitNoteItem;
	private MenuItem cancelItem;


	public InsurancePolicyCreateSubPolicyReceiptToolbar() {

		hideAll();
		this.createDebitNoteItem = new MenuItem("Criar Nota de Débito", new Command() {

			@Override
			public void execute() {
				onCreateDebitNote();
			}
		});
		addItem(this.createDebitNoteItem);
		addSeparator();

		this.cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(this.cancelItem);
	}

	protected abstract void onCreateDebitNote();

	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;

	}

}
