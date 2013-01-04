package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class CasualtySendMessageViewPresenter extends SendMessageViewPresenter<Casualty>{

	private CasualtyDataBroker broker;
	protected Casualty casualty;

	public CasualtySendMessageViewPresenter(Display<Casualty> view) {
		super(view);
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<Casualty> handler) {
		broker.getCasualty(ownerId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				casualty = response;
				setContacts();
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}


	protected void setContacts() {
		view.addContact("Sinistro (" + casualty.processNumber + ")", casualty.id, BigBangConstants.EntityIds.CASUALTY);
		view.addContact("Cliente (" + casualty.clientName + ")", casualty.clientId, BigBangConstants.EntityIds.CLIENT);		
		view.addContact("Mediador (" + casualty.inheritMediatorName + ")", casualty.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CASUALTY);
		super.setParameters(parameterHolder);
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
