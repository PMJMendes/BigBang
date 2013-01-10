package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.ConversationSearchParameter;
import bigBang.library.shared.ConversationSortParameter;
import bigBang.library.shared.NewSearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ConversationSearchParameter parameter;
		ConversationSortParameter sorts;

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

		parameter = new ConversationSearchParameter();
		parameter.ownerId = "CB0A6B61-6589-4BA5-8120-A0FE01077042";
		sorts = new ConversationSortParameter();
		sorts.field = ConversationSortParameter.SortableField.SUBJECT;
		sorts.order = SortOrder.ASC;

		Services.conversationService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Conversation> callback = new AsyncCallback<Conversation>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Conversation result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.conversationService.getConversation(stub.id, callback);
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

		Services.conversationService.closeSearch(workspaceId, callback);
	}
}
