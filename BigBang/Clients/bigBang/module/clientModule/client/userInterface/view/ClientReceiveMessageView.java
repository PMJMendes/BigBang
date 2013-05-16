package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;

public class ClientReceiveMessageView extends ReceiveMessageView<Client>{

	public ClientReceiveMessageView() {
		super(new ClientForm());
	}

}
