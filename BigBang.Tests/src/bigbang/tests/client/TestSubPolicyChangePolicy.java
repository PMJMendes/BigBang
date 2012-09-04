package bigbang.tests.client;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyChangePolicy
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		SubPolicySearchParameter parameter;

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

		parameter = new SubPolicySearchParameter();
		parameter.freeText = "-2475.1.1";

		Services.subPolicyService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubPolicy result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.subPolicyService.transferToPolicy(stub.id, "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40", callback);
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

		Services.subPolicyService.closeSearch(workspaceId, callback);
	}
}
