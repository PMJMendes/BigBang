package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class ClientReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Client> {

	private ClientProcessBroker broker;
	
	public ClientReceiveMessageViewPresenter(Display<Client> view) {
		super(view);
		broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
	}
	
	@Override
	protected void showOwner(String ownerId) {
		broker.getClient(ownerId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getOwnerForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}
	
	@Override
	protected void receive() {
		broker.receiveMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onReceiveMessageSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				String error = "";
				for (ResponseError tmp : errors) {
					error = error + tmp.description;
				}
				onReceiveMessageFailed(error);;
			}
		});
	}
}
