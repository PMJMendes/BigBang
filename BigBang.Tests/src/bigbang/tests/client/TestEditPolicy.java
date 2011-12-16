package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestEditPolicy
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

		Services.insurancePolicyService.getPolicy("FBA922E2-E2CE-4351-ABD5-9FBB00CE51B2", callback);
	}

	private static void DoStep2(InsurancePolicy testPolicy)
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

		Services.insurancePolicyService.openForEdit(testPolicy, callback);
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
				DoStep5(tmpPolicy);
			}
		};

		if ( (testPolicy.tableData == null) || (testPolicy.tableData.length < 1) )
			DoStep5(testPolicy);
		else
		{
			tmpPolicy = testPolicy;

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

	private static void DoStep5(InsurancePolicy testPolicy)
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

		Services.insurancePolicyService.commitPolicy(testPolicy.scratchPadId, callback);
	}
}
