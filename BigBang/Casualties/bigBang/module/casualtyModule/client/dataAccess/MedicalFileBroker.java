package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFileStub;

public interface MedicalFileBroker extends DataBrokerInterface<MedicalFile>{
	
	public void getMedicalFile(String id, ResponseHandler<MedicalFile> handler);
	
	public void editMedicalFile(MedicalFile file, ResponseHandler<MedicalFile> handler);
	
	public void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	public void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);	
	
	public void closeProcess(String id, ResponseHandler<MedicalFile> handler);

	public SearchDataBroker<MedicalFileStub> getSearchBroker();
}
