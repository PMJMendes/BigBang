package bigBang.client;

import com.google.gwt.core.client.GWT;

import bigBang.client.tests.TestsView;
import bigBang.client.tests.TestsViewPresenter;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.BigBangDocumentsBroker;
import bigBang.library.client.dataAccess.HistoryBrokerImpl;
import bigBang.library.client.dataAccess.SubProcessesBrokerImpl;
import bigBang.library.client.dataAccess.TypifiedTextBrokerImpl;
import bigBang.library.client.userInterface.presenter.ContactNavigationViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.ContactNavigationView;
import bigBang.library.client.userInterface.view.ContactView;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.ManagerTransferWithToolbarView;
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("TESTS_SECTION", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				TestsView view = (TestsView) GWT.create(TestsView.class);
				ViewPresenter presenter = new TestsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ManagerTransferWithToolbarView view = (ManagerTransferWithToolbarView) GWT.create(ManagerTransferWithToolbarView.class);
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SINGLE_CONTACT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ContactView view = (ContactView) GWT.create(ContactView.class);
				ViewPresenter presenter = new ContactViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CONTACT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ContactNavigationView view = (ContactNavigationView) GWT.create(ContactNavigationView.class);
				ViewPresenter presenter = new ContactNavigationViewPresenter(view);
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
				new SubProcessesBrokerImpl(),
				new TypifiedTextBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return null;
	}

}
