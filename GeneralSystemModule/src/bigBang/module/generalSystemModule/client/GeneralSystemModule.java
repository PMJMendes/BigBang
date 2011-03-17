package bigBang.module.generalSystemModule.client;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Module;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;

public class GeneralSystemModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	
	public void onModuleLoad() {
		
	}

	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];
		//GeneralSystem section
		GeneralSystemSection generalSystemSection = new GeneralSystemSection();
		GeneralSystemSectionView generalSystemSectionView = new GeneralSystemSectionView();
		GeneralSystemSectionViewPresenter generalSystemSectionPresenter = new GeneralSystemSectionViewPresenter(eventBus, null, generalSystemSectionView);
		generalSystemSectionPresenter.setSection(generalSystemSection);
		generalSystemSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = generalSystemSectionPresenter;
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}
}
