package bigBang.library.client;

import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public interface MainModule extends Module {
	
	public void run();

	public void setLoginPresenter(ViewPresenter loginViewPresenter);

	void includeMainMenuSectionPresenters(
			SectionViewPresenter[] mainMenuSectionPresenters);

	EventBus getEventBus();
}
