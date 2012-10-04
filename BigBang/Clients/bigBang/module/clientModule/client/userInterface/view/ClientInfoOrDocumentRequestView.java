package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;

public class ClientInfoOrDocumentRequestView extends InfoOrDocumentRequestView<Client> {

	public ClientInfoOrDocumentRequestView(){
		super(new ClientForm());
	}

}
