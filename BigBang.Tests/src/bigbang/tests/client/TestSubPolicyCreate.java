package bigbang.tests.client;

import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SubPolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyCreate
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
					if ( result[i].typeId.equalsIgnoreCase("C7BC8D2F-BD61-43D5-9347-9FF300EE9986") )
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

		Services.subPolicyService.openSubPolicyScratchPad(null, callback);
	}

	private static void DoStep2()
	{
		SubPolicy newPolicy;

		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubPolicy result)
			{
				DoStep3(result);
			}
		};

		newPolicy = new SubPolicy();
		newPolicy.id = gstrPad;
		newPolicy.mainPolicyId = "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40";

		Services.subPolicyService.initSubPolicyInPad(newPolicy, callback);
	}

	private static void DoStep3(SubPolicy testPolicy)
	{
		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubPolicy result)
			{
				DoStep4();
			}
		};

		testPolicy.clientId = "5AD8DC0B-613B-4B5C-ACB1-9FB70020137E";
		testPolicy.startDate = "2012-01-01";
		testPolicy.fractioningId = "B8234D73-4432-45A0-B670-9F8101580CB5";

		Services.subPolicyService.updateHeader(testPolicy, callback);
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

		Services.subPolicyService.commitPad(gstrPad, callback);
	}
}
