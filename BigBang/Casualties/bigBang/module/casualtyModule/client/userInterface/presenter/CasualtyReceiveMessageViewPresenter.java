package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class CasualtyReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Casualty> {

	CasualtyDataBroker broker;
	
	public CasualtyReceiveMessageViewPresenter(Display<Casualty> view) {
		super(view);
		broker = (CasualtyDataBroker) DataBrokerManager.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY);
	}

	@Override
	protected void showOwner(String ownerId) {
		broker.getCasualty(ownerId, new ResponseHandler<Casualty>() {
			
			@Override
			public void onResponse(Casualty response) {
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
