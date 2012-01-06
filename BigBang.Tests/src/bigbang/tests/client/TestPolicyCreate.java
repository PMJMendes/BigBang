package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyCreate
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "FBA922E2-E2CE-4351-ABD5-9FBB00CE51B2";

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
					if ( result[i].typeId.equals("D0C5AE6B-D340-4171-B7A3-9F81011F5D42") )
					{
						for ( j = 0; j < result[i].remapIds.length; j++ )
						{
							if ( lstrPolicy.equals(result[i].remapIds[j].oldId) )
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
		newPolicy.subLineId = "34E19434-6106-4359-93FE-9EE90118CEE0";

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

		testPolicy.clientId = "5AD8DC0B-613B-4B5C-ACB1-9FB70020137E";
		testPolicy.caseStudy = false;
		testPolicy.insuranceAgencyId = "F1EA00FA-36C5-44CB-B1EB-9FB700200FB8";
		testPolicy.startDate = "2012-01-01";
		testPolicy.durationId = "FFF15F7F-EB59-40D4-8E86-9F810157FD24";
		testPolicy.fractioningId = "B8234D73-4432-45A0-B670-9F8101580CB5";
		testPolicy.maturityDay = 1;
		testPolicy.maturityMonth = 1;

		Services.insurancePolicyService.updateHeader(testPolicy, callback);
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

		Services.insurancePolicyService.commitPad(gstrPad, callback);
	}
}
