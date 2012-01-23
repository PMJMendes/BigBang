package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.TypifiedText;

public class TestTextEdit
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		TypifiedText text;

		AsyncCallback<TypifiedText> callback = new AsyncCallback<TypifiedText>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TypifiedText result)
			{
				return;
			}
		};

		text = new TypifiedText();
		text.id = "3DDC3A50-1621-4701-B9EE-9FE10125FA99";
		text.tag = "TEST";
		text.label = "Texto Teste";
		text.subject = "Um Teste";
		text.text = "Isto é só um texto de teste. Se isto fosse um texto a sério, era muito maior.";

		Services.typifiedTextService.saveText(text, callback);
	}
}
