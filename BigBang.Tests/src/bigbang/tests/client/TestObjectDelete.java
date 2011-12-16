package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectDelete
{
	private static InsurancePolicy tmpPolicy;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(InsurancePolicy result)
			{
				DoStep2(result);
			}
		};
	
		Services.insurancePolicyService.getPolicy("0B0C69A5-FA4E-4A7D-B625-9FB2015D29D6", callback);
	}

	private static void DoStep2(InsurancePolicy policy)
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(InsurancePolicy result)
			{
				tmpPolicy = result;
				DoStep3(result);
			}
		};
	
		Services.insurancePolicyService.openForEdit(policy, callback);
	}

	private static void DoStep3(InsurancePolicy policy)
	{
		AsyncCallback<TipifiedListItem[]> callback = new AsyncCallback<TipifiedListItem[]> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(TipifiedListItem[] result)
			{
				if ( (result != null) && (result.length > 0) )
					DoStep4(result[0].id);
				else
					return;
			}
		};

		Services.insurancePolicyService.getPadItemsFilter("3A3316D2-9D7C-4FD1-8486-9F9C0012E119", policy.scratchPadId, callback);
	}

	private static void DoStep4(String tempObjectId)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				DoStep5();
			}
		};

		Services.insurancePolicyService.deleteObjectInPad(tempObjectId, callback);
	}

	private static void DoStep5()
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				return;
			}
		};

		Services.insurancePolicyService.commitPolicy(tmpPolicy.scratchPadId, callback);
	}
}
