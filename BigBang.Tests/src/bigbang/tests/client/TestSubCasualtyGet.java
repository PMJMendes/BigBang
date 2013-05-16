package bigbang.tests.client;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		SubCasualtySearchParameter parameter;
		SubCasualtySortParameter sorts;

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

		parameter = new SubCasualtySearchParameter();
		parameter.freeText = "20122318";
//		parameter.ownerId = "CA38CF67-FB1C-4213-9ABE-A03C011459A1";
		sorts = new SubCasualtySortParameter();
		sorts.field = SubCasualtySortParameter.SortableField.NUMBER;
		sorts.order = SortOrder.ASC;

		Services.subCasualtyService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<SubCasualty> callback = new AsyncCallback<SubCasualty>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubCasualty result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.subCasualtyService.getSubCasualty(stub.id, callback);
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

		Services.subCasualtyService.closeSearch(workspaceId, callback);
	}
}
