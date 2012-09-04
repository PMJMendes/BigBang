package bigBang.library.client;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ProcessBase;
import bigBang.definitions.shared.Permission;
import bigBang.library.interfaces.BigBangPermissionService;

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

	public static void hasGeneralPermission(String objectTypeId, final String permissionId, final ResponseHandler<Boolean> handler){
		BigBangPermissionService.Util.getInstance().getGeneralOpPermissions(objectTypeId, new BigBangAsyncCallback<Permission[]>(){

			@Override
			public void onResponseSuccess(Permission[] permissions) {
				if(permissions == null || permissionId == null){
					handler.onResponse(false);
				}else{
					for(int i = 0; i < permissions.length; i++) {
						String operationTypeId = permissions[i].getOperationTypeId();
						if(operationTypeId != null && operationTypeId.equalsIgnoreCase(permissionId)){
							handler.onResponse(true);
							return;
						}
					}
					handler.onResponse(false);
				}
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the process type permissions")
				});
			}

		});
	}

}
