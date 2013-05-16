package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;

public class ClientSendMessageView extends SendMessageView<Client> {

	public ClientSendMessageView(){
		super(new ClientForm());
	}

}
