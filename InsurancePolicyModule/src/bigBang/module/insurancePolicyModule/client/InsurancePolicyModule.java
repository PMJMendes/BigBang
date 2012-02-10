package bigBang.module.insurancePolicyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.client.dataAccess.ExerciseDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsurancePolicyProcessBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsuredObjectDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateDebitNoteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyOperationsViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuredObjectViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateDebitNoteView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.ExerciseView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyOperationsView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySearchOperationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySectionView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyVoidView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsuredObjectView;

import com.google.gwt.core.client.GWT;

public class InsurancePolicyModule implements Module {

	private boolean initialized = false;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicySectionView view = (InsurancePolicySectionView) GWT.create(InsurancePolicySectionView.class);
				ViewPresenter presenter = new InsurancePolicySectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyOperationsView view = (InsurancePolicyOperationsView) GWT.create(InsurancePolicyOperationsView.class);
				ViewPresenter presenter = new InsurancePolicyOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicySearchOperationView view = (InsurancePolicySearchOperationView) GWT.create(InsurancePolicySearchOperationView.class);
				InsurancePolicySearchOperationViewPresenter presenter = new InsurancePolicySearchOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_RECEIPT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CreateReceiptView view = (CreateReceiptView) GWT.create(CreateReceiptView.class);
				CreateReceiptViewPresenter presenter = new CreateReceiptViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_INSURED_OBJECT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsuredObjectView view = (InsuredObjectView) GWT.create(InsuredObjectView.class);
				InsuredObjectViewPresenter presenter = new InsuredObjectViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_EXERCISE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExerciseView view = (ExerciseView) GWT.create(ExerciseView.class);
				ViewPresenter presenter = new ExerciseViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_VOID", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyVoidView view = (InsurancePolicyVoidView) GWT.create(InsurancePolicyVoidView.class);
				ViewPresenter presenter = new InsurancePolicyVoidViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_DEBIT_NOTE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CreateDebitNoteView view = (CreateDebitNoteView) GWT.create(CreateDebitNoteView.class);
				ViewPresenter presenter = new CreateDebitNoteViewPresenter(view);
				return presenter;
			}
		});
	} 

	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		InsuredObjectDataBrokerImpl insuredObjectDataBrokerImpl = new InsuredObjectDataBrokerImpl();
		ExerciseDataBrokerImpl exerciseDataBrokerImpl = new ExerciseDataBrokerImpl();

		return new DataBroker[]{
				insuredObjectDataBrokerImpl,
				exerciseDataBrokerImpl,
				new InsurancePolicyProcessBrokerImpl(insuredObjectDataBrokerImpl, exerciseDataBrokerImpl)
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				//TODO
		};
	}

}
