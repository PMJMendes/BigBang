package bigBang.module.tasksModule.client;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;

public class TasksViewPresenterMapper {

	private static Map<String, String> operationPresenters;

	private static void ensureInitialized(){
		if(operationPresenters == null) {
			operationPresenters = new HashMap<String, String>();
			doMapping();
		}
	}

	public static String getViewPresenterIdForObjectTypeId(String objectTypeId) {
		ensureInitialized();
		String presenterId = operationPresenters.get(objectTypeId.toUpperCase());
		return presenterId;
	}

	private static void doMapping(){
		//CLIENT
		
		//MISC
		map(BigBangConstants.EntityIds.MANAGER_TRANSFER, "MANAGER_TRANSFER");		
		map(BigBangConstants.EntityIds.INSURANCE_POLICY, "INSURANCE_POLICY_TASKS");
		map(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, "INSURANCE_SUB_POLICY_TASKS");
		map(BigBangConstants.EntityIds.DAS_REQUEST, "DAS_REQUEST_TASKS");
		map(BigBangConstants.EntityIds.CONVERSATION, "TASKS_CONVERSATION");
		map(BigBangConstants.EntityIds.NEGOTIATION, "NEGOTIATION_TASKS");
		map(BigBangConstants.EntityIds.RECEIPT, "RECEIPT_TASKS");
		map(BigBangConstants.EntityIds.SIGNATURE_REQUEST, "SIGNATURE_REQUEST_TASKS");
		map(BigBangConstants.EntityIds.CASUALTY, "CASUALTY_TASKS");
		map(BigBangConstants.EntityIds.SUB_CASUALTY, "SUB_CASUALTY_TASKS");
	}
	
	private static void map(String objectTypeId, String presenterId){
		objectTypeId = objectTypeId.toUpperCase();
		presenterId = presenterId.toUpperCase();
		operationPresenters.put(objectTypeId, presenterId);
	}

}
