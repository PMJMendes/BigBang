package bigBang.library.interfaces;

import bigBang.definitions.shared.TypifiedText;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TypifiedTextServiceAsync
	extends Service
{
	void getTexts(String tag, AsyncCallback<TypifiedText[]> callback);
	void createText(TypifiedText text, AsyncCallback<TypifiedText> callback);
	void saveText(TypifiedText text, AsyncCallback<TypifiedText> callback);
	void deleteText(String textId, AsyncCallback<Void> callback);
}
