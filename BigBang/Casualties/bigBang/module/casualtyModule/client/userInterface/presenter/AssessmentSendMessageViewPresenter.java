package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class AssessmentSendMessageViewPresenter extends SendMessageViewPresenter<Assessment>{

	private AssessmentBroker broker;
	protected Assessment assessment;

	public AssessmentSendMessageViewPresenter(
			Display<Assessment> view) {
		super(view);
		broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<Assessment> responseHandler) {
		broker.getAssessment(ownerId, new ResponseHandler<Assessment>() {

			@Override
			public void onResponse(Assessment response) {
				assessment = response;
				setContacts();
				responseHandler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}

	protected void setContacts() {
		view.addContact("Sub-Sinistro (" + assessment.subCasualtyNumber + ")", assessment.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {		

		view.setTypeAndOwnerId(BigBangConstants.EntityIds.SUB_CASUALTY, parameterHolder.getParameter("subcasualtyid"));

		ownerId = parameterHolder.getParameter("assessmentid");

		clearView();

		if(ownerId.isEmpty()){
			onGetOwnerFailed();
		}else {
			showCreateRequest(ownerId);
		}
	}

}
