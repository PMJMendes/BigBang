package bigbang.tests.client;

import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicy.TableSection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyEdit
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrSubPolicy = "2238CC33-CBBB-4FC1-A7E8-9FFA011E42DD";

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
					if ( result[i].typeId.equalsIgnoreCase("C7BC8D2F-BD61-43D5-9347-9FF300EE9986") )
					{
						for ( j = 0; j < result[i].remapIds.length; j++ )
						{
							if ( lstrSubPolicy.equalsIgnoreCase(result[i].remapIds[j].oldId) )
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

		Services.subPolicyService.openSubPolicyScratchPad(lstrSubPolicy, callback);
	}

	private static void DoStep2()
	{
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

		Services.subPolicyService.getSubPolicyInPad(gstrPad, callback);
	}

	private static void DoStep3(SubPolicy testPolicy)
	{
		int i, n;

		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubPolicy result)
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

		Services.subPolicyService.updateHeader(testPolicy, callback);
	}

	private static void DoStep4(SubPolicy testPolicy)
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

			Services.subPolicyService.savePage(testPolicy.tableData[0], callback);
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

		Services.subPolicyService.commitPad(gstrPad, callback);
	}
}
