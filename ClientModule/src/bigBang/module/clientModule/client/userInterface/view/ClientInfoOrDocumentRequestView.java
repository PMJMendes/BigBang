package bigBang.module.clientModule.client.userInterface.view;

import bigBang.module.clientModule.client.userInterface.presenter.ClientInfoOrDocumentRequestViewPresenter;
import bigBang.definitions.shared.Client;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;

public class ClientInfoOrDocumentRequestView extends InfoOrDocumentRequestView implements ClientInfoOrDocumentRequestViewPresenter.Display {

	private ClientFormView clientForm;
	
	public ClientInfoOrDocumentRequestView(){
		super();
		clientForm = new ClientFormView();
		clientForm.setSize("100%", "100%");
		ownerFormContainer.add(clientForm);
	}

	@Override
	public HasEditableValue<Client> getOwnerForm() {
		return clientForm;
	}
	
}
