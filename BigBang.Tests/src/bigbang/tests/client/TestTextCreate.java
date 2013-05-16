package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.TypifiedText;

public class TestTextCreate
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
		text.tag = "TEST";
		text.label = "Texto de Teste";
		text.subject = "Teste";
		text.text = "Isto é só um texto de teste. Se isto fosse um texto a sério, era maior.";

		Services.typifiedTextService.createText(text, callback);
	}
}
