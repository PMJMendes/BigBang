package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;

public class TotalLossFileSendMessageViewPresenter extends SendMessageViewPresenter<TotalLossFile> {

	private TotalLossFileBroker broker;
	protected TotalLossFile file;
	
	public TotalLossFileSendMessageViewPresenter(
			bigBang.library.client.userInterface.presenter.SendMessageViewPresenter.Display<TotalLossFile> view) {
		super(view);
		broker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<TotalLossFile> responseHandler) {
		broker.getTotalLossFile(ownerId, new ResponseHandler<TotalLossFile>() {
			@Override
			public void onResponse(TotalLossFile response) {
				file = response;
				setContacts();
				responseHandler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
		
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
	
	protected void setContacts() {
		view.addContact("Sub-Sinistro (" +  file.subCasualtyNumber + ")", file.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {		

		view.setTypeAndOwnerId(BigBangConstants.EntityIds.SUB_CASUALTY, parameterHolder.getParameter("subcasualtyid"));

		ownerId = parameterHolder.getParameter("totallossfileid");

		clearView();

		if(ownerId.isEmpty()){
			onGetOwnerFailed();
		}else {
			showCreateRequest(ownerId);
		}
	}
}
