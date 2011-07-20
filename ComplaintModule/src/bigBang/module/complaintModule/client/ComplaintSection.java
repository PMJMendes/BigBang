package bigBang.module.complaintModule.client;

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
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintSearchOperationViewPresenter;
import bigBang.module.complaintModule.client.userInterface.view.ComplaintSearchOperationView;
import bigBang.module.complaintModule.interfaces.ComplaintService;
import bigBang.module.complaintModule.interfaces.ComplaintServiceAsync;
import bigBang.module.complaintModule.shared.ModuleConstants;
import bigBang.module.complaintModule.shared.operation.ComplaintSearchOperation;

public class ComplaintSection implements MenuSection {


	private static final String ID = ModuleConstants.ProcessTypeIDs.COMPLAINT;
	private static final String DESCRIPTION = "Reclamação";
	private static final String SHORT_DESCRIPTION = "Reclamação";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public ComplaintSection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		ComplaintSearchOperation complaintSearchOperation = (ComplaintSearchOperation)GWT.create(ComplaintSearchOperation.class);
		ComplaintSearchOperationView complaintSearchOperationView = (ComplaintSearchOperationView) GWT.create(ComplaintSearchOperationView.class);
		ComplaintServiceAsync complaintService = ComplaintService.Util.getInstance();
		ComplaintSearchOperationViewPresenter complaintSearchOperationViewPresenter = new ComplaintSearchOperationViewPresenter(null, complaintService, complaintSearchOperationView);
		complaintSearchOperationViewPresenter.setOperation((Operation) complaintSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)complaintSearchOperationViewPresenter);
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
		return MenuSections.COMPLAINT_SECTION;
	}

}
