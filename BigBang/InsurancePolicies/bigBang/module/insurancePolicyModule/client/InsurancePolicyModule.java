package bigBang.module.insurancePolicyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.module.insurancePolicyModule.client.dataAccess.ExerciseDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsurancePolicyProcessBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsuranceSubPolicyBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsuredObjectDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.SubPolicyInsuredObjectDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateDebitNoteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateExpenseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.ExerciseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyNegotiationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyOperationsViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTransferToClientViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuredObjectViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.NegotiationViewExternalInfoRequestViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyClientInfoOrDocumentRequestViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyClientSelectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyCreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyDeleteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyExerciseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyInsuredObjectViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyTransferToPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateDebitNoteView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateExpenseView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.ExerciseView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyNegotiationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyOperationsView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySearchOperationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySectionView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyTransferToClientView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyVoidView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsuredObjectView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.NegotiationViewExternalInfoRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyClientInfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyClientSelectionView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyCreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyDeleteView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyExerciseView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyInsuredObjectView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyTransferToPolicyView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyVoidView;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationCancellationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationExternalRequestViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationGrantViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationResponseViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationCancellationView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationDeleteView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationExternalRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationGrantView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationResponseView;

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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyView view = (SubPolicyView) GWT.create(SubPolicyView.class); 
				SubPolicyViewPresenter presenter = new SubPolicyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyDeleteView view = (SubPolicyDeleteView) GWT.create(SubPolicyDeleteView.class);
				SubPolicyDeleteViewPresenter presenter = new SubPolicyDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_INSURED_OBJECT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyInsuredObjectView view = (SubPolicyInsuredObjectView) GWT.create(SubPolicyInsuredObjectView.class);
				ViewPresenter presenter = new SubPolicyInsuredObjectViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_EXERCISE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyExerciseView view = (SubPolicyExerciseView) GWT.create(SubPolicyExerciseView.class);
				ViewPresenter presenter = new SubPolicyExerciseViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyNegotiationView<?> view = (InsurancePolicyNegotiationView<?>) GWT.create(InsurancePolicyNegotiationView.class); 
				InsurancePolicyNegotiationViewPresenter presenter = new InsurancePolicyNegotiationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationDeleteView view = (NegotiationDeleteView) GWT.create(NegotiationDeleteView.class); 
				NegotiationDeleteViewPresenter presenter = new NegotiationDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_CANCEL", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationCancellationView view = (NegotiationCancellationView) GWT.create(NegotiationCancellationView.class); 
				NegotiationCancellationViewPresenter presenter = new NegotiationCancellationViewPresenter(view);
				return presenter;
			}
		});

		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_TRANSFER_TO_CLIENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyTransferToClientView view = (InsurancePolicyTransferToClientView) GWT.create(InsurancePolicyTransferToClientView.class); 
				InsurancePolicyTransferToClientViewPresenter presenter = new InsurancePolicyTransferToClientViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_CLIENT_SELECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyClientSelectionView view = (SubPolicyClientSelectionView) GWT.create(SubPolicyClientSelectionView.class);
				ViewPresenter presenter = new SubPolicyClientSelectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_MAIN_POLICY_SELECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicySelectionView view = (InsurancePolicySelectionView) GWT.create(InsurancePolicySelectionView.class);
				InsurancePolicySelectionViewPresenter presenter = new InsurancePolicySelectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_EXTERNAL_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationExternalRequestView<?> view = (NegotiationExternalRequestView<?>) GWT.create(NegotiationExternalRequestView.class); 
				NegotiationExternalRequestViewPresenter presenter = new NegotiationExternalRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_VOID", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyVoidView view = (SubPolicyVoidView) GWT.create(SubPolicyVoidView.class);
				ViewPresenter presenter = new SubPolicyVoidViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_TRANSFER_TO_POLICY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyTransferToPolicyView view = (SubPolicyTransferToPolicyView) GWT.create(SubPolicyTransferToPolicyView.class);
				ViewPresenter presenter = new SubPolicyTransferToPolicyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_INFO_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyClientInfoOrDocumentRequestView view = (SubPolicyClientInfoOrDocumentRequestView) GWT.create(SubPolicyClientInfoOrDocumentRequestView.class);
				SubPolicyClientInfoOrDocumentRequestViewPresenter presenter = new SubPolicyClientInfoOrDocumentRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_CREATE_RECEIPT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyCreateReceiptView view = (SubPolicyCreateReceiptView) GWT.create(SubPolicyCreateReceiptView.class);
				SubPolicyCreateReceiptViewPresenter presenter = new SubPolicyCreateReceiptViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_GRANT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationGrantView view = (NegotiationGrantView ) GWT.create(NegotiationGrantView.class);
				NegotiationGrantViewPresenter presenter = new NegotiationGrantViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_RESPONSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationResponseView view = (NegotiationResponseView ) GWT.create(NegotiationResponseView.class);
				NegotiationResponseViewPresenter presenter = new NegotiationResponseViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_VIEW_EXTERNAL_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationViewExternalInfoRequestView view = (NegotiationViewExternalInfoRequestView) GWT.create(NegotiationViewExternalInfoRequestView.class);
				ViewPresenter presenter = new NegotiationViewExternalInfoRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_EXPENSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CreateExpenseView view = (CreateExpenseView) GWT.create(CreateExpenseView.class);
				CreateExpenseViewPresenter presenter = new CreateExpenseViewPresenter(view);
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
		SubPolicyInsuredObjectDataBrokerImpl subPolicyInsuredObjectDataBrokerImpl = new SubPolicyInsuredObjectDataBrokerImpl();
		ExerciseDataBrokerImpl exerciseDataBrokerImpl = new ExerciseDataBrokerImpl();

		return new DataBroker[]{
				insuredObjectDataBrokerImpl,
				subPolicyInsuredObjectDataBrokerImpl,
				exerciseDataBrokerImpl,
				new InsurancePolicyProcessBrokerImpl(insuredObjectDataBrokerImpl, exerciseDataBrokerImpl),
				new InsuranceSubPolicyBrokerImpl(subPolicyInsuredObjectDataBrokerImpl, exerciseDataBrokerImpl)
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				//TODO
		};
	}

}
