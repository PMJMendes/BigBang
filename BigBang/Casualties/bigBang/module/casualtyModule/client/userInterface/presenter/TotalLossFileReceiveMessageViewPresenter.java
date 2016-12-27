package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;

public class TotalLossFileReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<TotalLossFile>{

	private TotalLossFileBroker broker;
	
	public TotalLossFileReceiveMessageViewPresenter(Display<TotalLossFile> view) {
		super(view);
		broker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
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

	@Override
	protected void showOwner(String ownerId) {
		broker.getTotalLossFile(ownerId, new ResponseHandler<TotalLossFile>() {

			@Override
			public void onResponse(TotalLossFile response) {
				view.getOwnerForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a perda total."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}		
}

