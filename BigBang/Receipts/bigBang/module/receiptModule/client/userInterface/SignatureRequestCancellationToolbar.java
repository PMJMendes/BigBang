package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SignatureRequestCancellationToolbar extends BigBangOperationsToolBar{

	public SignatureRequestCancellationToolbar(){
		MenuItem cancelRequestItem = new MenuItem("Cancelar Pedido de Assinatura", new Command() {
			
			@Override
			public void execute() {
				onCancelSignatureRequestRequest();
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
	
	public abstract void onCancelSignatureRequestRequest();

}
