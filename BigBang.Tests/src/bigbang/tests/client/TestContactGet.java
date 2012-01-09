package bigbang.tests.client;

import bigBang.definitions.shared.Contact;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestContactGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Contact[]> callback = new AsyncCallback<Contact[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Contact[] result)
			{
				return;
			}
		};

		Services.contactsService.getContacts("57C7551F-B640-495B-8091-9FB700200FB8", callback);
	}
}
