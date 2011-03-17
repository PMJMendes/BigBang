package bigBang.module.generalSystemModule.client;

import bigBang.library.shared.Service;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CostCenterServiceAsync extends Service {

	void getCostCenterList(AsyncCallback<CostCenter[]> callback);

	void getCostCenter(String id, AsyncCallback<CostCenter> callback);

	void createCostCenter(CostCenter costCenter, AsyncCallback<String> callback);

	void deleteCostCenter(String id, AsyncCallback<String> callback);

	void saveCostCenter(CostCenter costCenter, AsyncCallback<String> callback);
	
	void addMember(String costCenterId, String userId, AsyncCallback<String> callback);
	
	void removeMember(String costCenterId, String[] memberIds, AsyncCallback<String> callback);
	
	void getAvailableUsersForMembership(String costCenterId, AsyncCallback<User[]> callback);

}
