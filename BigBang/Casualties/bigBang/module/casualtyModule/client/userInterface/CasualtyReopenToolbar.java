package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CasualtyReopenToolbar extends BigBangOperationsToolBar{

	protected MenuItem reopen, cancel;

	public CasualtyReopenToolbar() {
		hideAll();

		reopen = new MenuItem("Reabrir Sinistro", new Command() {

			@Override
			public void execute() {
				onReopen();
			}
		});
		addItem(reopen);

		addSeparator();

		cancel = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancel);
	}

	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public abstract void onReopen();


}
