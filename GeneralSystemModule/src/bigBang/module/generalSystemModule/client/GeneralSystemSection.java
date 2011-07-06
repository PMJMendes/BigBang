package bigBang.module.generalSystemModule.client;

import java.util.ArrayList;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.interfaces.Service;
import bigBang.library.interfaces.UndoService;
import bigBang.library.interfaces.UndoServiceAsync;
import bigBang.library.shared.operation.UndoOperation;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.CostCenterManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.InsuranceAgencyManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.MediatorManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.UserManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.interfaces.CostCenterServiceAsync;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyService;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyServiceAsync;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;
import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.operation.CostCenterManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.CoverageManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.MediatorManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.TaxManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.UserManagementOperation;

import com.google.gwt.core.client.GWT;

public class GeneralSystemSection implements MenuSection {
	
	private static final String ID = ModuleConstants.ProcessTypeIDs.GENERAL_SYSTEM;
	private static final String DESCRIPTION = "Sistema Geral";
	private static final String SHORT_DESCRIPTION = "Sist. Geral";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public GeneralSystemSection(BigBangPermissionManager permissionManager, String processId){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		/* UNDO */
		UndoOperation undoOperation = (UndoOperation)GWT.create(UndoOperation.class);
		UndoOperationView undoOperationView = (UndoOperationView) GWT.create(UndoOperationView.class);
		UndoServiceAsync undoService = UndoService.Util.getInstance();
		UndoOperationViewPresenter undoOperationViewPresenter = new UndoOperationViewPresenter(null, undoService, undoOperationView, processId);
		undoOperationViewPresenter.setOperation(undoOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter) undoOperationViewPresenter);
		
		/* COST CENTER MANAGEMENT */
		CostCenterManagementOperation costCenterManagementOperation = (CostCenterManagementOperation)GWT.create(CostCenterManagementOperation.class);
		CostCenterManagementOperationView costCenterManagementOperationView = new CostCenterManagementOperationView();
		CostCenterServiceAsync costCenterService = CostCenterService.Util.getInstance();
		CostCenterManagementOperationViewPresenter costCenterManagementOperationPresenter = new CostCenterManagementOperationViewPresenter(null, (Service)costCenterService, costCenterManagementOperationView);
		costCenterManagementOperationPresenter.setOperation(costCenterManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)costCenterManagementOperationPresenter);

		/* USERS MANAGEMENT */
		UserManagementOperation userManagementOperation = (UserManagementOperation)GWT.create(UserManagementOperation.class);
		UserManagementOperationView userManagementOperationView = new UserManagementOperationView();
		UserServiceAsync userService = UserService.Util.getInstance();
		UserManagementOperationViewPresenter userManagementOperationPresenter = new UserManagementOperationViewPresenter(null, userService, userManagementOperationView);
		userManagementOperationPresenter.setOperation(userManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)userManagementOperationPresenter);
		
		/* MEDIATORS MANAGEMENT */
		MediatorManagementOperation mediatorManagementOperation = (MediatorManagementOperation)GWT.create(MediatorManagementOperation.class);
		MediatorManagementOperationView mediatorManagementOperationView = new MediatorManagementOperationView();
		MediatorServiceAsync mediatorService = MediatorService.Util.getInstance();
		MediatorManagementOperationViewPresenter mediatorManagementOperationPresenter = new MediatorManagementOperationViewPresenter(null, mediatorService, mediatorManagementOperationView);
		mediatorManagementOperationPresenter.setOperation(mediatorManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)mediatorManagementOperationPresenter);
		
		/* INSURANCE AGENCIES MANAGEMENT */
		InsuranceAgencyManagementOperation insuranceAgencyManagementOperation = (InsuranceAgencyManagementOperation)GWT.create(InsuranceAgencyManagementOperation.class);
		InsuranceAgencyManagementOperationView insuranceAgencyManagementOperationView = (InsuranceAgencyManagementOperationView) GWT.create(InsuranceAgencyManagementOperationView.class);
		InsuranceAgencyServiceAsync insuranceAgencyService = InsuranceAgencyService.Util.getInstance();
		InsuranceAgencyManagementOperationViewPresenter insuranceAgencyManagementOperationPresenter = new InsuranceAgencyManagementOperationViewPresenter(null, insuranceAgencyService, insuranceAgencyManagementOperationView);
		insuranceAgencyManagementOperationPresenter.setOperation(insuranceAgencyManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)insuranceAgencyManagementOperationPresenter);
		
		/* COVERAGES MANAGEMENT */
		CoverageManagementOperation coverageManagementOperation = (CoverageManagementOperation)GWT.create(CoverageManagementOperation.class);
		CoverageManagementOperationView coverageManagementOperationView = (CoverageManagementOperationView) GWT.create(CoverageManagementOperationView.class);
		CoveragesServiceAsync coveragesService = CoveragesService.Util.getInstance();
		CoverageManagementOperationViewPresenter coverageManagementOperationPresenter = new CoverageManagementOperationViewPresenter(null, coveragesService, coverageManagementOperationView);
		coverageManagementOperationPresenter.setOperation(coverageManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)coverageManagementOperationPresenter);
		
		/* TAXES MANAGEMENT */
		TaxManagementOperation taxManagementOperation = (TaxManagementOperation)GWT.create(TaxManagementOperation.class);
		TaxManagementOperationView taxManagementOperationView = (TaxManagementOperationView) GWT.create(TaxManagementOperationView.class);
		TaxManagementOperationViewPresenter taxManagementOperationPresenter = new TaxManagementOperationViewPresenter(null, coveragesService, taxManagementOperationView);
		taxManagementOperationPresenter.setOperation(taxManagementOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)taxManagementOperationPresenter);
	}
	
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
		return MenuSections.GENERAL_SYSTEM_SECTION;
	}

}
