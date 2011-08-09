package bigBang.module.quoteRequestModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSectionViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSectionView;

public class QuoteRequestModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;
	
	public QuoteRequestModule(){
	}
	
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus);
	}
	
	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Quote request section
		QuoteRequestSection quoteRequestSection = new QuoteRequestSection(this.permissionManager);
		QuoteRequestSectionView quoteRequestSectionView = new QuoteRequestSectionView();
		QuoteRequestSectionViewPresenter quoteRequestSectionViewPresenter = new QuoteRequestSectionViewPresenter(eventBus, null, quoteRequestSectionView);
		quoteRequestSectionViewPresenter.setSection(quoteRequestSection);
		quoteRequestSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = quoteRequestSectionViewPresenter;

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
