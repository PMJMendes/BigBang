package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class AssessmentConversationViewPresenter extends ConversationViewPresenter<Assessment> {

	AssessmentBroker broker;
	protected Assessment assessment;
	
	public AssessmentConversationViewPresenter(
			bigBang.library.client.userInterface.presenter.ConversationViewPresenter.Display<Assessment> view) {
		super(view);
		broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<Assessment> handler) {
		broker.getAssessment(ownerId, new ResponseHandler<Assessment>() {
			
			@Override
			public void onResponse(Assessment response) {
				assessment = response;
				setContacts();
				handler.onResponse(response);
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a peritagem ou averiguação."), TYPE.ALERT_NOTIFICATION));
			}
		});		
	}
	
	protected void setContacts() {
		view.addContact("Sub-Sinistro (" + assessment.subCasualtyNumber + ")", assessment.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		view.setTypeAndOwnerId(BigBangConstants.EntityIds.SUB_CASUALTY, parameterHolder.getParameter("subcasualtyid"));
		ownerId = parameterHolder.getParameter("assessmentid");
		conversationId = parameterHolder.getParameter("conversationid");
		
		if(ownerId == null){
			onGetOwnerFailed();
		}
		else{
			getConversation();
			fillOwner(parameterHolder.getParameter("assessmentid"), new ResponseHandler<Assessment>() {
				
				@Override
				public void onResponse(Assessment response) {
					view.getOwnerForm().setValue(response);						
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();					
				}
			});
		}
	}
}
