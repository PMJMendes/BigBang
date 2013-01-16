package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.OtherEntity;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("OtherEntityService")
public interface OtherEntityService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static OtherEntityServiceAsync instance;
		public static OtherEntityServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(OtherEntityService.class);
			}
			return instance;
		}
	}

	public OtherEntity[] getOtherEntities() throws SessionExpiredException, BigBangException;
	public OtherEntity createOtherEntity(OtherEntity entity) throws SessionExpiredException, BigBangException;
	public OtherEntity saveOtherEntity(OtherEntity entity) throws SessionExpiredException, BigBangException;
	public void deleteOtherEntity(String id) throws SessionExpiredException, BigBangException;
}
