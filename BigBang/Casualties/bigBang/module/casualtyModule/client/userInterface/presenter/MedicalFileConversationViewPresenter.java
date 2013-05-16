package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;

public class MedicalFileConversationViewPresenter extends ConversationViewPresenter<MedicalFile> {

	MedicalFileBroker broker;
	MedicalFile file;
	
	public MedicalFileConversationViewPresenter(Display<MedicalFile> view) {
		super(view);
		broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<MedicalFile> handler) {
		broker.getMedicalFile(ownerId, new ResponseHandler<MedicalFile>() {
			
			@Override
			public void onResponse(MedicalFile response) {
				file = response;
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
		view.addContact("Sub-Sinistro (" +  file.subCasualtyNumber + ")", file.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		view.setTypeAndOwnerId(BigBangConstants.EntityIds.SUB_CASUALTY, parameterHolder.getParameter("subcasualtyid"));
		ownerId = parameterHolder.getParameter("medicalfileid");
		conversationId = parameterHolder.getParameter("conversationid");
		
		if(ownerId == null){
			onGetOwnerFailed();
		}
		else{
			getConversation();
			fillOwner(parameterHolder.getParameter("medicalfileid"), new ResponseHandler<MedicalFile>() {
				
				@Override
				public void onResponse(MedicalFile response) {
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
