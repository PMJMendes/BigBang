package bigBang.client;

import bigBang.library.shared.EventBus;
import bigBang.module.loginModule.client.LoginModule;
import bigBang.module.mainModule.client.MainModule;
import bigBang.module.tasksModule.client.TasksModule;
import bigBang.module.clientModule.client.ClientModule;
import bigBang.module.generalSystemModule.client.GeneralSystemModule;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class BigBang implements EntryPoint {

	public static ModuleManager moduleManager;
	public static EventBus eventBus;

	public void onModuleLoad() {
		eventBus = GWT.create(EventBus.class);
		moduleManager = GWT.create(ModuleManager.class);

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
			moduleManager.registerMainModule((MainModule) GWT.create(bigBang.module.mainModule.client.MainModule.class));
			moduleManager.registerLoginModule((LoginModule) GWT.create(bigBang.module.loginModule.client.LoginModule.class));
			
			moduleManager.registerModule((TasksModule) GWT.create(bigBang.module.tasksModule.client.TasksModule.class));
			moduleManager.registerModule((GeneralSystemModule) GWT.create(bigBang.module.generalSystemModule.client.GeneralSystemModule.class));
			moduleManager.registerModule((ClientModule) GWT.create(bigBang.module.clientModule.client.ClientModule.class));
			
			moduleManager.initializeModules();
			moduleManager.runMainModule();
		} catch (Exception e) {
			e.printStackTrace();
			GWT.log("Error : " + e.getMessage());
		}

	}

}
