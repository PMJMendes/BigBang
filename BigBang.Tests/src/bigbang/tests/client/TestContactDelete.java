package bigbang.tests.client;

import bigBang.definitions.shared.Contact;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestContactDelete
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
				if ( (result == null) || (result.length == 0) )
					return;
				else
					DoStep2(result[result.length - 1]);
			}
		};

		Services.contactsService.getContacts("D7570502-0495-4E18-9CB3-9FB700201363", callback);
	}

	private static void DoStep2(Contact contact)
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

		Services.contactsService.deleteContact(contact.id, callback);
	}
}
