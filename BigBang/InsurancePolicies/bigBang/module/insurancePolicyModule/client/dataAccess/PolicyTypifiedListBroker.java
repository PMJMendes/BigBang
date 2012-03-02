package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;

public class PolicyTypifiedListBroker extends BigBangTypifiedListBroker {

	public static class Util {
		private static PolicyTypifiedListBroker instance;
		
		public static PolicyTypifiedListBroker getInstance(){
			if(instance == null) {
				instance = new PolicyTypifiedListBroker();
			}
			return instance;
		}
	}
	
	public PolicyTypifiedListBroker(){
		super();
		service = InsurancePolicyService.Util.getInstance();
	}

}
