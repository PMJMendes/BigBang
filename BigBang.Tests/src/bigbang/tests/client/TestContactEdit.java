package bigbang.tests.client;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
 
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestContactEdit
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

		Services.contactsService.getContacts("57C7551F-B640-495B-8091-9FB700200FB8", callback);
	}

	private static void DoStep2(Contact contact)
	{
		AsyncCallback<Contact> callback = new AsyncCallback<Contact>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Contact result)
			{
				return;
			}
		};

		contact.name = "Teste 2";
		contact.info = new ContactInfo[1];
		contact.info[0] = new ContactInfo();
		contact.info[0].typeId = "60414F28-49E7-43AD-ACD9-9EDF00F41E76";
		contact.info[0].value = "91 234 56 78"; 
 
		Services.contactsService.saveContact(contact, callback);
	}
}
