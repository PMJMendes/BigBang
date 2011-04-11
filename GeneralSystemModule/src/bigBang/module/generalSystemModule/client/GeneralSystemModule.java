package bigBang.module.generalSystemModule.client;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;
import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;

public class GeneralSystemModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	private boolean initialized = false;

	@Override
	public void initialize(final EventBus eventBus, final BigBangPermissionManager permissionManager) {
		sectionPresenters = new SectionViewPresenter[1];
		
		GeneralSystemService.Util.getInstance().getGeneralSystemProcessId(new BigBangAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				permissionManager.getProcessPermissionContext(result, new BigBangAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						setup(eventBus, permissionManager);
					}
				});	
			}
		});
	}

	private void setup(EventBus eventBus, BigBangPermissionManager permissionManager) {
		//GeneralSystem section
		GeneralSystemSection generalSystemSection = new GeneralSystemSection(permissionManager);
		GeneralSystemSectionView generalSystemSectionView = new GeneralSystemSectionView();
		GeneralSystemSectionViewPresenter generalSystemSectionPresenter = new GeneralSystemSectionViewPresenter(eventBus, null, generalSystemSectionView);
		generalSystemSectionPresenter.setSection(generalSystemSection);
		generalSystemSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = generalSystemSectionPresenter;
		this.initialized = true;
		eventBus.fireEvent(new ModuleInitializedEvent(this));
	}
	
	public void initialize(EventBus eventBus) {
		this.initialize(eventBus, null);
	}

	public boolean isInitialized() {
		return initialized;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}
}
