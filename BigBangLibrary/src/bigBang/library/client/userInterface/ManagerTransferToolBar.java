package bigBang.library.client.userInterface;


import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.ManagerTransfer.Status;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class ManagerTransferToolBar extends MenuBar{

	protected final String ACCEPT_TEXT = "Aceitar";
	protected final String REJECT_TEXT = "Rejeitar";
	protected final String CANCEL_TEXT = "Cancelar";
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

	private void accept(){

		Window.alert("ACCEPTED");
		//TODO
	}

	private void reject(){

		Window.alert("REJECTED");
		//TODO
	}

	private void cancel(){

		Window.alert("CANCELLED");
		//TODO
	}

}
