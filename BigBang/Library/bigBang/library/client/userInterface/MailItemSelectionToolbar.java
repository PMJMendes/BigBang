package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class MailItemSelectionToolbar extends BigBangOperationsToolBar{

	public MailItemSelectionToolbar(){
		MenuItem confirm = new MenuItem("Confirmar", new Command() {

			@Override
			public void execute() {
				onConfirmRequest();

			}
		});

		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});

		hideAll();
		addItem(confirm);
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

	public abstract void onConfirmRequest();


}
