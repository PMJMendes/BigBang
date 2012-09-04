package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TypifiedText;

public interface TypifiedTextBroker extends DataBrokerInterface<TypifiedText>{


	public int incrementDataVersion(String tag);
	
	public void registerClient(String tag, TypifiedTextClient client);
	
	public void unregisterClient(String tag, TypifiedTextClient client);
	
	public boolean isClientRegistered(String tag, TypifiedTextClient client);
	
	public void getTexts(String tag, ResponseHandler<List<TypifiedText>> handler);
	
	public void getText(String tag, String textId, ResponseHandler<TypifiedText> handler);
	
	public void createText(String tag, TypifiedText text, ResponseHandler<TypifiedText> handler);

	public void removeText(String tag, String textId, ResponseHandler<TypifiedText> handler);
	
	public void requireDataRefresh(String ownerId);
	
	public void updateText(String tag, TypifiedText text, ResponseHandler<TypifiedText> handler);

	public void unregisterClient(TypifiedTextClient client);
}
