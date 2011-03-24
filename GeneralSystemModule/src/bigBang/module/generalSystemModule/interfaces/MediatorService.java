package bigBang.module.generalSystemModule.interfaces;

import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MediatorService")
public interface MediatorService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MediatorServiceAsync instance;
		public static MediatorServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(MediatorService.class);
			}
			return instance;
		}
	}
	
	public Mediator[] getMediators();
	
	public Mediator getMediator(String id);
	
	public String saveMediator(Mediator mediator);
	
	public Mediator createMediator(Mediator mediator);
	
	public String deleteMediator(String id);
	
	public ComissionProfile[] getComissionProfiles();
	
}
