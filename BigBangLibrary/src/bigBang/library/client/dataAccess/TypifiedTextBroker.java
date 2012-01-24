package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TypifiedText;

public interface TypifiedTextBroker {

	
	public void registerClient(String tag, TypifiedTextClient client);
	
	public void unregisterClient(String tag, TypifiedTextClient client);
	
	public boolean isClientRegistered(String tag, TypifiedTextClient client);
	
	public void refreshListData(String tag);
	
	public List<TypifiedText> getTexts(String tag);
	
	public TypifiedText getText(String tag, String textId);
	
	public void createText(String tag, TypifiedText text, ResponseHandler<TypifiedText> handler);

	public void removeText(String tag, String textId, ResponseHandler<TypifiedText> handler);
	
	public void saveText(String tag, TypifiedText text, ResponseHandler<TypifiedText> handler);

}
