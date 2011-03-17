package bigBang.library.shared.userInterface;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.view.View;

public interface MenuSection {
	
	public String getId();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public TextBadge getBadge();
	
	public boolean hasBadge();
	
	public void registerEventHandlers(EventBus eventBus);
	
}
