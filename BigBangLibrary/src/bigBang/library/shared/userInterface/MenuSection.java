package bigBang.library.shared.userInterface;

import bigBang.library.shared.EventBus;

public interface MenuSection {
	
	public String getId();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public TextBadge getBadge();
	
	public boolean hasBadge();
	
	public void registerEventHandlers(EventBus eventBus);
	
}
