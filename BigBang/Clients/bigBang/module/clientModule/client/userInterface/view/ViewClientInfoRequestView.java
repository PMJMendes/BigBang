package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;

public class ViewClientInfoRequestView extends ViewInfoOrDocumentRequestView<Client> {

	public ViewClientInfoRequestView() {
		super(new ClientFormView());
	}

}
