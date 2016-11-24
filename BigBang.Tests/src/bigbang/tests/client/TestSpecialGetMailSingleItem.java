package bigbang.tests.client;

import bigBang.library.shared.MailItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetMailSingleItem
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<MailItem> callback = new AsyncCallback<MailItem>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(MailItem result)
			{
				return;
			}
		};

		Services.mailService.getItem("AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAQVuGRAAA=",
				null, callback);
	}
}
