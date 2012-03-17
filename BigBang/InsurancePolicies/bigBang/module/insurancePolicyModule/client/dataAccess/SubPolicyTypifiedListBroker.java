package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;

public class SubPolicyTypifiedListBroker extends BigBangTypifiedListBroker {

	public static class Util {
		private static SubPolicyTypifiedListBroker instance;
		
		public static SubPolicyTypifiedListBroker getInstance(){
			if(instance == null) {
				instance = new SubPolicyTypifiedListBroker();
			}
			return instance;
		}
	}
	
	public SubPolicyTypifiedListBroker(){
		super();
		service = SubPolicyService.Util.getInstance();
	}

}
