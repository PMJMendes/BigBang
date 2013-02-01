package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.TotalLossFile;

public interface TotalLossFileBrokerClient extends DataBrokerClient<TotalLossFile>{

	void updateTotalLossFile(TotalLossFile result);

}
