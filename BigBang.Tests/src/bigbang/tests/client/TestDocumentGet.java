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

		Services.documentService.getDocuments("AC94E50E-799F-499D-A4F7-9FB70020135F", callback);
	}
}
