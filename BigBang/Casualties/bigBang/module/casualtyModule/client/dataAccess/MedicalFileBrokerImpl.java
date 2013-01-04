package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.module.casualtyModule.interfaces.MedicalFileService;
import bigBang.module.casualtyModule.interfaces.MedicalFileServiceAsync;

public class MedicalFileBrokerImpl extends DataBroker<MedicalFile> implements MedicalFileBroker{

	protected MedicalFileServiceAsync service;
	
	public MedicalFileBrokerImpl(){
		this.service = MedicalFileService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.MEDICAL_FILE;
	}
	
	@Override
	public void requireDataRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMedicalFile(String id, ResponseHandler<MedicalFile> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editMedicalFile(MedicalFile file,
			ResponseHandler<MedicalFile> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(Conversation conversation,
			ResponseHandler<Conversation> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveMessage(Conversation conversation,
			ResponseHandler<Conversation> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeProcess(String id, ResponseHandler<MedicalFile> handler) {
		// TODO Auto-generated method stub
		
	}

}
