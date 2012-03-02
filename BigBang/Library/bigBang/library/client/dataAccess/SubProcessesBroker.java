package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;

public interface SubProcessesBroker extends DataBrokerInterface<BigBangProcess> {

	void registerClient(String ownerId, DataBrokerClient<BigBangProcess> client);
	
	void unregisterClient(String ownerId, DataBrokerClient<BigBangProcess> client);
	
	void getSubProcesses(String ownerId, ResponseHandler<Collection<BigBangProcess>> handler);
	
	void getSubProcess(String processId, ResponseHandler<BigBangProcess> handler);
	
}
