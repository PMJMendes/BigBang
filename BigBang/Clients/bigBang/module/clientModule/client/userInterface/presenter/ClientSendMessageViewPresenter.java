package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class ClientSendMessageViewPresenter extends
SendMessageViewPresenter<Client> {

	private ClientProcessBroker broker;
	protected Client client;


	public ClientSendMessageViewPresenter(Display<Client> view) {
		super(view);
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
		super.setParameters(parameterHolder);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<Client> handler) {
		broker.getClient(ownerId, new ResponseHandler<Client>() {


			@Override
			public void onResponse(Client response) {
				client = response;
				setContacts();
				handler.onResponse(response);//TODO REQUESTS
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetContactsFailed();
			}
		});
	}

	private void setContacts(){
		view.addContact("Cliente (" + client.name +")", client.id, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Mediador (" + client.mediatorName + ")", client.mediatorId, BigBangConstants.EntityIds.MEDIATOR);
	}

	@Override
	protected void send() {
		broker.sendMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onSendRequestSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});
	}
}
