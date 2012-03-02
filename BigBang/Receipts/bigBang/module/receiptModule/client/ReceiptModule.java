package bigBang.module.receiptModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.receiptModule.client.dataAccess.ReceiptDataBrokerImpl;
import bigBang.module.receiptModule.client.userInterface.presenter.OperationNavigationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.OperationNavigationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSearchOperationView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptSectionView;

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
