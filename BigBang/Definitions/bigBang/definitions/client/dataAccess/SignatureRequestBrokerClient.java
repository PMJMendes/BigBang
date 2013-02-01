package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.SignatureRequest;

public interface SignatureRequestBrokerClient {

	public void updateSignatureRequest(SignatureRequest request);
	
	public void setDataVersionNumber(String id, int currentDataVersion);
	
	public void removeSignatureRequest(String id);
	
}
