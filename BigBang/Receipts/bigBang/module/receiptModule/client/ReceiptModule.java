package bigBang.module.receiptModule.client;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.GeneralTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.GeneralTasksView;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.library.client.userInterface.view.InsuranceSubPolicySelectionView;
import bigBang.library.client.userInterface.view.SubCasualtySelectionView;
import bigBang.module.receiptModule.client.dataAccess.DASRequestBrokerImpl;
import bigBang.module.receiptModule.client.dataAccess.ReceiptDataBrokerImpl;
import bigBang.module.receiptModule.client.dataAccess.SignatureRequestBrokerImpl;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MarkForPaymentViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassAgentAccountingViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassCreatePaymentNoticeViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassCreateSecondPaymentNoticeViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassInsurerAccountingViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassReceiptGenerationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassReturnToInsurerViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassSendPaymentViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassSendReceiptViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptAssociateWithDebitNoteViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptConversationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptOperationsViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptReceiveMessageViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptReturnViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSendMessageViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToOwnerViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.RepeatDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialMarkForPaymentViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.VoidDebitNoteViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.CancelDASRequestView;
import bigBang.module.receiptModule.client.userInterface.view.CancelSignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.CreateDASRequestView;
import bigBang.module.receiptModule.client.userInterface.view.CreateSignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.DASRequestTasksView;
import bigBang.module.receiptModule.client.userInterface.view.DASRequestView;
import bigBang.module.receiptModule.client.userInterface.view.MarkForPaymentView;
import bigBang.module.receiptModule.client.userInterface.view.MassAgentAccountingView;
import bigBang.module.receiptModule.client.userInterface.view.MassCreatePaymentNoticeView;
import bigBang.module.receiptModule.client.userInterface.view.MassCreateSecondPaymentNoticeView;
import bigBang.module.receiptModule.client.userInterface.view.MassInsurerAccountingView;
import bigBang.module.receiptModule.client.userInterface.view.MassReceiptGenerationView;
import bigBang.module.receiptModule.client.userInterface.view.MassReturnToInsurerView;
import bigBang.module.receiptModule.client.userInterface.view.MassSendPaymentView;
import bigBang.module.receiptModule.client.userInterface.view.MassSendReceiptView;
import bigBang.module.receiptModule.client.userInterface.view.MassSignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptAssociateWithDebitNoteView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptConversationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptOperationsView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptReceiveMessageView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptReturnView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSearchOperationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSectionView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSendMessageView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptTasksView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptTransferToOwnerView;
import bigBang.module.receiptModule.client.userInterface.view.RepeatDASRequestView;
import bigBang.module.receiptModule.client.userInterface.view.SerialMarkForPaymentView;
import bigBang.module.receiptModule.client.userInterface.view.SerialReceiptCreationView;
import bigBang.module.receiptModule.client.userInterface.view.SignatureRequestTasksView;
import bigBang.module.receiptModule.client.userInterface.view.SignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.VoidDebitNoteView;

import com.google.gwt.core.client.GWT;

