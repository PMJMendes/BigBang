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
import bigBang.module.receiptModule.client.userInterface.presenter.OperationNavigationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToPolicyViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.OperationNavigationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSearchOperationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSectionView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptTransferToPolicyView;

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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIPT_MASS_OPERATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				OperationNavigationView view = (OperationNavigationView) GWT.create(OperationNavigationView.class);
				OperationNavigationViewPresenter presenter = new OperationNavigationViewPresenter(view);
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
