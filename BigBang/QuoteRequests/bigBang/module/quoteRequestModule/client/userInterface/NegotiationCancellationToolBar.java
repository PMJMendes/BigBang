package bigBang.module.quoteRequestModule.client.userInterface;


import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class NegotiationCancellationToolBar extends BigBangOperationsToolBar
{

	public NegotiationCancellationToolBar(){
		MenuItem voidItem = new MenuItem("Cancelar Negociação", new Command() {
			
			@Override
			public void execute() {
				onCancelNegotiationRequest();
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
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}
	
	public abstract void onCancelNegotiationRequest();
	
	public abstract void onCancelRequest();
	
}
	
