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

		Services.documentService.getDocuments("41EDAA89-D62C-42E9-8B49-A0B80001F70C", callback);
	}
}
