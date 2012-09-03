package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.Policy2;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Policy2Service")
public interface Policy2Service
	extends RemoteService
{
	public static class Util
	{
		private static Policy2ServiceAsync instance;
		public static Policy2ServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(Policy2Service.class);

			return instance;
		}
	}

	public Policy2 getEmptyPolicy(String subLineId) throws SessionExpiredException, BigBangException;
	public Object2 getEmptyObject(String subLineId) throws SessionExpiredException, BigBangException;

	public Policy2 getPolicy(String policyId) throws SessionExpiredException, BigBangException;
	public Object2 getPolicyObject(String objectId) throws  SessionExpiredException, BigBangException;

	public Policy2 editPolicy(Policy2 policy) throws  SessionExpiredException, BigBangException;
}
