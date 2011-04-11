package bigBang.library.client.userInterface;

import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;

public interface MenuSection {
	
	public String getId();
	
	public MenuSections getMenuIndex();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public TextBadge getBadge();
	
	public boolean hasBadge();
	
	public void registerEventHandlers(EventBus eventBus);
	
}
