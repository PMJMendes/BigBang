package bigBang.module.riskAnalisysModule.client;

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
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSearchOperationViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysSearchOperationView;
import bigBang.module.riskAnalisysModule.interfaces.RiskAnalisysService;
import bigBang.module.riskAnalisysModule.interfaces.RiskAnalisysServiceAsync;
import bigBang.module.riskAnalisysModule.shared.ModuleConstants;
import bigBang.module.riskAnalisysModule.shared.operation.RiskAnalisysSearchOperation;

public class RiskAnalisysSection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.RISK_ANALISYS;
	private static final String DESCRIPTION = "Análise de Risco";
	private static final String SHORT_DESCRIPTION = "A. Risco";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public RiskAnalisysSection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		RiskAnalisysSearchOperation riskAnalisysSearchOperation = (RiskAnalisysSearchOperation)GWT.create(RiskAnalisysSearchOperation.class);
		RiskAnalisysSearchOperationView riskAnalisysSearchOperationView = (RiskAnalisysSearchOperationView) GWT.create(RiskAnalisysSearchOperationView.class);
		RiskAnalisysServiceAsync riskAnalisysService = RiskAnalisysService.Util.getInstance();
		RiskAnalisysSearchOperationViewPresenter riskAnalisysSearchOperationViewPresenter = new RiskAnalisysSearchOperationViewPresenter(null, riskAnalisysService, riskAnalisysSearchOperationView);
		riskAnalisysSearchOperationViewPresenter.setOperation((Operation) riskAnalisysSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)riskAnalisysSearchOperationViewPresenter);
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
