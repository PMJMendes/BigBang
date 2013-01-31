package bigBang.module.generalSystemModule.client;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.ExpandableSelectionManagementPanelInstantiator;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LoginSuccessEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.presenter.CasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ExpenseSelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.OtherEntitySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.CasualtySelectionView;
import bigBang.library.client.userInterface.view.ExpenseSelectionView;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.library.client.userInterface.view.InsuranceSubPolicySelectionView;
import bigBang.library.client.userInterface.view.OtherEntitySelectionView;
import bigBang.library.client.userInterface.view.SubCasualtySelectionView;
import bigBang.module.casualtyModule.client.userInterface.presenter.AssessmentSelectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileSelectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.TotalLossFileSelectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.view.AssessmentSelectionView;
import bigBang.module.casualtyModule.client.userInterface.view.MedicalFileSelectionView;
import bigBang.module.casualtyModule.client.userInterface.view.TotalLossFileSelectionView;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSelectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientSelectionView;
import bigBang.module.generalSystemModule.client.dataAccess.ClientGroupBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CostCenterBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CoverageBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.InsuranceAgencyBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.MediatorBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.OtherEntityBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.UserBrokerImpl;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.OtherEntityManagementViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.ClientGroupManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CostCenterManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;
import bigBang.module.generalSystemModule.client.userInterface.view.InsuranceAgencyManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.MediatorManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.OtherEntityManagementView;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxManagementOperationView;
import bigBang.module.generalSystemModule.client.userInterface.view.UserManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;
import bigBang.module.generalSystemModule.shared.GeneralSystem;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSelectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSelectionView;

import com.google.gwt.core.client.GWT;

public class GeneralSystemModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		initializeProcess();
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
		
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("GENERAL_SYSTEM_OTHER_ENTITIES_MANAGEMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				OtherEntityManagementView view = (OtherEntityManagementView) GWT.create(OtherEntityManagementView.class);
				ViewPresenter presenter = new OtherEntityManagementViewPresenter(view);
				return presenter;
			}
		});

		//EXPANDABLE PANELS
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.INSURANCE_POLICY, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				InsurancePolicySelectionView view = (InsurancePolicySelectionView) GWT.create(InsurancePolicySelectionView.class);
				InsurancePolicySelectionViewPresenter presenter = new InsurancePolicySelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.CLIENT, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				ClientSelectionView view = (ClientSelectionView) GWT.create(ClientSelectionView.class);
				ClientSelectionViewPresenter presenter = new ClientSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				InsuranceSubPolicySelectionView view = (InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class);
				InsuranceSubPolicySelectionViewPresenter presenter = new InsuranceSubPolicySelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.RECEIPT, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				ReceiptSelectionView view = (ReceiptSelectionView) GWT.create(ReceiptSelectionView.class);
				ReceiptSelectionViewPresenter presenter = new ReceiptSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.CASUALTY, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				CasualtySelectionView view = (CasualtySelectionView) GWT.create(CasualtySelectionView.class);
				CasualtySelectionViewPresenter presenter = new CasualtySelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.SUB_CASUALTY, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				SubCasualtySelectionView view = (SubCasualtySelectionView) GWT.create(SubCasualtySelectionView.class);
				SubCasualtySelectionViewPresenter presenter = new SubCasualtySelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.EXPENSE, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				ExpenseSelectionView view = (ExpenseSelectionView) GWT.create(ExpenseSelectionView.class);
				ExpenseSelectionViewPresenter presenter = new ExpenseSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.ASSESSMENT, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				AssessmentSelectionView view = (AssessmentSelectionView) GWT.create(AssessmentSelectionView.class);
				AssessmentSelectionViewPresenter presenter = new AssessmentSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.MEDICAL_FILE, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				MedicalFileSelectionView view = (MedicalFileSelectionView) GWT.create(MedicalFileSelectionView.class);
				MedicalFileSelectionViewPresenter presenter = new MedicalFileSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.OTHER_ENTITY, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				OtherEntitySelectionView view = (OtherEntitySelectionView) GWT.create(OtherEntitySelectionView.class);
				OtherEntitySelectionViewPresenter presenter = new OtherEntitySelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
		
		MutableSelectionFormFieldFactory.registerPanelInstantiator(BigBangConstants.EntityIds.TOTAL_LOSS_FILE, new ExpandableSelectionManagementPanelInstantiator() {

			@Override
			public ExpandableSelectionFormFieldPanel getInstance() {
				TotalLossFileSelectionView view = (TotalLossFileSelectionView) GWT.create(TotalLossFileSelectionView.class);
				TotalLossFileSelectionViewPresenter presenter = new TotalLossFileSelectionViewPresenter(view);
				presenter.go();
				return presenter;
			}
		});
	}

	protected void initializeProcess(){
		EventBus.getInstance().addHandler(LoginSuccessEvent.TYPE, new LoginSuccessEventHandler() {

			@Override
			public void onLoginSuccess(LoginSuccessEvent event) {
				GeneralSystemService.Util.getInstance().getGeneralSystem(new BigBangAsyncCallback<GeneralSystem>() {

					@Override
					public void onResponseSuccess(GeneralSystem result) {
						SessionGeneralSystem.setGeneralSystem(result);
						NavigationHistoryManager.getInstance().reload();
					}
				});
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
				new OtherEntityBrokerImpl()
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
				BigBangConstants.EntityIds.OTHER_ENTITY
		};
	}
}
