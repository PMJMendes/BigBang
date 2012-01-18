package bigBang.client;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.BigBangDocumentsBroker;
import bigBang.library.client.dataAccess.HistoryBrokerImpl;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.ManagerTransferView;
import bigBang.library.client.userInterface.view.UndoOperationView;

public class BigBangModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ManagerTransferView view = (ManagerTransferView) GWT.create(ManagerTransferView.class);
				ViewPresenter presenter = new ManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("HISTORY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				UndoOperationView view = (UndoOperationView) GWT.create(UndoOperationView.class);
				UndoOperationViewPresenter presenter = new UndoOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("DOCUMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				DocumentView view = (DocumentView) GWT.create(DocumentView.class);
				ViewPresenter presenter = new DocumentViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new HistoryBrokerImpl()	,
				new BigBangContactsListBroker(),
				new BigBangDocumentsBroker(),
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return null;
	}

}
