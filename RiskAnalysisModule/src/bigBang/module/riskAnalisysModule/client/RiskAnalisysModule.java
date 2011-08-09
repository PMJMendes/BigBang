package bigBang.module.riskAnalisysModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSectionViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysSectionView;

public class RiskAnalisysModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;
	
	public RiskAnalisysModule(){
	}
	
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus);
	}
	
	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Risk analisys section
		RiskAnalisysSection riskAnalisysSection = new RiskAnalisysSection(this.permissionManager);
		RiskAnalisysSectionView riskAnalisysSectionView = new RiskAnalisysSectionView();
		RiskAnalisysSectionViewPresenter riskAnalisysSectionViewPresenter = new RiskAnalisysSectionViewPresenter(eventBus, null, riskAnalisysSectionView);
		riskAnalisysSectionViewPresenter.setSection(riskAnalisysSection);
		riskAnalisysSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = riskAnalisysSectionViewPresenter;
		
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
