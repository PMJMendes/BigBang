package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NegotiationGrantToolBar extends BigBangOperationsToolBar{


	public NegotiationGrantToolBar(){

		MenuItem voidItem = new MenuItem("Adjudicar", new Command() {

			@Override
			public void execute() {
				onGrantNegotiationRequest();
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

	public abstract void onCancelRequest();

	public abstract void onGrantNegotiationRequest();

}

