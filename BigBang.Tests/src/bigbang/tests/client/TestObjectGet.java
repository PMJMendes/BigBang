package bigbang.tests.client;

import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		InsuredObjectSearchParameter parameter;

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

		parameter = new InsuredObjectSearchParameter();
		parameter.policyId = "FBA922E2-E2CE-4351-ABD5-9FBB00CE51B2";

		Services.policyObjectService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.policyObjectService.getObject(stub.id, callback);
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

		Services.policyObjectService.closeSearch(workspaceId, callback);
	}
}
