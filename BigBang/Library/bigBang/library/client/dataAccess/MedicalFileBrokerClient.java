package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.MedicalFile;

public interface MedicalFileBrokerClient extends DataBrokerClient<MedicalFile>{
	
	void updateMedicalFile(MedicalFile response);

}
