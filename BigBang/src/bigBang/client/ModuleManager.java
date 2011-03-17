package bigBang.client;

import java.util.ArrayList;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.LoginModule;
import bigBang.library.shared.MainModule;
import bigBang.library.shared.Module;
import bigBang.library.shared.ProcessModule;

public class ModuleManager {

	private EventBus eventBus;
	private static MainModule mainModule;
	private static LoginModule loginModule;
	private ArrayList <Module> modules;

	public ModuleManager(){
		modules = new ArrayList <Module> ();
	}

	public void registerMainModule(MainModule module) throws Exception{
		if(isMainModuleInitialized())
			throw new Exception("The main module has already been initialized. It cannot be changed.");
		mainModule = module;
		
		if(mainModule == null)
			throw new Exception("Cannot initialize main module");

		mainModule.initialize(this.eventBus);
	}

	public void registerLoginModule(LoginModule module) throws Exception{
		if(!isMainModuleInitialized())
			throw new Exception("The main module must be initialized before registering any otherprocessModules.");
		loginModule = module;
		mainModule.setLoginPresenter(loginModule.getLoginViewPresenter());
	}
	
	public void registerModule(Module module) throws Exception {
		if(!isMainModuleInitialized())
			throw new Exception("The main module must be initialized before registering any otherprocessModules.");

		if(modules.contains(module))
			return;

		modules.add(module);
	}

	public boolean isRegistered(Module module){
		return module == mainModule || module == loginModule || modules.contains(module);
	}

	private boolean isMainModuleInitialized(){
		return mainModule != null && mainModule.isInitialized();
	}
	
	public void initializeModules() {
		int length = modules.size();
		for(int i = 0; i < length; i++){
			Module module = modules.get(i);
			module.initialize(eventBus);
			mainModule.includeMainMenuSectionPresenters(module.getMainMenuSectionPresenters());
		}
	}

	public void runMainModule() throws Exception {
		//if(isMainModuleInitialized())
		//	throw new Exception("The main module has already been initialized. It cannot be run.");
		mainModule.run();
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
