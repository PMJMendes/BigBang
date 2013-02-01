package bigBang.client;

import java.util.ArrayList;
import java.util.List;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.MainModule;
import bigBang.library.client.Module;
import bigBang.library.client.dataAccess.DataBrokerManager;

public class ModuleManager {

	private DataBrokerManager brokerManager;

	private static MainModule mainModule;
	private static Module loginModule;
	private List<Module> modules;

	protected int initializedModulesCount = 0;

	public ModuleManager(){
		modules = new ArrayList <Module> ();
		brokerManager = DataBrokerManager.getInstance();
	}

	public void registerMainModule(MainModule module) throws Exception{
		if(isMainModuleInitialized())
			throw new Exception("The main module has already been initialized. It cannot be changed.");
		mainModule = module;

		if(mainModule == null)
			throw new Exception("Cannot initialize main module");
		mainModule.initialize();
	}

	public void registerLoginModule(Module module) throws Exception{
		if(!isMainModuleInitialized())
			throw new Exception("The main module must be initialized before registering any otherprocessModules.");
		loginModule = module;
		loginModule.initialize();
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

		//Registers the broker implementations
		for(int i = 0; i < length; i++){
			Module module = modules.get(i);
			DataBroker<?>[] brokerImplementations = module.getBrokerImplementations();
			for(int j = 0; brokerImplementations != null && j < brokerImplementations.length; j++){
				DataBroker<?> broker = brokerImplementations[j];
				this.brokerManager.registerBrokerImplementation(broker.getDataElementId(), broker);
			}
		}

		//Verifies the broker dependencies
		for(int i = 0; i < length; i++){
			Module module = modules.get(i);
			String[] brokerDependencies = module.getBrokerDependencies();
			for(int j = 0; brokerDependencies != null && j < brokerDependencies.length; j++){
				if(!this.brokerManager.hasBrokerImplementationForDataElement(brokerDependencies[j])) {
					throw new RuntimeException("A broker dependency was not satisfied. The module " + module.getClass().getName() + 
							" requires a broker implementation for the data element with id=\"" + brokerDependencies[j] + "\"");
				}
			}
		}

		for(int i = 0; i < length; i++){
			Module module = modules.get(i);
			module.initialize();
		}
	}

	public void runMainModule() throws Exception {
		initializeModules();
		mainModule.run();
	}

}
