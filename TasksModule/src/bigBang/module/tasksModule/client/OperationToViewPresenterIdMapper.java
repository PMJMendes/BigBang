package bigBang.module.tasksModule.client;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.BigBangConstants;

public class OperationToViewPresenterIdMapper {

	private static Map<String, String> operationPresenters;

	private static void ensureInitialized(){
		if(operationPresenters == null) {
			operationPresenters = new HashMap<String, String>();
			doMapping();
		}
	}

	public static String getViewPresenterIdForOperationId(String operationId) {
		ensureInitialized();
		String presenterId = operationPresenters.get(operationId.toUpperCase());
		return presenterId;
	}

	private static void doMapping(){
		//CLIENT
		
		//MISC
		map(BigBangConstants.OperationIds.ManagerTransfer.CREATE_MANAGER_TRANSFER, "MANAGER_TRANSFER");		
		map(BigBangConstants.OperationIds.ManagerTransfer.CANCEL_MANAGER_TRANSFER, "MANAGER_TRANSFER");
	}
	
	private static void map(String operationId, String presenterId){
		operationId = operationId.toUpperCase();
		presenterId = presenterId.toUpperCase();
		operationPresenters.put(operationId, presenterId);
	}

}
