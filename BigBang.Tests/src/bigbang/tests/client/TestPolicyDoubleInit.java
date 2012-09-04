package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyDoubleInit
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Remap[] result)
			{
				int i, j;

				gstrPad = null;
				for (i = 0; i < result.length; i++ )
				{
					if ( result[i].typeId.equalsIgnoreCase("D0C5AE6B-D340-4171-B7A3-9F81011F5D42") )
					{
						for ( j = 0; j < result[i].remapIds.length; j++ )
						{
							if ( result[i].remapIds[j].oldId == null )
							{
								gstrPad = result[i].remapIds[j].newId;
								break;
							}
						}
						break;
					}
				}

				if ( gstrPad == null )
					return;

				DoStep2();
			}
		};

		Services.insurancePolicyService.openPolicyScratchPad(null, callback);
	}

	private static void DoStep2()
	{
		InsurancePolicy newPolicy;

		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				DoStep3(result);
			}
		};

		newPolicy = new InsurancePolicy();
		newPolicy.id = gstrPad;
		newPolicy.subLineId = "FE18F5ED-F87C-4595-B9AC-9EE90118D568";

		Services.insurancePolicyService.initPolicyInPad(newPolicy, callback);
	}

	private static void DoStep3(InsurancePolicy testPolicy)
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				DoStep4();
			}
		};

		testPolicy.subLineId = "34E19434-6106-4359-93FE-9EE90118CEE0";

		Services.insurancePolicyService.initPolicyInPad(testPolicy, callback);
	}

	private static void DoStep4()
	{
		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Remap[] result)
			{
				return;
			}
		};

		Services.insurancePolicyService.discardPad(gstrPad, callback);
	}
}
