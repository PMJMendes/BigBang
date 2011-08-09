package bigBang.module.casualtyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySectionView;

public class CasualtyModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;

	public CasualtyModule(){
	}

	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus); 
	}

	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Casualty section
		CasualtySection casualtySection = new CasualtySection(this.permissionManager);
		CasualtySectionView casualtySectionView = new CasualtySectionView();
		CasualtySectionViewPresenter casualtySectionViewPresenter = new CasualtySectionViewPresenter(eventBus, null, casualtySectionView);
		casualtySectionViewPresenter.setSection(casualtySection);
		casualtySection.registerEventHandlers(eventBus);
		sectionPresenters[0] = casualtySectionViewPresenter;

		eventBus.fireEvent(new ModuleInitializedEvent(this));
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}

	public Process[] getProcesses() {
		return null;
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getBrokerDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

}
