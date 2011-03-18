package bigBang.module.clientModule.client;

import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientSectionView;

public class ClientModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	
	public ClientModule(){
	}
	
	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];
		
		//Client section
		ClientSection clientSection = new ClientSection();
		ClientSectionView clientSectionView = new ClientSectionView();
		ClientSectionViewPresenter clientSectionPresenter = new ClientSectionViewPresenter(eventBus, null, clientSectionView);
		clientSectionPresenter.setSection(clientSection);
		clientSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = clientSectionPresenter;
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
	
}
