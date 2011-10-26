package bigBang.client;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.casualtyModule.client.CasualtyModule;
import bigBang.module.clientModule.client.ClientModule;
import bigBang.module.complaintModule.client.ComplaintModule;
import bigBang.module.expenseModule.client.ExpenseModule;
import bigBang.module.generalSystemModule.client.GeneralSystemModule;
import bigBang.module.insurancePolicyModule.client.InsurancePolicyModule;
import bigBang.module.loginModule.client.LoginModule;
import bigBang.module.mainModule.client.MainModule;
import bigBang.module.quoteRequestModule.client.QuoteRequestModule;
import bigBang.module.receiptModule.client.ReceiptModule;
import bigBang.module.riskAnalisysModule.client.RiskAnalisysModule;
import bigBang.module.tasksModule.client.TasksModule;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class BigBang implements EntryPoint {

	public static ModuleManager moduleManager;
	public static EventBus eventBus;
	public static BigBangPermissionManager permissionManager;
	public static DataBrokerManager brokerManager;

	public void onModuleLoad() {
		eventBus = GWT.create(EventBus.class);
		moduleManager = GWT.create(ModuleManager.class);
		brokerManager = DataBrokerManager.Util.getInstance();
		permissionManager = new BigBangPermissionManager();
		
		BigBangAsyncCallback.setEventBus(eventBus);
		
		try {
			/*
			 * Load modules in the following order
			 * 
			 * 1. Main Module
			 * 
			 * 2. Login Module (if exists)
			 * 
			 * 3. Other Modules
			 */

			moduleManager.setEventBus(eventBus);
			moduleManager.setPermissionManager(permissionManager);
			moduleManager.setBrokerManager(brokerManager);
			moduleManager.registerMainModule((MainModule) GWT.create(bigBang.module.mainModule.client.MainModule.class));
			moduleManager.registerLoginModule((LoginModule) GWT.create(bigBang.module.loginModule.client.LoginModule.class));
			
//			moduleManager.registerModule((TasksModule) GWT.create(bigBang.module.tasksModule.client.TasksModule.class));
//			moduleManager.registerModule((GeneralSystemModule) GWT.create(bigBang.module.generalSystemModule.client.GeneralSystemModule.class));
//			moduleManager.registerModule((ClientModule) GWT.create(bigBang.module.clientModule.client.ClientModule.class));
//			moduleManager.registerModule((QuoteRequestModule) GWT.create(bigBang.module.quoteRequestModule.client.QuoteRequestModule.class));			
			moduleManager.registerModule((InsurancePolicyModule) GWT.create(bigBang.module.insurancePolicyModule.client.InsurancePolicyModule.class));
//			moduleManager.registerModule((ReceiptModule) GWT.create(bigBang.module.receiptModule.client.ReceiptModule.class));
//			moduleManager.registerModule((RiskAnalisysModule) GWT.create(bigBang.module.riskAnalisysModule.client.RiskAnalisysModule.class));
//			moduleManager.registerModule((CasualtyModule) GWT.create(bigBang.module.casualtyModule.client.CasualtyModule.class));			
//			moduleManager.registerModule((ExpenseModule) GWT.create(bigBang.module.expenseModule.client.ExpenseModule.class));
//			moduleManager.registerModule((ComplaintModule) GWT.create(bigBang.module.complaintModule.client.ComplaintModule.class));
			
			moduleManager.runMainModule();
		} catch (Exception e) {
			e.printStackTrace();
			GWT.log("Error : " + e.getMessage());
		}

	}

}
