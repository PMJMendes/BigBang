package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectEdit
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
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				DoStep5(result);
			}
		};

		Services.insurancePolicyService.getObjectInPad(tempObjectId, callback);
	}

	private static void DoStep5(InsuredObject object)
	{
		int i, j, k, n;

		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				DoStep6();
			}
		};

		object.unitIdentification = "Teste";

		n = 101;
		for ( i = 0; i < object.headerData.fixedFields.length; i++ )
		{
			object.headerData.fixedFields[i].value = Integer.toString(n);
			n++;
		}
		for ( i = 0; i < object.headerData.variableFields.length; i++ )
		{
			for ( j = 0; j < object.headerData.variableFields[i].data.length; j++ )
			{
				object.headerData.variableFields[i].data[j].value = Integer.toString(n);
				n++;
			}
		}
		for ( i = 0; i < object.coverageData.length; i++ )
		{
			for ( j = 0; j < object.coverageData[i].fixedFields.length; j++ )
			{
				object.coverageData[i].fixedFields[j].value = Integer.toString(n);
				n++;
			}
			for ( j = 0; j < object.coverageData[i].variableFields.length; j++ )
			{
				for ( k = 0; k < object.coverageData[i].variableFields[j].data.length; k++ )
				{
					object.coverageData[i].variableFields[j].data[k].value = Integer.toString(n);
					n++;
				}
			}
		}

		Services.insurancePolicyService.updateObjectInPad(object, callback);
	}

	private static void DoStep6()
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
