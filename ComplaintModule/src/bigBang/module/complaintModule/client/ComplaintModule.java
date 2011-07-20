package bigBang.module.complaintModule.client;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintSectionViewPresenter;
import bigBang.module.complaintModule.client.userInterface.view.ComplaintSectionView;

public class ComplaintModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;

	public ComplaintModule(){
	}

	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus); 
	}

	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Complaint section
		ComplaintSection complaintSection = new ComplaintSection(this.permissionManager);
		ComplaintSectionView complaintSectionView = new ComplaintSectionView();
		ComplaintSectionViewPresenter complaintSectionViewPresenter = new ComplaintSectionViewPresenter(eventBus, null, complaintSectionView);
		complaintSectionViewPresenter.setSection(complaintSection);
		complaintSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = complaintSectionViewPresenter;

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


}
