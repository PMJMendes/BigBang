package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NegotiationResponseToolBar extends BigBangOperationsToolBar{

	public NegotiationResponseToolBar(){
	MenuItem voidItem = new MenuItem("Receber resposta", new Command() {
			
			@Override
			public void execute() {
				onResponse();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(voidItem);
		addSeparator();
		addItem(cancelItem);
	}
	
	protected abstract void onResponse();
	
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
