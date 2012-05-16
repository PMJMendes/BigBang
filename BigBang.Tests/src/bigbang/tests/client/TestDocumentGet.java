package bigbang.tests.client;

import bigBang.definitions.shared.Document;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDocumentGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Document[]> callback = new AsyncCallback<Document[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Document[] result)
			{
				return;
			}
		};

		Services.documentService.getDocuments("70E43C7F-249C-4127-811E-A05100C9E27B", callback);
	}
}
