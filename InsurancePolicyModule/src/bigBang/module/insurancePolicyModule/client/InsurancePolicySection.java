package bigBang.module.insurancePolicyModule.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.Operation;
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.library.client.event.ScreenInvokedEventHandler;
import bigBang.library.client.event.ShowMeRequestEvent;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySearchOperationView;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;
import bigBang.module.insurancePolicyModule.shared.operation.InsurancePolicySearchOperation;

public class InsurancePolicySection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.INSURANCE_POLICY;
	private static final String DESCRIPTION = "Apólices";
	private static final String SHORT_DESCRIPTION = "Apólices";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public InsurancePolicySection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		InsurancePolicySearchOperation insurancePolicySearchOperation = (InsurancePolicySearchOperation)GWT.create(InsurancePolicySearchOperation.class);
		InsurancePolicySearchOperationView insurancePolicySearchOperationView = (InsurancePolicySearchOperationView) GWT.create(InsurancePolicySearchOperationView.class);
		InsurancePolicyServiceAsync insurancePolicyService = InsurancePolicyService.Util.getInstance();
		InsurancePolicySearchOperationViewPresenter insurancePolicySearchOperationViewPresenter = new InsurancePolicySearchOperationViewPresenter(null, insurancePolicyService, insurancePolicySearchOperationView);
		insurancePolicySearchOperationViewPresenter.setOperation((Operation) insurancePolicySearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)insurancePolicySearchOperationViewPresenter);
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

	public void registerEventHandlers(final EventBus eventBus) {
		for(ViewPresenter p : this.getOperationPresenters()) {
			p.setEventBus(eventBus);
		}
		eventBus.addHandler(ScreenInvokedEvent.TYPE, new ScreenInvokedEventHandler() {
			
			@Override
			public void onScreenInvoked(ScreenInvokedEvent event) {
				String processTypeId = event.getProcessTypeId();
				if(processTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					eventBus.fireEvent(new ShowMeRequestEvent(this));
				}
			}
		});
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.INSURANCE_POLICY_SECTION;
	}

}
