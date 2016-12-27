package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class AssessmentReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Assessment>{

	private AssessmentBroker broker;
	
	public AssessmentReceiveMessageViewPresenter(Display<Assessment> view) {
		super(view);
		broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
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
				onReceiveMessageFailed(error);;;
			}
		});		
	}

	@Override
	protected void showOwner(String ownerId) {
		broker.getAssessment(ownerId, new ResponseHandler<Assessment>() {
			
			@Override
			public void onResponse(Assessment response) {
				view.getOwnerForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a peritagem ou averiguação."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

}