public class ReceiptModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	public boolean isInitialized() {
		return this.initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				//Receipt section
				ReceiptSectionView receiptSectionView = (ReceiptSectionView) GWT.create(ReceiptSectionView.class);
				ReceiptSectionViewPresenter receiptSectionViewPresenter = new ReceiptSectionViewPresenter(receiptSectionView);
				return receiptSectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiptOperationsView view = (ReceiptOperationsView) GWT.create(ReceiptOperationsView.class);
				ViewPresenter presenter = new ReceiptOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiptSearchOperationView receiptSearchOperationView = (ReceiptSearchOperationView) GWT.create(ReceiptSearchOperationView.class);
				ReceiptSearchOperationViewPresenter receiptSearchOperationViewPresenter = new ReceiptSearchOperationViewPresenter(receiptSearchOperationView);
				return receiptSearchOperationViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_INSURANCE_POLICY_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiptTransferToOwnerView receiptTransferToOwnerView = (ReceiptTransferToOwnerView) GWT.create(ReceiptTransferToOwnerView.class);
				ReceiptTransferToOwnerViewPresenter receiptTransferToOwnerViewPresenter = new ReceiptTransferToOwnerViewPresenter(receiptTransferToOwnerView);
				return receiptTransferToOwnerViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("" +
				"RECEIPT_POLICY_SELECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsurancePolicySelectionView view = (InsurancePolicySelectionView) GWT.create(InsurancePolicySelectionView.class);
				InsurancePolicySelectionViewPresenter presenter = new InsurancePolicySelectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("" +
				"RECEIPT_SUBPOLICY_SELECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InsuranceSubPolicySelectionView view = (InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class);
				InsuranceSubPolicySelectionViewPresenter presenter = new InsuranceSubPolicySelectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("" +
				"RECEIPT_SUBCASUALTY_SELECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubCasualtySelectionView view = (SubCasualtySelectionView) GWT.create(SubCasualtySelectionView.class);
				SubCasualtySelectionViewPresenter presenter = new SubCasualtySelectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("" +
				"RECEIPT_ASSOCIATE_DEBIT_NOTE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiptAssociateWithDebitNoteView view = (ReceiptAssociateWithDebitNoteView) GWT.create(ReceiptAssociateWithDebitNoteView.class);
				ReceiptAssociateWithDebitNoteViewPresenter presenter = new ReceiptAssociateWithDebitNoteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_RETURN", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				ReceiptReturnView view = (ReceiptReturnView) GWT.create(ReceiptReturnView.class);
				ReceiptReturnViewPresenter presenter = new ReceiptReturnViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SERIAL_RECEIPT_CREATION", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				SerialReceiptCreationView view = (SerialReceiptCreationView) GWT.create(SerialReceiptCreationView.class);
				SerialReceiptCreationViewPresenter presenter = new SerialReceiptCreationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_CREATE_PAYMENT_NOTICE", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassCreatePaymentNoticeView view = (MassCreatePaymentNoticeView) GWT.create(MassCreatePaymentNoticeView.class);
				MassCreatePaymentNoticeViewPresenter presenter = new MassCreatePaymentNoticeViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_MARK_FOR_PAYMENT", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MarkForPaymentView view = (MarkForPaymentView) GWT.create(MarkForPaymentView.class);
				MarkForPaymentViewPresenter presenter = new MarkForPaymentViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SERIAL_RECEIPT_MARK_FOR_PAYMENT", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				SerialMarkForPaymentView view = (SerialMarkForPaymentView) GWT.create(SerialMarkForPaymentView.class);
				SerialMarkForPaymentViewPresenter presenter = new SerialMarkForPaymentViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_SEND_RECEIPT_TO_CLIENT", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassSendReceiptView view = (MassSendReceiptView) GWT.create(MassSendReceiptView.class);
				MassSendReceiptViewPresenter presenter = new MassSendReceiptViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_INSURER_ACCOUNTING", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassInsurerAccountingView view = (MassInsurerAccountingView) GWT.create(MassInsurerAccountingView.class);
				MassInsurerAccountingViewPresenter presenter = new MassInsurerAccountingViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_AGENT_ACCOUNTING", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassAgentAccountingView view = (MassAgentAccountingView) GWT.create(MassAgentAccountingView.class);
				MassAgentAccountingViewPresenter presenter = new MassAgentAccountingViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_RECEIPT_RETURN_TO_AGENCY", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassReturnToInsurerView view = (MassReturnToInsurerView) GWT.create(MassReturnToInsurerView.class);
				MassReturnToInsurerViewPresenter presenter = new MassReturnToInsurerViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_SIGNATURE_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassSignatureRequestView view = (MassSignatureRequestView) GWT.create(MassSignatureRequestView.class);
				MassSignatureRequestViewPresenter presenter = new MassSignatureRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SIGNATURE_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				SignatureRequestView view = (SignatureRequestView) GWT.create(SignatureRequestView.class);
				SignatureRequestViewPresenter presenter = new SignatureRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CANCEL_SIGNATURE_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				CancelSignatureRequestView view = (CancelSignatureRequestView) GWT.create(CancelSignatureRequestView.class);
				CancelSignatureRequestViewPresenter presenter = new CancelSignatureRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CREATE_SIGNATURE_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				CreateSignatureRequestView view = (CreateSignatureRequestView) GWT.create(CreateSignatureRequestView.class);
				CreateSignatureRequestViewPresenter presenter = new CreateSignatureRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("DAS_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				DASRequestView view = (DASRequestView) GWT.create(DASRequestView.class);
				DASRequestViewPresenter presenter = new DASRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CANCEL_DAS_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				CancelDASRequestView view = (CancelDASRequestView) GWT.create(CancelDASRequestView.class);
				CancelDASRequestViewPresenter presenter = new CancelDASRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CREATE_DAS_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				CreateDASRequestView view = (CreateDASRequestView) GWT.create(CreateDASRequestView.class);
				CreateDASRequestViewPresenter presenter = new CreateDASRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("REPEAT_DAS_REQUEST", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				RepeatDASRequestView view = (RepeatDASRequestView) GWT.create(RepeatDASRequestView.class);
				RepeatDASRequestViewPresenter presenter = new RepeatDASRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("DAS_REQUEST_TASKS", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				DASRequestTasksView view = (DASRequestTasksView) GWT.create(DASRequestTasksView.class);
				ViewPresenter presenter = new DASRequestTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SIGNATURE_REQUEST_TASKS", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				SignatureRequestTasksView view = (SignatureRequestTasksView) GWT.create(SignatureRequestTasksView.class);
				ViewPresenter presenter = new SignatureRequestTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_TASKS", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				ReceiptTasksView view = (ReceiptTasksView) GWT.create(ReceiptTasksView.class);
				ViewPresenter presenter = new ReceiptTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_GENERAL_TASKS", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				GeneralTasksView view = new GeneralTasksView() {
					
					@Override
					public String getItemType() {
						return "recibo";
					}
				};
				GeneralTasksViewPresenter presenter = new GeneralTasksViewPresenter();
				presenter.setView(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_SEND_PAYMENT", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassSendPaymentView view = (MassSendPaymentView) GWT.create(MassSendPaymentView.class);
				ViewPresenter presenter = new MassSendPaymentViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_RECEIPT_GENERATION", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassReceiptGenerationView view = (MassReceiptGenerationView) GWT.create(MassReceiptGenerationView.class);
				ViewPresenter presenter = new MassReceiptGenerationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_SEND_MESSAGE", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				ReceiptSendMessageView view = (ReceiptSendMessageView) GWT.create(ReceiptSendMessageView.class);
				ViewPresenter presenter = new ReceiptSendMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_RECEIVE_MESSAGE", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				ReceiptReceiveMessageView view = (ReceiptReceiveMessageView) GWT.create(ReceiptReceiveMessageView.class);
				ViewPresenter presenter = new ReceiptReceiveMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_CONVERSATION", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				ReceiptConversationView view = (ReceiptConversationView) GWT.create(ReceiptConversationView.class);
				ViewPresenter presenter = new ReceiptConversationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_CREATE_SECOND_PAYMENT_NOTICE", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassCreateSecondPaymentNoticeView view = (MassCreateSecondPaymentNoticeView) GWT.create(MassCreateSecondPaymentNoticeView.class);
				MassCreateSecondPaymentNoticeViewPresenter presenter = new MassCreateSecondPaymentNoticeViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("VOID_DEBIT_NOTE", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				VoidDebitNoteView view = (VoidDebitNoteView) GWT.create(VoidDebitNoteView.class);
				VoidDebitNoteViewPresenter presenter = new VoidDebitNoteViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new ReceiptDataBrokerImpl(), 
				new SignatureRequestBrokerImpl(),
				new DASRequestBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.RECEIPT	
		};
	}

}
