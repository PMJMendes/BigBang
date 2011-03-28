package bigBang.module.generalSystemModule.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.interfaces.Service;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenterManager;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxesAndMultipliersManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.CostCenterManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.InsuranceAgencyManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.MediatorManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.UserManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.interfaces.CostCenterServiceAsync;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;
import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.generalSystemModule.shared.operation.CostCenterManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.CoverageManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.MediatorManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.TaxesAndMultipliersManagementOperation;
import bigBang.module.generalSystemModule.shared.operation.UserManagementOperation;

public class GeneralSystemSection implements MenuSection {

	private static final String ID = "GENERAL_SYSTEM_SECTION";
	private static final String DESCRIPTION = "Sistema Geral";
	private static final String SHORT_DESCRIPTION = "Sist. Geral";
	private HashMap<String, OperationViewPresenter> sectionOperationPresenters; 
	
	public GeneralSystemSection(){
		this.sectionOperationPresenters = new HashMap<String, OperationViewPresenter>();

		/* COST CENTER MANAGEMENT */
		CostCenterManagementOperation costCenterManagementOperation = (CostCenterManagementOperation)GWT.create(CostCenterManagementOperation.class);
		CostCenterManagementOperationView costCenterManagementOperationView = new CostCenterManagementOperationView();
		CostCenterServiceAsync costCenterService = CostCenterService.Util.getInstance();
		CostCenterManagementOperationViewPresenter costCenterManagementOperationPresenter = new CostCenterManagementOperationViewPresenter(null, (Service)costCenterService, costCenterManagementOperationView);
		costCenterManagementOperationPresenter.setOperation(costCenterManagementOperation);
		this.sectionOperationPresenters.put(CostCenterManagementOperation.ID, (OperationViewPresenter)costCenterManagementOperationPresenter);

		/* USERS MANAGEMENT */
		UserManagementOperation userManagementOperation = (UserManagementOperation)GWT.create(UserManagementOperation.class);
		UserManagementOperationView userManagementOperationView = new UserManagementOperationView();
		UserServiceAsync userService = UserService.Util.getInstance();
		UserManagementOperationViewPresenter userManagementOperationPresenter = new UserManagementOperationViewPresenter(null, userService, userManagementOperationView);
		userManagementOperationPresenter.setOperation(userManagementOperation);
		this.sectionOperationPresenters.put(UserManagementOperation.ID, (OperationViewPresenter)userManagementOperationPresenter);
		
		/* CLIENT GROUP MANAGEMENT */

		/* MEDIATORS MANAGEMENT */
		MediatorManagementOperation mediatorManagementOperation = (MediatorManagementOperation)GWT.create(MediatorManagementOperation.class);
		MediatorManagementOperationView mediatorManagementOperationView = new MediatorManagementOperationView();
		MediatorServiceAsync mediatorService = MediatorService.Util.getInstance();
		MediatorManagementOperationViewPresenter mediatorManagementOperationPresenter = new MediatorManagementOperationViewPresenter(null, mediatorService, mediatorManagementOperationView);
		mediatorManagementOperationPresenter.setOperation(mediatorManagementOperation);
		this.sectionOperationPresenters.put(MediatorManagementOperation.ID, (OperationViewPresenter)mediatorManagementOperationPresenter);
		
		/* INSURANCE AGENCIES MANAGEMENT */
		InsuranceAgencyManagementOperation insuranceAgencyManagementOperation = (InsuranceAgencyManagementOperation)GWT.create(InsuranceAgencyManagementOperation.class);
		InsuranceAgencyManagementOperationView insuranceAgencyManagementOperationView = (InsuranceAgencyManagementOperationView) GWT.create(InsuranceAgencyManagementOperationView.class);
		InsuranceAgencyManagementOperationViewPresenter insuranceAgencyManagementOperationPresenter = new InsuranceAgencyManagementOperationViewPresenter(null, null, insuranceAgencyManagementOperationView);
		insuranceAgencyManagementOperationPresenter.setOperation(insuranceAgencyManagementOperation);
		this.sectionOperationPresenters.put(insuranceAgencyManagementOperation.ID, (OperationViewPresenter)insuranceAgencyManagementOperationPresenter);
		
		/* TAXES AND MULTIPLIERS MANAGEMENT */
		TaxesAndMultipliersManagementOperation taxesAndMultipliersManagementOperation = (TaxesAndMultipliersManagementOperation)GWT.create(TaxesAndMultipliersManagementOperation.class);
		TaxesAndMultipliersManagementOperationViewPresenter taxesAndMultipliersManagementOperationPresenter = (TaxesAndMultipliersManagementOperationViewPresenter) GWT.create(TaxesAndMultipliersManagementOperationViewPresenter.class);
		taxesAndMultipliersManagementOperationPresenter.setOperation(taxesAndMultipliersManagementOperation);
		this.sectionOperationPresenters.put(taxesAndMultipliersManagementOperation.ID, (OperationViewPresenter)taxesAndMultipliersManagementOperationPresenter);

		/* COVERAGES MANAGEMENT */
		CoverageManagementOperation coverageManagementOperation = (CoverageManagementOperation)GWT.create(CoverageManagementOperation.class);
		CoverageManagementOperationView coverageManagementOperationView = (CoverageManagementOperationView) GWT.create(CoverageManagementOperationView.class);
		CoverageManagementOperationViewPresenter coverageManagementOperationPresenter = new CoverageManagementOperationViewPresenter(null, null, coverageManagementOperationView);
		coverageManagementOperationPresenter.setOperation(coverageManagementOperation);
		this.sectionOperationPresenters.put(coverageManagementOperation.ID, (OperationViewPresenter)coverageManagementOperationPresenter);

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
		int nOps = this.sectionOperationPresenters.size();
		OperationViewPresenter[] result = new OperationViewPresenter[nOps];
		for(int i = 0; i < nOps; i++)
			result[i] = (OperationViewPresenter)this.sectionOperationPresenters.values().toArray()[i];
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : this.getOperationPresenters()) {
			p.setEventBus(eventBus);
		}
	}

}
