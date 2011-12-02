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

		Services.insurancePolicyService.getPolicy("F4D6391A-CBB3-4555-BCB1-9FA900BA4838", callback);
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
		int i, j, n;

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

		n = 0;
		if ( testPolicy.headerFields != null )
		{
			for ( i = 0; i < testPolicy.headerFields.length; i++ )
			{
				if ( (testPolicy.headerFields[i].type == InsurancePolicy.FieldType.NUMERIC) ||
						(testPolicy.headerFields[i].type == InsurancePolicy.FieldType.TEXT) )
				{
					testPolicy.headerFields[i].value = Integer.toString(n);
					n++;
				}
			}
		}
		if ( testPolicy.tableData != null )
		{
			for ( i = 0; i < testPolicy.tableData.length; i++ )
			{
				if ( testPolicy.tableData[i].data != null )
				{
					for ( j = 0; j < testPolicy.tableData[0].data.length; j++ )
					{
						if ( (testPolicy.columns[testPolicy.tableData[i].data[j].columnIndex].type ==
								InsurancePolicy.FieldType.NUMERIC) ||
								(testPolicy.columns[testPolicy.tableData[i].data[j].columnIndex].type ==
								InsurancePolicy.FieldType.TEXT) )
						{
							testPolicy.tableData[i].data[j].value = Integer.toString(n);
							n++;
						}
					}
				}
			}
		}
		if ( testPolicy.extraData != null )
		{
			for ( i = 0; i < testPolicy.extraData.length; i++ )
			{
				if ( (testPolicy.extraData[i].type == InsurancePolicy.FieldType.NUMERIC) ||
						(testPolicy.extraData[i].type == InsurancePolicy.FieldType.TEXT) )
				{
					testPolicy.extraData[i].value = Integer.toString(n);
					n++;
				}
			}
		}

		Services.insurancePolicyService.updateHeader(testPolicy, callback);
	}

	private static void DoStep4(InsurancePolicy testPolicy)
	{
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
