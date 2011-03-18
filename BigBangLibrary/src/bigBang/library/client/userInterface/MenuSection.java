package bigBang.library.client.userInterface;

import bigBang.library.client.EventBus;

public interface MenuSection {
	
	public String getId();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public TextBadge getBadge();
	
	public boolean hasBadge();
	
	public void registerEventHandlers(EventBus eventBus);
	
}
