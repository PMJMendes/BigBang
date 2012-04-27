package bigbang.tests.client;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestContactCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Contact contact;

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

		contact = new Contact();
		contact.ownerTypeId = "EFFA56DF-8F3C-4361-A584-A02E00C4F0C5";
		contact.ownerId = "CA38CF67-FB1C-4213-9ABE-A03C011459A1";
		contact.name = "Teste";
		contact.typeId = "04F6BC3C-0283-47F0-9670-9EEE013350D9";
		contact.info = new ContactInfo[1];
		contact.info[0] = new ContactInfo();
		contact.info[0].typeId = "96467849-6FE1-4113-928C-9EDF00F40FB9";
		contact.info[0].value = "joao.mendes@premium.minds.com";
		Services.contactsService.createContact(contact, callback);
	}
}
