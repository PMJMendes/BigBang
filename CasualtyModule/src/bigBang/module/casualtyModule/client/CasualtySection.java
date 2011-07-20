package bigBang.module.casualtyModule.client;

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
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySearchOperationView;
import bigBang.module.casualtyModule.interfaces.CasualtyService;
import bigBang.module.casualtyModule.interfaces.CasualtyServiceAsync;
import bigBang.module.casualtyModule.shared.ModuleConstants;
import bigBang.module.casualtyModule.shared.operation.CasualtySearchOperation;

public class CasualtySection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.CASUALTY;
	private static final String DESCRIPTION = "Sinistro";
	private static final String SHORT_DESCRIPTION = "Sinistro";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public CasualtySection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		CasualtySearchOperation casualtySearchOperation = (CasualtySearchOperation)GWT.create(CasualtySearchOperation.class);
		CasualtySearchOperationView casualtySearchOperationView = (CasualtySearchOperationView) GWT.create(CasualtySearchOperationView.class);
		CasualtyServiceAsync casualtyService = CasualtyService.Util.getInstance();
		CasualtySearchOperationViewPresenter casualtySearchOperationViewPresenter = new CasualtySearchOperationViewPresenter(null, casualtyService, casualtySearchOperationView);
		casualtySearchOperationViewPresenter.setOperation((Operation) casualtySearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)casualtySearchOperationViewPresenter);
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
