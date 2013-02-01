package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;

public class MedicalFileReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<MedicalFile> {

	private MedicalFileBroker broker;
	
	public MedicalFileReceiveMessageViewPresenter(Display<MedicalFile> view) {
		super(view);
		broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
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
				onReceiveMessageFailed();
			}
		});		
	}

	@Override
	protected void showOwner(String ownerId) {
		broker.getMedicalFile(ownerId, new ResponseHandler<MedicalFile>() {
			
			@Override
			public void onResponse(MedicalFile response) {
				view.getOwnerForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a ficha clínica."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}
}
