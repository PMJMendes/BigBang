package bigBang.library.client;

import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.interfaces.BigBangPermissionServiceAsync;

public class BigBangPermissionManager {

	private BigBangPermissionServiceAsync service;
	private String[] operationPermissions;
	private String currentProcess;
	
	public BigBangPermissionManager(){
		service = BigBangPermissionService.Util.getInstance();
	}
	
	public void getProcessPermissionContext(String processId, final BigBangAsyncCallback<Void> callback){
		currentProcess = processId;
		service.getProcessPermissions(processId, new BigBangAsyncCallback<String[]>() {

			@Override
			public void onSuccess(String[] result) {
				operationPermissions = result;
				callback.onSuccess(null);
			}
		});
	}
	
	public boolean hasContext(){
		return operationPermissions != null;
	}
	
	public String getProcessContextId(){
		return currentProcess;
	}
	
	public void clearContext(){
		operationPermissions = null;
		currentProcess = null;
	}
	
	public boolean hasPermissionForOperation(String operationId) {
		for(int i = 0; i < operationPermissions.length; i++) {
			if(operationPermissions[i].equals(operationId))
				return true;
		}
		return false;
	}

}
