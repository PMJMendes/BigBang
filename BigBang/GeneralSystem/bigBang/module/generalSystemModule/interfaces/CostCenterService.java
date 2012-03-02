package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.CostCenter;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CostCenterService")
public interface CostCenterService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static CostCenterServiceAsync instance;
		public static CostCenterServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(CostCenterService.class);
			}
			return instance;
		}
	}
	
	public CostCenter[] getCostCenterList() throws SessionExpiredException, BigBangException;
	public CostCenter createCostCenter(CostCenter costCenter) throws SessionExpiredException, BigBangException;
	public CostCenter saveCostCenter(CostCenter costCenter) throws SessionExpiredException, BigBangException;
	public void deleteCostCenter(String id) throws SessionExpiredException, BigBangException;
//	public String addMembers(String costCenterId, String[] userIds) throws BigBangException;
//	public String removeMember(String costCenterId, String[] memberIds) throws BigBangException;
//	public User[] getAvailableUsersForMembership(String costCenterId) throws BigBangException;
}
