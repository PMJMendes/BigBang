package bigBang.module.receiptModule.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSearchOperationView;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.operation.ReceiptSearchOperation;

public class ReceiptSection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.RECEIPT;
	private static final String DESCRIPTION = "Recibos";
	private static final String SHORT_DESCRIPTION = "Recibos";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public ReceiptSection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		ReceiptSearchOperation receiptSearchOperation = (ReceiptSearchOperation)GWT.create(ReceiptSearchOperation.class);
		ReceiptSearchOperationView receiptSearchOperationView = (ReceiptSearchOperationView) GWT.create(ReceiptSearchOperationView.class);
		ReceiptServiceAsync receiptService = ReceiptService.Util.getInstance();
		ReceiptSearchOperationViewPresenter receiptSearchOperationViewPresenter = new ReceiptSearchOperationViewPresenter(null, receiptService, receiptSearchOperationView);
		receiptSearchOperationViewPresenter.setOperation((Operation) receiptSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)receiptSearchOperationViewPresenter);
	}
	
	@Override
	public String getId() {
		return ID;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public TextBadge getBadge() {
		return null;
	}

	public boolean hasBadge() {
		return false;
	}
	
	public OperationViewPresenter[] getOperationPresenters(){
		OperationViewPresenter[] result = new OperationViewPresenter[this.sectionOperationPresenters.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = this.sectionOperationPresenters.get(i);
		}
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : this.getOperationPresenters()) {
			p.setEventBus(eventBus);
		}
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.RECEIPT_SECTION;
	}

}
