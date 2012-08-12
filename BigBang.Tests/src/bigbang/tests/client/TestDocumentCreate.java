package bigbang.tests.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

import bigBang.definitions.shared.Document;

public class TestDocumentCreate
{
	private static FormPanel gfrmMain;
	private static FileUpload gfupMain;
	private static Button gbtnOk;
	private static String mstrFile;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		VerticalPanel lvert;

		RootPanel.get().clear();

		lvert = new VerticalPanel();
		RootPanel.get().add(lvert);

		gfrmMain = new FormPanel();
		gfrmMain.setEncoding(FormPanel.ENCODING_MULTIPART);
		gfrmMain.setMethod(FormPanel.METHOD_POST);
		gfrmMain.setAction(GWT.getModuleBaseURL() + "bbfile");
		lvert.add(gfrmMain);

		gfupMain = new FileUpload();
		gfupMain.setName("none");
		gfrmMain.setWidget(gfupMain);

		gbtnOk = new Button();
		gbtnOk.setText("Ok");
		lvert.add(gbtnOk);

		gbtnOk.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
	        {
				gbtnOk.setEnabled(false);
				gfrmMain.submit();
	        }
	    });

		gfrmMain.addSubmitCompleteHandler(new SubmitCompleteHandler()
		{
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				String lstrResults;

				gbtnOk.setEnabled(true);

				lstrResults = event.getResults();
				if ( lstrResults.startsWith("!") )
					return;

				mstrFile = lstrResults.split("!", 2)[0];
				DoStep2();
			}
		});
	}

	private static void DoStep2()
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
				DoStep3();
			}
		};

		document = new Document();
		document.name = "Doc de Teste";
		document.ownerTypeId = "D535A99E-149F-44DC-A28B-9EE600B240F5";
		document.ownerId = "3E6E0EFC-3C35-4648-8000-A0A3012438D5";
		document.docTypeId = "5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A";
		document.fileStorageId = mstrFile;

		Services.documentService.createDocument(document, callback);
	}

	private static void DoStep3()
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

		Services.fileService.Discard(mstrFile, callback);
	}
}
