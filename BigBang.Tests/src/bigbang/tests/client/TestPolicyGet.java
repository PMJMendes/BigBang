package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
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
						DoStep4(result.workspaceId);
				}
				else
					return;
			}
		};

		parameter = new InsurancePolicySearchParameter();
		parameter.subLineId = "22FE8580-E680-4EC7-9ABB-9EE9011AA269";
//		parameter.freeText = "12982";
//		parameter.insuredObject = "31-54-QT";
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
				DoStep3();
			}
		};

		Services.insurancePolicyService.getPolicy(stub.id, callback);
	}

	private static void DoStep3()
	{
		AsyncCallback<TableSection> callback = new AsyncCallback<TableSection>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TableSection result)
			{
				DoStep4(tmpWorkspace);
			}
		};

		Services.insurancePolicyService.getPage("87c1b24d-daa1-42bc-864a-a058003c4dba",
				"070a6adc-48bc-428c-a785-a058003ca488", null, callback);
	}

	private static void DoStep4(String workspaceId)
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
