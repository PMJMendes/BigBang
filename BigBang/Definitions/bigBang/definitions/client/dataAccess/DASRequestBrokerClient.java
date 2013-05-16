package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.DASRequest;

public interface DASRequestBrokerClient {
	
	public void updateDASRequest(DASRequest request);
	
	public void setDataVersionNumber(String id, int currentDataVersion);
	
	public void removeDASRequest(String id);
	

}
