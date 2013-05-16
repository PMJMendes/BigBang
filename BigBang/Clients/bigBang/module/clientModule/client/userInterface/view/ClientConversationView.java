package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;

public class ClientConversationView extends ConversationView<Client>{

	public ClientConversationView() {
		super(new ClientForm());
	}

}
