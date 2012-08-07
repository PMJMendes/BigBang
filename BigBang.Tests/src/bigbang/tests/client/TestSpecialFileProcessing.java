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

public class TestSpecialFileProcessing
{
	private static FormPanel gfrmMain;
	private static FileUpload gfupMain;
	private static Button gbtnOk;

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

				DoStep2(lstrResults.split("!", 2)[0]);
			}
		});
	}

	private static void DoStep2(String pstrFileID)
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

		Services.fileService.process("29631C89-6783-4EBA-A214-A0A600B8AFFA", pstrFileID, callback);
	}
}
