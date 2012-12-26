package bigBang.module.insurancePolicyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.ExpandableSelectionManagementPanelInstantiator;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.presenter.GeneralTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.GeneralTasksView;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSelectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientSelectionView;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsurancePolicyProcessBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsuranceSubPolicyBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.InsuredObjectDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.dataAccess.SubPolicyInsuredObjectDataBrokerImpl;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateDebitNoteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateExpenseSubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateExpenseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyConversationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyNegotiationConversationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySendMessageViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyReceiveMessageViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyCreateSubPolicyReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyManagerTransferViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyMassManagerTransferViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyNegotiationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyOperationsViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTasksViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTransferToClientViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuranceSubPolicyConversationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuranceSubPolicyTasksViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyReceiveMessageViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicySendMessageViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyCreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyDeleteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyTransferToPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateDebitNoteView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateExpenseSubPolicyView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateExpenseView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyConversationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyCreateSubPolicyReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyNegotiationConversationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyReceiveMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyManagerTransferView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyMassManagerTransferView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyNegotiationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyOperationsView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySearchOperationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySectionView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicySendMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyTasksView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyTransferToClientView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsurancePolicyVoidView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsuranceSubPolicyConversationView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsuranceSubPolicyTasksView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyReceiveMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicySendMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyCreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyDeleteView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyTransferToPolicyView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.SubPolicyVoidView;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationReceiveMessageViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationResponseViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationSendMessageViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationDeleteView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationReceiveMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationResponseView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationSendMessageView;

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
				ClientSelectionView view = (ClientSelectionView) GWT.create(ClientSelectionView.class);
				ViewPresenter presenter = new ClientSelectionViewPresenter(view);
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_RECEIVE_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationReceiveMessageView view = (NegotiationReceiveMessageView) GWT.create(NegotiationReceiveMessageView.class); 
				NegotiationReceiveMessageViewPresenter presenter = new NegotiationReceiveMessageViewPresenter(view);
				return presenter;
			}
		});	
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_SEND_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationSendMessageView view = (NegotiationSendMessageView) GWT.create(NegotiationSendMessageView.class); 
				NegotiationSendMessageViewPresenter presenter = new NegotiationSendMessageViewPresenter(view);
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_SEND_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicySendMessageView view = (SubPolicySendMessageView) GWT.create(SubPolicySendMessageView.class);
				SubPolicySendMessageViewPresenter presenter = new SubPolicySendMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SUB_POLICY_RECEIVE_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubPolicyReceiveMessageView view = (SubPolicyReceiveMessageView) GWT.create(SubPolicyReceiveMessageView.class);
				SubPolicyReceiveMessageViewPresenter presenter = new SubPolicyReceiveMessageViewPresenter(view);
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_RESPONSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationResponseView view = (NegotiationResponseView ) GWT.create(NegotiationResponseView.class);
				NegotiationResponseViewPresenter presenter = new NegotiationResponseViewPresenter(view);
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_EXPENSE_SUB_POLICY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CreateExpenseSubPolicyView view = (CreateExpenseSubPolicyView) GWT.create(CreateExpenseSubPolicyView.class);
				CreateExpenseSubPolicyViewPresenter presenter = new CreateExpenseSubPolicyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_MASS_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyMassManagerTransferView view = (InsurancePolicyMassManagerTransferView) GWT.create(InsurancePolicyMassManagerTransferView.class);
				ViewPresenter presenter = new InsurancePolicyMassManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyManagerTransferView view = (InsurancePolicyManagerTransferView) GWT.create(InsurancePolicyManagerTransferView.class);
				ViewPresenter presenter = new InsurancePolicyManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyTasksView view = (InsurancePolicyTasksView) GWT.create(InsurancePolicyTasksView.class);
				ViewPresenter presenter = new InsurancePolicyTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_SUB_POLICY_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsuranceSubPolicyTasksView view = (InsuranceSubPolicyTasksView) GWT.create(InsuranceSubPolicyTasksView.class);
				ViewPresenter presenter  = new InsuranceSubPolicyTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_RECEIVE_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyReceiveMessageView view = (InsurancePolicyReceiveMessageView) GWT.create(InsurancePolicyReceiveMessageView.class);
				ViewPresenter presenter = new InsurancePolicyReceiveMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_SEND_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicySendMessageView view = (InsurancePolicySendMessageView) GWT.create(InsurancePolicySendMessageView.class);
				InsurancePolicySendMessageViewPresenter presenter = new InsurancePolicySendMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CREATE_SUB_POLICY_RECEIPT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyCreateSubPolicyReceiptView view = (InsurancePolicyCreateSubPolicyReceiptView) GWT.create(InsurancePolicyCreateSubPolicyReceiptView.class);
				InsurancePolicyCreateSubPolicyReceiptViewPresenter presenter = new InsurancePolicyCreateSubPolicyReceiptViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyConversationView view = (InsurancePolicyConversationView) GWT.create(InsurancePolicyConversationView.class);
				InsurancePolicyConversationViewPresenter presenter = new InsurancePolicyConversationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_SUB_POLICY_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsuranceSubPolicyConversationView view = (InsuranceSubPolicyConversationView) GWT.create(InsuranceSubPolicyConversationView.class);
				InsuranceSubPolicyConversationViewPresenter presenter = new InsuranceSubPolicyConversationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_NEGOTIATION_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicyNegotiationConversationView view = (InsurancePolicyNegotiationConversationView) GWT.create(InsurancePolicyNegotiationConversationView.class);
				InsurancePolicyNegotiationConversationViewPresenter presenter = new InsurancePolicyNegotiationConversationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INSURANCE_POLICY_GENERAL_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				GeneralTasksView view = new GeneralTasksView() {

					@Override
					public String getItemType() {
						return "apolice";
					}
				};

				GeneralTasksViewPresenter presenter = new GeneralTasksViewPresenter();
				presenter.setView(view);
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

	} 

	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker[]{
				new SubPolicyInsuredObjectDataBrokerImpl(),
				new InsurancePolicyProcessBrokerImpl(),
				new InsuranceSubPolicyBrokerImpl(), 
				new InsuredObjectDataBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				//TODO
		};
	}

}
