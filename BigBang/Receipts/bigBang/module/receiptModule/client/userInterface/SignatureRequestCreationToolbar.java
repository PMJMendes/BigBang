package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SignatureRequestCreationToolbar extends BigBangOperationsToolBar{

	public SignatureRequestCreationToolbar(){
		MenuItem createItem = new MenuItem("Criar Pedido de Assinatura", new Command() {

			@Override
			public void execute() {
				onCreateSignatureRequest();
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

	public abstract void onCreateSignatureRequest();

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
