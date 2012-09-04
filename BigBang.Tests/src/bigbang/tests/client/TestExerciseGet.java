package bigbang.tests.client;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExerciseGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExerciseSearchParameter parameter;

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

		parameter = new ExerciseSearchParameter();
		parameter.policyId = "54FF52B3-076D-4E3E-B825-9FD000C6AF77";

		Services.policyExerciseService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.policyExerciseService.getExercise(stub.id, callback);
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

		Services.policyExerciseService.closeSearch(workspaceId, callback);
	}
}
