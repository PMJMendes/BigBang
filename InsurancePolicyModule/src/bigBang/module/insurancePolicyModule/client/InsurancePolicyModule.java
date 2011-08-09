package bigBang.module.insurancePolicyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySectionView;

public class InsurancePolicyModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;
	
	public InsurancePolicyModule(){
	}
	
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus);
	}
	
	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Insurance Policy section
		InsurancePolicySection insurancePolicySection = new InsurancePolicySection(this.permissionManager);
		InsurancePolicySectionView insurancePolicySectionView = new InsurancePolicySectionView();
		InsurancePolicySectionViewPresenter insurancePolicySectionViewPresenter = new InsurancePolicySectionViewPresenter(eventBus, null, insurancePolicySectionView);
		insurancePolicySectionViewPresenter.setSection(insurancePolicySection);
		insurancePolicySection.registerEventHandlers(eventBus);
		sectionPresenters[0] = insurancePolicySectionViewPresenter;

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
