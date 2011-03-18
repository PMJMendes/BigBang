package bigBang.module.generalSystemModule.server;

import bigBang.module.generalSystemModule.client.CostCenterService;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CostCenterServiceImpl extends RemoteServiceServlet implements CostCenterService {

	private static final long serialVersionUID = 1L;

	public CostCenter[] getCostCenterList() {
		CostCenter[] result = new CostCenter[10];
		for(int i = 0; i < 10; i++){
			CostCenter instance = new CostCenter();
			instance.id = "sdfds" + i;
			instance.name = "Centro de Custo " + i;
			instance.code = "code" + i;
			instance.members = new User[20];
			for(int j = 0 ; j < 20; j++) {
				User user = new User();
				user.id = "id" + j;
				user.name = "name" + j;
				user.role = new UserRole();
				user.role.id = "roleId";
				user.role.name = "Administrador";
				user.email = "email@email.com" + j;
				user.username = "username" + j;
				instance.members[j] = user;
			}
			result[i] = instance;
		}
		return result;
	}

	public CostCenter getCostCenter(String id) {
		CostCenter instance = new CostCenter();
		instance.id = "sdfds" + id;
		instance.name = "Centro de Custo " + id;
		instance.code = "code" + id;
		instance.members = new User[20];
		for(int j = 0 ; j < 20; j++) {
			User user = new User();
			user.id = "id" + j;
			user.name = "name" + j;
			user.role = new UserRole();
			user.role.id = "roleId";
			user.role.name = "Administrador";
			user.email = "email@email.com" + j;
			user.username = "username" + j;
			instance.members[j] = user;
		}
		return instance;
	}

	public String createCostCenter(CostCenter costCenter) {
		return null;
	}

	public String saveCostCenter(CostCenter costCenter) {
		return null;
	}

	public String deleteCostCenter(String id) {
		return null;
	}
	
	public User[] getAvailableUsersForMembership(String costCenterId) {
		User[] users = new User[20];
		for(int j = 0 ; j < 20; j++) {
			User user = new User();
			user.id = "id" + j;
			user.name = "name" + j;
			user.role = new UserRole();
			user.role.id = "roleId";
			user.role.name = "Administrador";
			user.email = "email@email.com" + j;
			user.username = "username" + j;
			users[j] = user;
		}
		return users;
	}
	
	public String addMembers(String costcenterId, String[] userIds) {
		return "";
	}

	public String removeMember(String costCenterId, String[] memberIds) {
		// TODO Auto-generated method stub
		return "";
	}

}
