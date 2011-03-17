package bigBang.library.shared.userInterface.presenter;

import bigBang.library.shared.userInterface.MenuSection;

public interface SectionViewPresenter extends ViewPresenter {
	
	public MenuSection getSection();
	
	public void setSection(MenuSection section);
	
}
