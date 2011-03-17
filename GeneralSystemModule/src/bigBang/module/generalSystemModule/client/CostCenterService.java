package bigBang.module.generalSystemModule.client;

import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CostCenterService")
public interface CostCenterService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CostCenterServiceAsync instance;
		public static CostCenterServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(CostCenterService.class);
			}
			return instance;
		}
	}
	
	public CostCenter[] getCostCenterList();
	
	public CostCenter getCostCenter(String id);

	public String createCostCenter(CostCenter costCenter);
	
	public String saveCostCenter(CostCenter costCenter);
	
	public String deleteCostCenter(String id);

	public String addMember(String costCenterId, String userId);
	
	public String removeMember(String costCenterId, String[] memberIds);

	public User[] getAvailableUsersForMembership(String costCenterId);

}
