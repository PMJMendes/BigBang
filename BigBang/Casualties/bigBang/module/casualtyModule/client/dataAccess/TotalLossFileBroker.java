package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.client.dataAccess.SearchDataBroker;


public interface TotalLossFileBroker extends DataBrokerInterface<TotalLossFile>{

	public void getTotalLossFile(String id, ResponseHandler<TotalLossFile> handler);
	
	public void editTotalLossFile(TotalLossFile file, ResponseHandler<TotalLossFile> handler);
	
	public void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	public void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);	

	public void closeProcess(String id, ResponseHandler<TotalLossFile> handler);
	
	public SearchDataBroker<TotalLossFileStub> getSearchBroker();
	
}
