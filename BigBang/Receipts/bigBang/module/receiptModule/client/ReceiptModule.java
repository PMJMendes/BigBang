package bigBang.module.receiptModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.module.receiptModule.client.dataAccess.ReceiptDataBrokerImpl;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassCreatePaymentNoticeViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassReturnToInsurerViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptOperationsViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptAssociateWithDebitNoteViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptReturnViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToPolicyViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.CancelSignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.CreateSignatureRequestView;
import bigBang.module.receiptModule.client.userInterface.view.MassCreatePaymentNoticeView;
import bigBang.module.receiptModule.client.userInterface.view.MassReturnToInsurerView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptOperationsView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptAssociateWithDebitNoteView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptReturnView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSearchOperationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSectionView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptTransferToPolicyView;
import bigBang.module.receiptModule.client.userInterface.view.SerialReceiptCreationView;
import bigBang.module.receiptModule.client.userInterface.view.SignatureRequestView;

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
				ReceiptTransferToPolicyView receiptTransferToPolicyView = (ReceiptTransferToPolicyView) GWT.create(ReceiptTransferToPolicyView.class);
				ReceiptTransferToPolicyViewPresenter receiptTransferToPolicyViewPresenter = new ReceiptTransferToPolicyViewPresenter(receiptTransferToPolicyView);
				return receiptTransferToPolicyViewPresenter;
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_PAYMENT_NOTICE_CREATION", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				MassCreatePaymentNoticeView view = (MassCreatePaymentNoticeView) GWT.create(MassCreatePaymentNoticeView.class);
				MassCreatePaymentNoticeViewPresenter presenter = new MassCreatePaymentNoticeViewPresenter(view);
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
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new ReceiptDataBrokerImpl()	
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.RECEIPT	
		};
	}

}
