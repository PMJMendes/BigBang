package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.CostCenter;
import bigBang.definitions.shared.User;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CostCenterServiceAsync
	extends Service
{
	void getCostCenterList(AsyncCallback<CostCenter[]> callback);
	void getCostCenterUsers(String costCenterId, AsyncCallback<User[]> callback);
	void createCostCenter(CostCenter costCenter, AsyncCallback<CostCenter> callback);
	void saveCostCenter(CostCenter costCenter, AsyncCallback<CostCenter> callback);
	void deleteCostCenter(String id, AsyncCallback<Void> callback);
//	void addMembers(String costCenterId, String[] userIds, AsyncCallback<String> callback);
//	void removeMember(String costCenterId, String[] memberIds, AsyncCallback<String> callback);
//	void getAvailableUsersForMembership(String costCenterId, AsyncCallback<User[]> callback);
}
