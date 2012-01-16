package bigBang.module.generalSystemModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.dataAccess.HistoryBrokerImpl;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.generalSystemModule.client.dataAccess.ClientGroupBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CostCenterBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CoverageBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.InsuranceAgencyBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.MediatorBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.UserBrokerImpl;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.ClientGroupManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CostCenterManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;
import bigBang.module.generalSystemModule.client.userInterface.view.InsuranceAgencyManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.MediatorManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.UserManagementOperationView;

import com.google.gwt.core.client.GWT;

public class GeneralSystemModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				GeneralSystemSectionView view = (GeneralSystemSectionView) GWT.create(GeneralSystemSectionView.class);
				ViewPresenter presenter = new GeneralSystemSectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_USER_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				UserManagementOperationView view = (UserManagementOperationView) GWT.create(UserManagementOperationView.class);
				ViewPresenter presenter = new UserManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_MEDIATOR_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				MediatorManagementOperationView view = (MediatorManagementOperationView) GWT.create(MediatorManagementOperationView.class);
				ViewPresenter presenter = new MediatorManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_COST_CENTER_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CostCenterManagementOperationView view = (CostCenterManagementOperationView) GWT.create(CostCenterManagementOperationView.class);
				ViewPresenter presenter = new CostCenterManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_INSURANCE_AGENCY_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsuranceAgencyManagementOperationView view = (InsuranceAgencyManagementOperationView) GWT.create(InsuranceAgencyManagementOperationView.class);
				ViewPresenter presenter = new InsuranceAgencyManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_COVERAGE_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CoverageManagementOperationView view = (CoverageManagementOperationView) GWT.create(CoverageManagementOperationView.class);
				ViewPresenter presenter = new CoverageManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_TAX_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				TaxManagementOperationView view = (TaxManagementOperationView) GWT.create(TaxManagementOperationView.class);
				ViewPresenter presenter = new TaxManagementOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_CLIENT_GROUP_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ClientGroupManagementOperationView view = (ClientGroupManagementOperationView) GWT.create(ClientGroupManagementOperationView.class);
				ViewPresenter presenter = new ClientGroupManagementOperationViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new CostCenterBrokerImpl(),
				new UserBrokerImpl(),
				new InsuranceAgencyBrokerImpl(),
				new ClientGroupBrokerImpl(),
				new MediatorBrokerImpl(),
				new CoverageBrokerImpl(),
				new HistoryBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.COST_CENTER,
				BigBangConstants.EntityIds.USER,
				BigBangConstants.EntityIds.INSURANCE_AGENCY,
				BigBangConstants.EntityIds.CLIENT_GROUP,
				BigBangConstants.EntityIds.MEDIATOR,
				BigBangConstants.EntityIds.COVERAGE,
				BigBangConstants.EntityIds.HISTORY
		};
	}
}
