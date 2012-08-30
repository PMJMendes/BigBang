package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.InsurancePolicy.TableSection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyEdit
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "155143AF-1634-4DCC-80DF-A0B800162382";

		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]> ()
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
							if ( lstrPolicy.equalsIgnoreCase(result[i].remapIds[j].oldId) )
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

		Services.insurancePolicyService.openPolicyScratchPad(lstrPolicy, callback);
	}

	private static void DoStep2()
	{
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

		Services.insurancePolicyService.getPolicyInPad(gstrPad, callback);
	}

	private static void DoStep3(InsurancePolicy testPolicy)
	{
		int i, n;

		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				DoStep4(result);
			}
		};

		n = 1;
		if ( testPolicy.headerFields != null )
		{
			for ( i = 0; i < testPolicy.headerFields.length; i++ )
			{
				testPolicy.headerFields[i].value = Integer.toString(n);
				n++;
			}
		}
		if ( testPolicy.extraData != null )
		{
			for ( i = 0; i < testPolicy.extraData.length; i++ )
			{
				testPolicy.extraData[i].value = Integer.toString(n);
				n++;
			}
		}
		if ( testPolicy.coInsurers != null )
		{
			for ( i = 0; i < testPolicy.coInsurers.length; i++ )
			{
				testPolicy.coInsurers[i].percent = 50.0 - testPolicy.coInsurers[i].percent;
			}
		}

		Services.insurancePolicyService.updateHeader(testPolicy, callback);
	}

	private static void DoStep4(InsurancePolicy testPolicy)
	{
		int i, j, n;

		AsyncCallback<TableSection> callback = new AsyncCallback<TableSection>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TableSection result)
			{
				DoStep5();
			}
		};

		if ( (testPolicy.tableData == null) || (testPolicy.tableData.length < 1) )
			DoStep5();
		else
		{
			n = 11;
			if ( testPolicy.tableData != null )
			{
				for ( i = 0; i < testPolicy.tableData.length; i++ )
				{
					if ( testPolicy.tableData[i].data != null )
					{
						for ( j = 0; j < testPolicy.tableData[0].data.length; j++ )
						{
							testPolicy.tableData[i].data[j].value = Integer.toString(n);
							n++;
						}
					}
				}
			}

			Services.insurancePolicyService.savePage(testPolicy.tableData[0], callback);
		}
	}

	private static void DoStep5()
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
