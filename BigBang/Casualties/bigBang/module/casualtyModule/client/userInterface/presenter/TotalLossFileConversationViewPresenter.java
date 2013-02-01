package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;

public class TotalLossFileConversationViewPresenter extends ConversationViewPresenter<TotalLossFile>{

	private TotalLossFileBroker broker;
	protected TotalLossFile file;
	
	public TotalLossFileConversationViewPresenter(
			bigBang.library.client.userInterface.presenter.ConversationViewPresenter.Display<TotalLossFile> view) {
		super(view);
		broker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<TotalLossFile> handler) {
		broker.getTotalLossFile(ownerId, new ResponseHandler<TotalLossFile>() {
			
			@Override
			public void onResponse(TotalLossFile response) {
				file = response;
				setContacts();
				handler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a perda total."), TYPE.ALERT_NOTIFICATION));

			}
		});
		
	}
	
	protected void setContacts() {
		view.addContact("Sub-Sinistro (" + file.subCasualtyNumber + ")", file.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);		
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		view.setTypeAndOwnerId(BigBangConstants.EntityIds.SUB_CASUALTY, parameterHolder.getParameter("subcasualtyid"));
		ownerId = parameterHolder.getParameter("totallossfileid");
		conversationId = parameterHolder.getParameter("conversationid");
		
		if(ownerId == null){
			onGetOwnerFailed();
		}
		else{
			getConversation();
			fillOwner(parameterHolder.getParameter("totallossfileid"), new ResponseHandler<TotalLossFile>() {
				
				@Override
				public void onResponse(TotalLossFile response) {
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
