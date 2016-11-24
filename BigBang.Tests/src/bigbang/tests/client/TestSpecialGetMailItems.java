package bigbang.tests.client;

import bigBang.library.shared.MailItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetMailItems
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<MailItemStub[]> callback = new AsyncCallback<MailItemStub[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(MailItemStub[] result)
			{
				DoStep2();
			}
		};

		Services.mailService.getItems(callback);
	}

	private static void DoStep2()
	{
//		AsyncCallback<ExchangeItemStub[]> callback = new AsyncCallback<ExchangeItemStub[]>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(ExchangeItemStub[] result)
//			{
//				return;
//			}
//		};
//
//		Services.exchangeService.getItemsAll(callback);
	}
}
