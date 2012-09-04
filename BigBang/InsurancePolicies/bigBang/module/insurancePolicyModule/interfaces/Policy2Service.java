package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Policy2Service")
public interface Policy2Service
	extends InsurancePolicyService
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

	public InsurancePolicy getEmptyPolicy(String subLineId) throws SessionExpiredException, BigBangException;
	public Object2 getEmptyObject(String subLineId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy getPolicy2(String policyId) throws SessionExpiredException, BigBangException;
	public Object2 getPolicyObject(String objectId) throws  SessionExpiredException, BigBangException;

	public InsurancePolicy editPolicy(InsurancePolicy policy) throws  SessionExpiredException, BigBangException;
}
