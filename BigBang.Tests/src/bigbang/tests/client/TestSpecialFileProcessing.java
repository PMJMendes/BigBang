package bigbang.tests.client;

import bigBang.definitions.shared.TipifiedListItem;

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

public class TestSpecialFileProcessing
{
	private static String gstrFormatID;
	private static FormPanel gfrmMain;
	private static FileUpload gfupMain;
	private static Button gbtnOk;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<TipifiedListItem[]> callback = new AsyncCallback<TipifiedListItem[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TipifiedListItem[] result)
			{
				gstrFormatID = "408B7ADD-18DB-4D31-A11C-A12700E3001F";
				DoStep2();
			}
		};

		Services.fileService.getListItemsFilter("5514358C-2FCF-4769-981F-3C11BB25BA76", "apolice", callback);
	}

	private static void DoStep2()
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

				DoStep3(lstrResults.split("!", 2)[0]);
			}
		});
	}

	private static void DoStep3(String pstrFileID)
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

		Services.fileService.process(gstrFormatID, pstrFileID, callback);
	}
}
