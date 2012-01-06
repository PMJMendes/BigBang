package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Document;

public class TestDocumentCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Document document;

		AsyncCallback<Document> callback = new AsyncCallback<Document>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Document result)
			{
				return;
			}
		};

		document = new Document();
		document.name = "Doc de Teste";
		document.ownerTypeId = "D535A99E-149F-44DC-A28B-9EE600B240F5";
		document.ownerId = "AC94E50E-799F-499D-A4F7-9FB70020135F";
		document.docTypeId = "5E348ABC-0490-4A6B-B303-9FD0011AA4FC";
		document.text = "Isto é só um documento de teste.";

		Services.documentService.createDocument(document, callback);
	}
}
