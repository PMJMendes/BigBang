package bigBang.library.client;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.interfaces.BigBangPermissionServiceAsync;
import bigBang.library.shared.Permission;

public class BigBangPermissionManager {
	
	protected Map<String, Permission[]> contexts;
	protected BigBangPermissionServiceAsync service;
	
	public static class Util {
		protected static BigBangPermissionManager instance;
		public static BigBangPermissionManager getInstance() {
			if(instance == null){
				instance = new BigBangPermissionManager();
			}
			return instance;
		}
	}
	
	public BigBangPermissionManager(){
		service = BigBangPermissionService.Util.getInstance();
		this.contexts = new HashMap<String, Permission[]>();
	}
	
	public void getProcessPermissionContext(final String processId, final ResponseHandler<Void> handler){
		service.getProcessPermissions(processId, new BigBangAsyncCallback<Permission[]>() {

			@Override
			public void onSuccess(Permission[] result) {
				contexts.put(processId, result);
				handler.onResponse(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onResponse(null); //TODO FJVC
			}
		});
	}
	
	public void clearContext(String processId){
		this.contexts.remove(processId);
	}
	
	public boolean hasPermissionForOperation(String processId, String operationId) {
		return true; //TODO FJVC
		/*Permission[] permissions = contexts.get(processId);
		for(Permission p : permissions) {
			if(operationId.equalsIgnoreCase(p.id)){
				return true;
			}
		}
		return false;*/
	}

}
