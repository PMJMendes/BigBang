package bigBang.library.client;

import bigBang.definitions.shared.ProcessBase;
import bigBang.definitions.shared.Permission;

public class PermissionChecker {

	public static boolean hasPermission(ProcessBase process, String permissionId){
		Permission[] permissions = process.permissions;
		if(permissions == null || permissionId == null){
			return false;
		}
		for(int i = 0; i < permissions.length; i++) {
			String instanceId = permissions[i].getOperationInstanceId();
			String id = permissions[i].getOperationTypeId();
			if(id != null && id.equalsIgnoreCase(permissionId) && instanceId != null){
				return true;
			}
		}
		return false;
	}
	
}
