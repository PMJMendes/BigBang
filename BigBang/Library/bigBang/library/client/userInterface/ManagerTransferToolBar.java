package bigBang.library.client.userInterface;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ManagerTransferToolBar extends MenuBar{

	protected final String ACCEPT_TEXT = "Aceitar Transferência";
	protected final String REJECT_TEXT = "Rejeitar Transferência";
	protected final String CANCEL_TEXT = "Cancelar Transferência";
	public MenuItem accept;
	public MenuItem reject;
	public MenuItem cancel;
	private Command acceptC, rejectC, cancelC;

	public ManagerTransferToolBar(boolean b) {
		super(b);
		createCommands();

		this.accept = new BigBangMenuItem(ACCEPT_TEXT, acceptC);
		this.reject = new BigBangMenuItem(REJECT_TEXT, rejectC);
		this.cancel = new BigBangMenuItem(CANCEL_TEXT, cancelC);
		this.addItem(this.accept);
		this.addItem(this.reject);
		this.addItem(this.cancel);
	}

	private void createCommands(){
		acceptC = new Command(){
			public void execute(){
				accept();
			}

		};
		rejectC = new Command(){
			public void execute(){
				reject();
			}
		};
		cancelC = new Command(){
			public void execute(){
				cancel();
			}
		};
	}

	public abstract void accept();

	public abstract void reject();

	public abstract void cancel();

}
