package bigbang.tests.client;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.casualtyModule.shared.MedicalFileSearchParameter;
import bigBang.module.casualtyModule.shared.MedicalFileSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMedicalFileGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		MedicalFileSearchParameter parameter;
		MedicalFileSortParameter sorts;

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

		parameter = new MedicalFileSearchParameter();
		sorts = new MedicalFileSortParameter();
		sorts.field = MedicalFileSortParameter.SortableField.REFERENCE;
		sorts.order = SortOrder.ASC;

		Services.medicalFileService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<MedicalFile> callback = new AsyncCallback<MedicalFile>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(MedicalFile result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.medicalFileService.getMedicalFile(stub.id, callback);
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

		Services.medicalFileService.closeSearch(workspaceId, callback);
	}
}
