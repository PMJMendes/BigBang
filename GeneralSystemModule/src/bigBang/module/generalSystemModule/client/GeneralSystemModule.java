package bigBang.module.generalSystemModule.client;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;

public class GeneralSystemModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	
	public void onModuleLoad() {
		
	}

	@Override
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		sectionPresenters = new SectionViewPresenter[1];
		//GeneralSystem section
		GeneralSystemSection generalSystemSection = new GeneralSystemSection(permissionManager);
		GeneralSystemSectionView generalSystemSectionView = new GeneralSystemSectionView();
		GeneralSystemSectionViewPresenter generalSystemSectionPresenter = new GeneralSystemSectionViewPresenter(eventBus, null, generalSystemSectionView);
		generalSystemSectionPresenter.setSection(generalSystemSection);
		generalSystemSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = generalSystemSectionPresenter;
	}
	
	public void initialize(EventBus eventBus) {
		this.initialize(eventBus, null);
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}
}
