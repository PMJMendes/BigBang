package bigBang.library.client.userInterface.presenter;

import bigBang.library.client.userInterface.MenuSection;

public interface SectionViewPresenter extends ViewPresenter {
	
	public MenuSection getSection();
	
	public void setSection(MenuSection section);
	
}
