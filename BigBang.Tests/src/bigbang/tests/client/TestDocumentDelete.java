package bigbang.tests.client;

import bigBang.definitions.shared.Document;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDocumentDelete
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
				if ( (result == null) || (result.length == 0) )
					return;
				else
					DoStep2(result[result.length - 1]);
			}
		};

		Services.documentService.getDocuments("AC94E50E-799F-499D-A4F7-9FB70020135F", callback);
	}

	private static void DoStep2(Document doc)
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

		Services.documentService.deleteDocument(doc.id, callback);
	}
}
