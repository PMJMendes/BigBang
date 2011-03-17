package bigBang.library.shared;

import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;

public interface MainModule extends Module {
	
	public void run();

	public void setLoginPresenter(ViewPresenter loginViewPresenter);

	void includeMainMenuSectionPresenters(
			SectionViewPresenter[] mainMenuSectionPresenters);

	EventBus getEventBus();
}
