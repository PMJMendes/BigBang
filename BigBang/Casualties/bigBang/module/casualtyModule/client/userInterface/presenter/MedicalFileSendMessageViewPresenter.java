package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;

public class MedicalFileSendMessageViewPresenter extends SendMessageViewPresenter<MedicalFile> {

	private MedicalFileBroker broker;
	protected MedicalFile medicalFile;
	
	public MedicalFileSendMessageViewPresenter(
			Display<MedicalFile> view) {
		super(view);
		broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<MedicalFile> responseHandler) {
		broker.getMedicalFile(ownerId, new ResponseHandler<MedicalFile>() {
			
			@Override
			public void onResponse(MedicalFile response) {
				medicalFile = response;
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
		view.addContact("Sub-Sinistro (FALTA NUMERO DO SUB-SINISTRO" +  medicalFile.subCasualtyId + ")", medicalFile.subCasualtyId , BigBangConstants.EntityIds.SUB_CASUALTY);
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

		ownerId = parameterHolder.getParameter("medicalfileid");

		clearView();

		if(ownerId.isEmpty()){
			onGetOwnerFailed();
		}else {
			showCreateRequest(ownerId);
		}
	}

}
