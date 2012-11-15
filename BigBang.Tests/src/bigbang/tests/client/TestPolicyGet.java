package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		InsurancePolicySearchParameter parameter;
		InsurancePolicySortParameter sorts;

		AsyncCallback<NewSearchResult> callback = new AsyncCallback<NewSearchResult>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(NewSearchResult result)
			{
				if ( result.workspaceId != null )
				{
					if ( (result.results != null) && (result.results.length > 0) )
					{
						tmpWorkspace = result.workspaceId;
						DoStep2(result.results[0]);
					}
					else
						DoStep3(result.workspaceId);
				}
				else
					return;
			}
		};

		parameter = new InsurancePolicySearchParameter();
		parameter.freeText = "o";
//		parameter.ownerId = "CAABA817-DA4D-418B-AECE-A0FE00E1B27C";
//		parameter.allowedStates = InsurancePolicySearchParameter.AllowedStates.NONLIVE;
		sorts = new InsurancePolicySortParameter();
		sorts.field = InsurancePolicySortParameter.SortableField.RELEVANCE;
		sorts.order = SortOrder.DESC;

		Services.insurancePolicyService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 50, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.insurancePolicyService.getPolicy(stub.id, callback);
	}

	private static void DoStep3(String workspaceId)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		Services.insurancePolicyService.closeSearch(workspaceId, callback);
	}
}
