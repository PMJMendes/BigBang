package bigBang.module.receiptModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.receiptModule.client.dataAccess.ReceiptDataBrokerImpl;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSectionView;

public class ReceiptModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;
	
	public ReceiptModule(){
	}
	
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus);
	}
	
	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Receipt section
		ReceiptSection receiptSection = new ReceiptSection(this.permissionManager);
		ReceiptSectionView receiptSectionView = new ReceiptSectionView();
		ReceiptSectionViewPresenter receiptSectionViewPresenter = new ReceiptSectionViewPresenter(eventBus, null, receiptSectionView);
		receiptSectionViewPresenter.setSection(receiptSection);
		receiptSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = receiptSectionViewPresenter;

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
		return new DataBroker<?>[]{
			new ReceiptDataBrokerImpl()	
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
			BigBangConstants.EntityIds.RECEIPT	
		};
	}

}
